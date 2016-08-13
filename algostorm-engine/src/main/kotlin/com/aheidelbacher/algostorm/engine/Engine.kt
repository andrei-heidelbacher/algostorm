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

package com.aheidelbacher.algostorm.engine

import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

import kotlin.concurrent.thread
import kotlin.system.measureNanoTime

/**
 * An asynchronous engine that runs the game loop on its own private thread.
 *
 * All changes to the game state outside of this engine's thread may lead to
 * inconsistent state and concurrency issues. Thus, the engine state should
 * remain private to the engine and modified only in the [handleTick] and
 * [clearState] methods.
 *
 * All the engine methods are thread safe as long as the complete construction
 * of the engine and initialization of the state happen-before any other method
 * call.
 */
abstract class Engine {
    companion object {
        /**
         * Name of the engine thread.
         */
        const val NAME: String = "ALGOSTORM_ENGINE"

        /**
         * Returns the resource with the given name using the [Engine] class
         * [Class.getResourceAsStream] method.
         *
         * @param name the name of the requested resource
         * @throws IllegalArgumentException if the given resource doesn't exist
         */
        @JvmStatic fun getResource(name: String): InputStream =
                requireNotNull(Engine::class.java.getResourceAsStream(name)) {
                    "Resource $name doesn't exist!"
                }
    }

    /**
     * An enum which represents the status of an engine.
     */
    enum class Status {
        RUNNING, STOPPING, STOPPED
    }

    private val stateLock = Any()
    private val statusLock = Any()
    private var process: Thread? = null
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
     * The number of milliseconds spent in an update cycle and the resolution of
     * an atomic time unit.
     */
    protected abstract val millisPerTick: Int

    /**
     * This method is invoked at most once every [millisPerTick] from this
     * engine's thread while this engine is running. The call to this method is
     * synchronized with the state lock.
     *
     * It is the entry point into the game logic code.
     */
    protected abstract fun handleTick(): Unit

    /**
     * Retrieves the current game state and serializes it to the given stream.
     * The call to this method is synchronized with the state lock.
     *
     * @param outputStream the stream to which the game state is written
     */
    protected abstract fun writeStateToStream(outputStream: OutputStream): Unit

    /**
     * Clears the current game state for a clean shutdown. The call to this
     * method is synchronized with the state lock.
     */
    protected abstract fun clearState(): Unit

    /**
     * Sets the [status] to [Status.RUNNING] and starts the engine thread. The
     * engine `status` must be [Status.STOPPED] at the time of calling.
     *
     * The engine thread automatically sets the `status` to `Status.STOPPED`
     * after terminating (either normally or exceptionally).
     *
     * While this engine is running, at most once every [millisPerTick]
     * milliseconds, it will invoke the [handleTick] method. The call to
     * `handleTick` is synchronized with the state lock. Time is measured using
     * [measureNanoTime]. If, at any point, the measured time is negative, the
     * engine thread throws an [IllegalStateException] and terminates.
     *
     * @throws IllegalStateException if the `status` is not `Status.STOPPED` or
     * if [isShutdown] is `true`
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
                        val elapsedTimeMillis = measureNanoTime {
                            synchronized(stateLock) {
                                handleTick()
                            }
                        } / 1000000
                        check(elapsedTimeMillis >= 0) {
                            "Elapsed time millis can't be negative!"
                        }
                        val sleepTimeMillis = millisPerTick - elapsedTimeMillis
                        if (sleepTimeMillis > 0) {
                            Thread.sleep(sleepTimeMillis)
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
                process?.join()
            }
            process = null
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
        }
    }

    /**
     * Acquires the state lock and calls the [writeStateToStream] method.
     *
     * @param outputStream the stream to which the game state is written
     */
    fun serializeState(outputStream: OutputStream) {
        synchronized(stateLock) {
            writeStateToStream(outputStream)
        }
    }
}
