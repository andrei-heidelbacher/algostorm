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

package com.aheidelbacher.algostorm.android.test

import com.aheidelbacher.algostorm.engine.Engine
import com.aheidelbacher.algostorm.engine.audio.AudioDriver
import com.aheidelbacher.algostorm.engine.graphics2d.Color
import com.aheidelbacher.algostorm.engine.graphics2d.GraphicsDriver
import com.aheidelbacher.algostorm.engine.graphics2d.Matrix
import com.aheidelbacher.algostorm.engine.input.InputDriver
import java.io.OutputStream


class TestEngine(
        audioDriver: AudioDriver,
        graphicsDriver: GraphicsDriver,
        inputDriver: InputDriver
) : Engine(audioDriver, graphicsDriver, inputDriver) {
    private data class Rectangle(
            val x: Int,
            val y: Int,
            val width: Int,
            val height: Int
    )

    override val millisPerUpdate: Int
        get() = 25


    private val rectangle = Rectangle(0, 0, 64, 64)

    override fun onStart() {
    }

    override fun onRender() {
        if (graphicsDriver.isCanvasReady) {
            graphicsDriver.lockCanvas()
            graphicsDriver.drawRectangle(
                    color = Color("#FF0000FF"),
                    width = rectangle.width,
                    height = rectangle.height,
                    matrix = Matrix.identity().postTranslate(
                            dx = rectangle.x.toFloat(),
                            dy = rectangle.y.toFloat()
                    )
            )
            graphicsDriver.unlockAndPostCanvas()
        }
    }

    override fun onHandleInput() {
    }

    override fun onUpdate() {
    }

    override fun onSerializeState(outputStream: OutputStream) {
        serializationDriver.writeValue(outputStream, rectangle)
    }

    override fun onStop() {
    }

    override fun onShutdown() {
    }
}
