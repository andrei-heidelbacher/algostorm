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

package com.andreihh.algostorm.core.engine

import com.andreihh.algostorm.core.drivers.audio.AudioDriver
import com.andreihh.algostorm.core.drivers.graphics2d.GraphicsDriver
import com.andreihh.algostorm.core.drivers.input.InputDriver
import com.andreihh.algostorm.core.drivers.io.FileSystemDriver

abstract class Handler {
    protected lateinit var audioDriver: AudioDriver
        private set

    protected lateinit var graphicsDriver: GraphicsDriver
        private set

    protected lateinit var inputDriver: InputDriver
        private set

    protected lateinit var fileSystemDriver: FileSystemDriver
        private set

    /**
     * The positive number of milliseconds spent in an update cycle and the
     * resolution of an atomic time unit.
     */
    abstract val millisPerUpdate: Int

    fun onInit(platform: Platform, args: Map<String, Any?>) {
        audioDriver = platform.audioDriver
        graphicsDriver = platform.graphicsDriver
        inputDriver = platform.inputDriver
        fileSystemDriver = platform.fileSystemDriver
        onInit(args)
    }

    /**
     * The entry point into the initialization logic of the engine.
     *
     * This method is invoked after the engine is created and before the
     * engine can be started.
     */
    abstract protected fun onInit(args: Map<String, Any?>)

    /**
     * The entry point into the initialization logic after starting the
     * engine thread.
     *
     * This method is invoked right after starting the private engine thread
     * and is run on the engine thread.
     */
    abstract fun onStart()

    /**
     * The entry point into the game logic.
     *
     * This method is invoked at most once every [millisPerUpdate] while
     * this engine is running and is run on the engine thread.
     */
    abstract fun onUpdate()

    /**
     * The entry point into the clean-up logic before stopping the private
     * engine thread.
     *
     * This method is invoked right before the engine thread is stopped and
     * is run on the engine thread.
     */
    abstract fun onStop()

    /**
     * The entry point into the clean-up logic for releasing the engine.
     *
     * This method is invoked right before this engine's drivers are
     * released.
     */
    abstract fun onRelease()

    /**
     * The entry point into the error handling logic when an exception
     * occurs on the engine thread.
     *
     * This method is invoked right before the engine thread terminates and
     * is run on the engine thread.
     *
     * @param cause the error which occurred on the engine thread
     */
    abstract fun onError(cause: Exception)
}
