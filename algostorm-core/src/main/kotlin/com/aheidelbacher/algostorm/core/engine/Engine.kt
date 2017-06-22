/*
 * Copyright 2017 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.aheidelbacher.algostorm.core.engine

import com.aheidelbacher.algostorm.core.drivers.Driver
import com.aheidelbacher.algostorm.core.drivers.client.audio.AudioDriver
import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.GraphicsDriver
import com.aheidelbacher.algostorm.core.drivers.client.input.InputDriver
import com.aheidelbacher.algostorm.core.drivers.io.FileSystemDriver

import java.io.InputStream
import java.io.OutputStream

import kotlin.concurrent.thread
import kotlin.system.measureNanoTime

/**
 * An asynchronous engine that runs the game loop on its own private thread.
 *
 * All changes to the game state outside of this engine's thread may lead to
 * inconsistent state and concurrency issues. Thus, the engine state should
 * remain private to the engine and modified only in the [onUpdate] and
 * [onRelease] methods.
 *
 * All the engine methods are thread safe as long as the complete construction
 * of the engine and initialization of the state happen-before any other method
 * call.
 *
 * @constructor initializes this engine's drivers
 * @property audioDriver the driver that handles playing music and sound effects
 * @property graphicsDriver the driver that handles drawing to the screen
 * @property inputDriver the driver that handles reading input from the user
 * @property fileSystemDriver the driver that handles files and resources
 */
abstract class Engine(
        protected val audioDriver: AudioDriver,
        protected val graphicsDriver: GraphicsDriver,
        protected val inputDriver: InputDriver,
        protected val fileSystemDriver: FileSystemDriver
) {
    companion object {
        /** Name of the engine thread. */
        const val NAME: String = "ALGOSTORM"
    }

    /** The status of an engine. */
    enum class Status {
        UNINITIALIZED, RUNNING, STOPPING, STOPPED, RELEASED
    }

    private var process: Thread? = null

    /** The current status of this engine. */
    @Volatile var status: Status = Status.UNINITIALIZED
        private set

    /**
     * The positive number of milliseconds spent in an update cycle and the
     * resolution of an atomic time unit.
     */
    protected abstract val millisPerUpdate: Int

    /**
     * The entry point into the initialization logic of the engine.
     *
     * This method is invoked after the engine is created and before the engine
     * can be started.
     */
    protected abstract fun onInit(src: InputStream?): Unit

    /**
     * The entry point into the initialization logic after starting the engine
     * thread.
     *
     * This method is invoked right after starting the private engine thread and
     * is run on the engine thread.
     */
    protected abstract fun onStart(): Unit

    /**
     * The entry point into the game logic.
     *
     * This method is invoked at most once every [millisPerUpdate] while this
     * engine is running and is run on the engine thread.
     */
    protected abstract fun onUpdate(): Unit

    /**
     * The entry point into the clean-up logic before stopping the private
     * engine thread.
     *
     * This method is invoked right before the engine thread is stopped and is
     * run on the engine thread.
     */
    protected abstract fun onStop(): Unit

    /**
     * The entry point into the error handling logic when an exception occurs on
     * the engine thread.
     *
     * This method is invoke right before the engine thread terminates and is
     * run on the engine thread.
     *
     * @param cause the error which occurred on the engine thread
     */
    protected abstract fun onError(cause: Exception): Unit

    /**
     * Retrieves the current game state and serializes it to the given stream.
     *
     * @param out the stream to which the game state is written
     */
    protected abstract fun onSerializeState(out: OutputStream): Unit

    /**
     * The entry point into the clean-up logic for releasing the engine.
     *
     * This method is invoked right before this engine's drivers are released.
     */
    protected abstract fun onRelease(): Unit

    @Throws(Exception::class)
    private fun run() {
        onStart()
        while (status == Status.RUNNING) {
            val elapsedMillis = measureNanoTime(this::onUpdate) / 1000000
            check(elapsedMillis >= 0) { "Elapsed time can't be negative!" }
            val updateMillis = millisPerUpdate
            check(updateMillis > 0) { "Update time must be positive!" }
            val sleepMillis = updateMillis - elapsedMillis
            if (sleepMillis > 0) {
                Thread.sleep(sleepMillis)
            }
        }
        onStop()
    }

    fun init(inputStream: InputStream?) {
        check(status == Status.UNINITIALIZED) { "Engine already initialized!" }
        onInit(inputStream)
        status = Status.STOPPED
    }

    /**
     * Sets the [status] to [Status.RUNNING] and starts the engine thread.
     *
     * While this engine is running, at most once every [millisPerUpdate]
     * milliseconds, it will invoke the [onUpdate] method on the engine thread.
     *
     * Time is measured using [measureNanoTime]. If, at any point, the measured
     * time or [millisPerUpdate] is negative, the engine thread throws an
     * [IllegalStateException] and terminates.
     *
     * @throws IllegalStateException if the `status` is not `Status.STOPPED`
     */
    fun start() {
        check(status == Status.STOPPED) { "Engine can't start if not stopped!" }
        status = Status.RUNNING
        process = thread(name = NAME) {
            try {
                run()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    /**
     * Serializes the game state to the given stream.
     *
     * @param outputStream the stream to which the game state is written
     */
    fun serializeState(outputStream: OutputStream) {
        check(status == Status.STOPPED) {
            "Can't serialize state if engine isn't stopped!"
        }
        onSerializeState(outputStream)
    }

    /**
     * Sets the engine [status] to [Status.STOPPING] and then joins the engine
     * thread to the current thread, waiting at most [timeoutMillis]
     * milliseconds.
     *
     * If the join succeeds, the `status` will be set to `Status.STOPPED`.
     *
     * The timeout must be positive.
     *
     * @throws InterruptedException if the current thread is interrupted while
     * waiting for this engine to stop
     * @throws IllegalStateException if this engine attempts to stop itself,
     * because the engine thread can't join itself
     */
    @Throws(InterruptedException::class)
    fun stop(timeoutMillis: Int) {
        require(timeoutMillis > 0) { "Timeout must be positive!" }
        check(status == Status.RUNNING || status == Status.STOPPING) {
            "Engine can't stop if not running or stopping!"
        }
        status = Status.STOPPING
        check(process != Thread.currentThread()) { "Engine can't stop itself!" }
        process?.join(timeoutMillis.toLong())
        process = null
        status = Status.STOPPED
    }

    /**
     * Performs clean-up logic and releases this engine's drivers.
     *
     * The engine `status` must be `Status.STOPPED`.
     *
     * @throws IllegalStateException if this engine is not stopped
     */
    @Throws(InterruptedException::class)
    fun release() {
        check(status == Status.STOPPED) {
            "Engine can't be released if not stopped!"
        }
        onRelease()
        listOf(
                audioDriver,
                graphicsDriver,
                inputDriver,
                fileSystemDriver
        ).forEach(Driver::release)
        status = Status.RELEASED
    }
}
