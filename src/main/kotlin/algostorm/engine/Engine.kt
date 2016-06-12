/*
 * Copyright 2016 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package algostorm.engine

import algostorm.ecs.Component
import algostorm.ecs.EntitySystem
import algostorm.ecs.MutableEntityManager
import algostorm.event.EventBus
import algostorm.serialization.SaveState
import algostorm.serialization.Serializer

import java.io.OutputStream
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

/**
 * An asynchronous engine that runs the game loop on its own private thread.
 *
 * All the systems passed in the state will have their handlers subscribed to
 * the event bus upon the creation of the engine. The subscriptions are
 * synchronized on the internal state lock. All the engine methods are thread
 * safe.
 *
 * @property state the state of this engine. All changes made to the `state`
 * outside of this engine's thread may lead to inconsistent state and
 * concurrency issues. Thus, the engine state should remain private to the
 * engine.
 * @property systems the systems which handle the logic of the game
 * @property millisPerTick the number of milliseconds spent in an update cycle
 * and the resolution of an atomic time unit
 */
abstract class Engine protected constructor(
        private val state: State,
        private val systems: List<EntitySystem>,
        private val millisPerTick: Int
) {
    companion object {
        /**
         * Name of the engine thread.
         */
        const val NAME = "Algostorm-Engine"
    }

    /**
     * An enum which represents the status of an engine.
     */
    enum class Status {
        RUNNING, STOPPING, STOPPED
    }

    /**
     * The state of an engine.
     *
     * @property entityManager the entity manager that handles all the entities
     * in the game
     * @property properties various properties used by the game systems. The
     * stored properties must not be generic, otherwise they may not be
     * serializable.
     * @property eventBus the event bus which handles all the events in the game
     */
    data class State(
            val entityManager: MutableEntityManager,
            val properties: MutableMap<String, Any>,
            val eventBus: EventBus
    )

    private val stateLock = Any()
    private val subscriptions = synchronized(stateLock) {
        systems.flatMap { system ->
            system.handlers.map { handler -> state.eventBus.subscribe(handler) }
        }
    }

    private val statusLock = Any()
    private var process = synchronized(statusLock) { thread(name = NAME) {} }
    private val internalStatus = AtomicReference(Status.STOPPED)
    private val internalShutdownStatus = AtomicBoolean(false)

    /**
     * The current status of this engine.
     */
    val status: Status
        get() = internalStatus.get()

    /**
     * The current shutdown status of this engine.
     */
    val isShutdown: Boolean
        get() = internalShutdownStatus.get()

    /**
     * This method is invoked at most once every [millisPerTick] from this
     * engine's thread while this engine is running. The call to this method is
     * synchronized with the `state` lock.
     *
     * It is the entry point into the game logic code.
     *
     * @param state this engine's [state]
     */
    protected abstract fun handleTick(state: State): Unit

    /**
     * Sets the [status] to [Status.RUNNING] and starts the engine thread. The
     * engine `status` must be [Status.STOPPED] at the time of calling.
     *
     * The engine thread automatically sets the `status` to `Status.STOPPED`
     * after terminating (either normally or exceptionally).
     *
     * While this engine is running, at most once every [millisPerTick]
     * milliseconds, it will invoke the [handleTick] method. The call to
     * `handleTick` is synchronized with the `state` lock.
     *
     * @throws IllegalStateException if the `status` is not `Status.STOPPED` or
     * if this engine has been shutdown
     */
    fun start() {
        synchronized(statusLock) {
            check(!internalShutdownStatus.get()) {
                "Can't start the engine if it has been shutdown!"
            }
            check(internalStatus.compareAndSet(
                    Status.STOPPED,
                    Status.RUNNING
            )) { "Can't start the engine if it isn't stopped!" }
            process = thread(name = NAME) {
                try {
                    while (status == Status.RUNNING) {
                        val elapsedTime = measureTimeMillis {
                            synchronized(stateLock) {
                                handleTick(state)
                            }
                        }
                        val sleepTime = millisPerTick - elapsedTime
                        if (sleepTime > 0) {
                            Thread.sleep(sleepTime)
                        }
                    }
                } finally {
                    internalStatus.set(Status.STOPPED)
                }
            }
        }
    }

    /**
     * Sets the engine [status] to [Status.STOPPING] and then joins the engine
     * thread to the current thread. If the join succeeds, the `status` will be
     * set to [Status.STOPPED].
     *
     * If this engine attempts to stop itself, it will signal to stop processing
     * ticks, but will not join. As a consequence, subsequent calls to `status`
     * may return `Status.STOPPING`.
     *
     * @throws InterruptedException if the current thread is interrupted while
     * waiting for this engine to stop
     */
    @Throws(InterruptedException::class)
    fun stop() {
        synchronized(statusLock) {
            internalStatus.compareAndSet(Status.RUNNING, Status.STOPPING)
            if (process != Thread.currentThread()) {
                process.join()
            }
        }
    }

    /**
     * Blocks until all events have been handled and the event bus becomes
     * empty, deletes all entities from the entity manager and removes all
     * properties.
     */
    fun clearState() {
        synchronized(stateLock) {
            state.eventBus.publishPosts()
            state.entityManager.clear()
            state.properties.clear()
        }
    }

    /**
     * [Stops][stop] and [clears][clearState] this engine, unsubscribes all its
     * systems from the event bus and sets the [isShutdown] flag to `true`.
     *
     * @throws IllegalStateException if the engine is already shutdown
     * @throws InterruptedException if the current thread is interrupted while
     * waiting for this engine to stop
     */
    @Throws(InterruptedException::class)
    fun shutdown() {
        synchronized(statusLock) {
            check(internalShutdownStatus.compareAndSet(false, true)) {
                "Can't shutdown the engine multiple times!"
            }
            stop()
        }
        synchronized(stateLock) {
            clearState()
            subscriptions.forEach { it.unsubscribe() }
        }
    }

    /**
     * Retrieves the current [SaveState] and serializes it to the given stream
     * using the [Serializer.writeValue] method.
     *
     * @param outputStream the stream to which the state is written
     */
    fun saveState(outputStream: OutputStream) {
        synchronized(stateLock) {
            val entities = state.entityManager.entities.associate {
                it.id to it.components
            }
            val properties = state.properties
            Serializer.writeValue(outputStream, SaveState(entities, properties))
        }
    }

    /**
     * [Clears][clearState] this engine and loads the given game state.
     *
     * @param entities a mapping from entity ids to [Component] lists.
     * @param properties the properties of the game
     */
    fun loadState(
            entities: Map<Int, Iterable<Component>>,
            properties: Map<String, Any>
    ) {
        synchronized(stateLock) {
            clearState()
            entities.forEach { state.entityManager.create(it.key, it.value) }
            state.properties.putAll(properties)
        }
    }
}
