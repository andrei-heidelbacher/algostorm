/*
 * Copyright (c) 2017  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.andreihh.algostorm.core.engine

import com.andreihh.algostorm.core.engine.Engine.Status.RELEASED
import com.andreihh.algostorm.core.engine.Engine.Status.RUNNING
import com.andreihh.algostorm.core.engine.Engine.Status.STOPPED
import com.andreihh.algostorm.core.engine.Engine.Status.STOPPING
import com.andreihh.algostorm.core.engine.Engine.Status.UNINITIALIZED
import java.util.ServiceLoader
import kotlin.concurrent.thread
import kotlin.system.measureNanoTime

/**
 * An asynchronous engine that runs the game loop on its own private thread.
 *
 * All changes to the game state outside of this engine's thread may lead to
 * inconsistent state and concurrency issues. Thus, the engine state should
 * remain private to the engine and modified only in the [Handler.onUpdate] and
 * [Handler.onRelease] methods.
 *
 * All the engine methods are thread safe as long as the complete construction
 * of the engine and initialization of the state happen-before any other method
 * call.
 *
 * @property platform the platform of this engine
 */
class Engine(private val platform: Platform) {
    companion object {
        /** Name of the engine thread. */
        const val NAME: String = "ALGOSTORM"
    }

    /** The status of an engine. */
    enum class Status {
        UNINITIALIZED, RUNNING, STOPPING, STOPPED, RELEASED
    }

    /** The current status of this engine. */
    @Volatile var status: Status = UNINITIALIZED
        private set

    private val handler = ServiceLoader.load(Handler::class.java).first()
    private var process: Thread? = null

    @Throws(Exception::class)
    private fun run() {
        handler.onStart()
        while (status == RUNNING) {
            val elapsedMillis = measureNanoTime(handler::onUpdate) / 1000000
            check(elapsedMillis >= 0) { "Elapsed time can't be negative!" }
            val updateMillis = handler.millisPerUpdate
            check(updateMillis > 0) { "Update time must be positive!" }
            val sleepMillis = updateMillis - elapsedMillis
            if (sleepMillis > 0) {
                Thread.sleep(sleepMillis)
            }
        }
        handler.onStop()
    }

    fun init(args: Map<String, Any?>) {
        check(status == UNINITIALIZED) { "Engine already initialized!" }
        handler.onInit(platform, args)
        status = STOPPED
    }

    /**
     * Sets the [status] to [Status.RUNNING] and starts the engine thread.
     *
     * While this engine is running, at most once every
     * [Handler.millisPerUpdate] milliseconds, it will invoke the
     * [Handler.onUpdate] method on the engine thread.
     *
     * Time is measured using [measureNanoTime]. If, at any point, the measured
     * time or [Handler.millisPerUpdate] is negative, the engine thread throws
     * an [IllegalStateException] and terminates.
     *
     * @throws IllegalStateException if the `status` is not `Status.STOPPED`
     */
    fun start() {
        check(status == STOPPED) { "Engine can't start if not stopped!" }
        status = RUNNING
        process = thread(name = NAME) {
            try {
                run()
            } catch (e: Exception) {
                handler.onError(e)
            }
        }
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
        check(status == RUNNING || status == STOPPING) {
            "Engine can't stop if not running or stopping!"
        }
        status = STOPPING
        check(process != Thread.currentThread()) { "Engine can't stop itself!" }
        process?.join(timeoutMillis.toLong())
        process = null
        status = STOPPED
    }

    /**
     * Performs clean-up logic and releases this engine's drivers.
     *
     * The engine `status` must be `Status.STOPPED`.
     *
     * @throws IllegalStateException if this engine is not stopped
     */
    fun release() {
        check(status == STOPPED) { "Engine can't be released if not stopped!" }
        handler.onRelease()
        platform.release()
        status = RELEASED
    }
}
