/*
 * Copyright (c) 2016  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
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

import com.aheidelbacher.algostorm.engine.audio.AudioDriver
import com.aheidelbacher.algostorm.engine.driver.Driver
import com.aheidelbacher.algostorm.engine.graphics2d.GraphicsDriver
import com.aheidelbacher.algostorm.engine.input.InputDriver
import com.aheidelbacher.algostorm.engine.script.KotlinScriptDriver
import com.aheidelbacher.algostorm.engine.script.ScriptDriver
import com.aheidelbacher.algostorm.engine.serialization.JsonDriver
import com.aheidelbacher.algostorm.engine.serialization.SerializationDriver

import java.io.FileNotFoundException
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
 * remain private to the engine and modified only in the [onUpdate] and
 * [onShutdown] methods.
 *
 * All the engine methods are thread safe as long as the complete construction
 * of the engine and initialization of the state happen-before any other method
 * call.
 *
 * @property audioDriver the driver that handles playing music and sound effects
 * @property graphicsDriver the driver that handles drawing to the screen
 * @property inputDriver the driver that handles reading input from the user
 * @property scriptDriver the driver that handles running scripts
 * @property serializationDriver the driver that handles the serialization and
 * deserialization of the game state
 * @constructor initializes this engine's drivers
 */
abstract class Engine(
        protected val audioDriver: AudioDriver,
        protected val graphicsDriver: GraphicsDriver,
        protected val inputDriver: InputDriver,
        protected val scriptDriver: ScriptDriver = KotlinScriptDriver(),
        protected val serializationDriver: SerializationDriver = JsonDriver()
) {
    companion object {
        /** Name of the engine thread. */
        const val NAME: String = "ALGOSTORM"
    }

    /** The status of an engine. */
    enum class Status {
        RUNNING, STOPPING, STOPPED
    }

    private val stateLock = Any()
    private val statusLock = Any()
    private var process: Thread? = null
    private val internalStatus = AtomicReference(Status.STOPPED)
    private val internalShutdownStatus = AtomicBoolean(false)

    init {
    }

    /** The current status of this engine. */
    val status: Status
        get() = internalStatus.get()

    /** The current shutdown status of this engine. */
    val isShutdown: Boolean
        get() = internalShutdownStatus.get()

    /**
     * The number of milliseconds spent in an update cycle and the resolution of
     * an atomic time unit.
     *
     * Must be positive.
     */
    protected abstract val millisPerUpdate: Int

    /**
     * The entry point into the initialization logic for starting the engine
     * thread.
     *
     * This method is invoked right after starting the private engine thread.
     * The call to this method is synchronized with the state lock.
     */
    protected abstract fun onStart(): Unit

    /**
     * The entry point into the rendering logic.
     *
     * This method is invoked right before [onHandleInput]. The call to this
     * method is synchronized with the state lock.
     */
    protected abstract fun onRender(): Unit

    /**
     * The entry point into the input-handling logic.
     *
     * This method is invoked right before [onUpdate]. The call to this method
     * is synchronized with the state lock.
     */
    protected abstract fun onHandleInput(): Unit

    /**
     * The entry point into the game logic.
     *
     * This method is invoked at most once every [millisPerUpdate] while this
     * engine is running. The call to this method is synchronized with the state
     * lock.
     */
    protected abstract fun onUpdate(): Unit

    /**
     * Retrieves the current game state and serializes it to the given stream.
     *
     * The call to this method is synchronized with the state lock.
     *
     * @param outputStream the stream to which the game state is written
     */
    protected abstract fun onSerializeState(outputStream: OutputStream): Unit

    /**
     * The entry point into the clean-up logic for stopping the private engine
     * thread.
     *
     * This method is invoked right before [stop] returns. The call to this
     * method is synchronized with the state lock.
     */
    protected abstract fun onStop(): Unit

    /**
     * The entry point into the clean-up logic for shutting down the engine.
     *
     * This method is invoked right before [shutdown] releases this engine's
     * drivers. The call to this method is synchronized with the state lock.
     */
    protected abstract fun onShutdown(): Unit

    /**
     * Sets the [status] to [Status.RUNNING] and starts the engine thread.
     *
     * The engine thread automatically sets the `status` to `Status.STOPPED`
     * after terminating (either normally or exceptionally).
     *
     * While this engine is running, at most once every [millisPerUpdate]
     * milliseconds, it will invoke, in order, the following methods:
     * [onRender], [onHandleInput] and [onUpdate]. These calls are synchronized
     * with the state lock.
     *
     * Time is measured using [measureNanoTime]. If, at any point, the measured
     * time or `millisPerUpdate` is negative, the engine thread throws an
     * [IllegalStateException] and terminates.
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
                    synchronized(stateLock) {
                        onStart()
                    }
                    while (status == Status.RUNNING) {
                        val elapsedMillis = measureNanoTime {
                            synchronized(stateLock) {
                                onRender()
                                onHandleInput()
                                onUpdate()
                            }
                        } / 1000000
                        check(elapsedMillis >= 0) {
                            "Elapsed time millis can't be negative!"
                        }
                        val updateMillis = millisPerUpdate
                        check(updateMillis > 0) {
                            "Millis spent in an update cycle must be positive!"
                        }
                        val sleepMillis = updateMillis - elapsedMillis
                        if (sleepMillis > 0) {
                            Thread.sleep(sleepMillis)
                        }
                    }
                } finally {
                    internalStatus.set(Status.STOPPED)
                }
            }
        }
    }

    /**
     * Serializes the game state to the given stream.
     *
     * @param outputStream the stream to which the game state is written
     */
    fun serializeState(outputStream: OutputStream) {
        synchronized(stateLock) {
            onSerializeState(outputStream)
        }
    }

    /**
     * Sets the engine [status] to [Status.STOPPING] and then joins the engine
     * thread to the current thread.
     *
     * If the join succeeds, the `status` will be set to `Status.STOPPED`.
     *
     * @throws InterruptedException if the current thread is interrupted while
     * waiting for this engine to stop
     * @throws IllegalStateException if this engine attempts to stop itself,
     * because the engine thread can't join itself
     */
    @Throws(InterruptedException::class)
    fun stop() {
        synchronized(statusLock) {
            internalStatus.compareAndSet(Status.RUNNING, Status.STOPPING)
            check(process != Thread.currentThread()) {
                "Engine can't stop itself!"
            }
            process?.join()
            process = null
        }
        synchronized(stateLock) {
            onStop()
        }
    }

    /**
     * Sets the [isShutdown] flag to `true`, stops this engine, performs
     * clean-up logic and releases this engine's drivers, in this order.
     *
     * The drivers passed in the constructor are released while holding the
     * state lock.
     *
     * @throws IllegalStateException if this engine is already shutdown
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
            onShutdown()
            listOf(
                    audioDriver,
                    graphicsDriver,
                    inputDriver,
                    scriptDriver,
                    serializationDriver
            ).forEach(Driver::release)
        }
    }
}
