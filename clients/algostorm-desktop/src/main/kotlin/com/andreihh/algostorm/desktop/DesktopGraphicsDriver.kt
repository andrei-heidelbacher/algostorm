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

package com.andreihh.algostorm.desktop

import com.andreihh.algostorm.core.drivers.graphics2d.Bitmap
import com.andreihh.algostorm.core.drivers.graphics2d.Color
import com.andreihh.algostorm.core.drivers.graphics2d.GraphicsDriver
import com.andreihh.algostorm.core.drivers.io.Resource
import javafx.application.Platform
import javafx.scene.canvas.Canvas
import javafx.scene.image.Image
import javafx.scene.paint.Color.rgb

class DesktopGraphicsDriver(canvas: Canvas) : GraphicsDriver {
    private val gc = canvas.graphicsContext2D
    private val bitmaps = hashMapOf<Resource<Bitmap>, Image>()
    private val drawCalls = arrayListOf<() -> Unit>()
    private var isLocked = false

    override val isCanvasReady: Boolean
        get() = gc.canvas.scene != null

    override val width: Int
        get() = gc.canvas.width.toInt()

    override val height: Int
        get() = gc.canvas.height.toInt()

    override fun loadBitmap(bitmap: Resource<Bitmap>) {
        javaClass.getResourceAsStream("/assets/${bitmap.path}").use { src ->
            bitmaps[bitmap] = Image(src)
        }
    }

    private fun checkIsLocked() {
        check(isLocked) { "Canvas is not locked!" }
    }

    private fun checkIsNotLocked() {
        check(!isLocked) { "Canvas is already locked!" }
    }

    override fun lockCanvas() {
        checkIsNotLocked()
        isLocked = true
    }

    override fun save() {
        checkIsLocked()
        drawCalls.add { gc.save() }
    }

    override fun translate(dx: Float, dy: Float) {
        checkIsLocked()
        drawCalls.add { gc.translate(1.0 * dx, 1.0 * dy) }
    }

    override fun scale(sx: Float, sy: Float) {
        checkIsLocked()
        drawCalls.add { gc.scale(1.0 * sx, 1.0 * sy) }
    }

    override fun rotate(degrees: Float) {
        checkIsLocked()
        drawCalls.add { gc.rotate(1.0 * degrees) }
    }

    override fun restore() {
        checkIsLocked()
        drawCalls.add { gc.restore() }
    }

    override fun drawBitmap(
            bitmap: Resource<Bitmap>,
            sx: Int,
            sy: Int,
            sw: Int,
            sh: Int,
            dx: Int,
            dy: Int,
            dw: Int,
            dh: Int
    ) {
        checkIsLocked()
        val image = requireNotNull(bitmaps[bitmap])
        drawCalls.add {
            gc.drawImage(
                    image,
                    1.0 * sx,
                    1.0 * sy,
                    1.0 * sw,
                    1.0 * sh,
                    1.0 * dx,
                    1.0 * dy,
                    1.0 * dw,
                    1.0 * dh
            )
        }
    }

    override fun drawColor(color: Color) {
        checkIsLocked()
        drawCalls.add {
            gc.fill = rgb(color.r, color.g, color.b, 1.0 * color.a / 255)
            gc.fillRect(0.0, 0.0, gc.canvas.width, gc.canvas.height)
        }
    }

    override fun drawRectangle(color: Color, x: Int, y: Int, w: Int, h: Int) {
        checkIsLocked()
        drawCalls.add {
            gc.fill = rgb(color.r, color.g, color.b, 1.0 * color.a / 255)
            gc.fillRect(1.0 * x, 1.0 * y, 1.0 * w, 1.0 * h)
        }
    }

    override fun unlockAndPostCanvas() {
        checkIsLocked()
        val jobs = drawCalls.toList()
        drawCalls.clear()
        Platform.runLater {
            jobs.forEach { it() }
        }
        isLocked = false
    }

    override fun release() {
        bitmaps.clear()
    }
}
