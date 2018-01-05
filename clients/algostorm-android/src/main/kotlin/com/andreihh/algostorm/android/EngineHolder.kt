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

package com.andreihh.algostorm.android

import android.content.Context
import android.os.Bundle
import android.view.SurfaceView
import com.andreihh.algostorm.core.drivers.input.Input
import com.andreihh.algostorm.core.drivers.ui.UiListener
import com.andreihh.algostorm.core.engine.Engine
import com.andreihh.algostorm.core.engine.Platform

class EngineHolder(context: Context) {
    private val appContext = context.applicationContext
    private val audioDriver = AndroidAudioDriver(appContext)
    private val graphicsDriver = AndroidGraphicsDriver(appContext)
    private val inputDriver = AndroidInputDriver(appContext)
    private val fileSystemDriver = AndroidFileSystemDriver(appContext)
    private val uiDriver = AndroidUiDriver(appContext)
    private val platform = Platform(
        audioDriver, graphicsDriver, inputDriver, fileSystemDriver, uiDriver
    )

    private val engine = Engine(platform)

    private fun Bundle.toMap(): Map<String, Any?> =
            keySet().associate { it to get(it) }

    fun init(args: Bundle) {
        engine.init(args.toMap())
    }

    fun sendInput(input: Input) {
        inputDriver.write(input)
    }

    fun setListener(listener: UiListener) {
        uiDriver.setListener(listener)
    }

    fun attachSurface(surfaceView: SurfaceView) {
        surfaceView.setOnTouchListener(inputDriver)
        graphicsDriver.attachSurface(surfaceView.holder)
    }

    fun detachSurface() {
        graphicsDriver.detachSurface()
    }

    fun start() {
        engine.start()
    }

    fun stop() {
        engine.stop(100)
    }

    fun release() {
        engine.release()
    }
}
