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

package com.aheidelbacher.algostorm.android

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.view.SurfaceHolder

import com.aheidelbacher.algostorm.core.engine.driver.Resource
import com.aheidelbacher.algostorm.core.engine.graphics2d.Color
import com.aheidelbacher.algostorm.core.engine.graphics2d.GraphicsDriver

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

class AndroidGraphicsDriver(
        surfaceHolder: SurfaceHolder,
        private val scale: Float
) : GraphicsDriver {
    companion object {
        private val bitmaps = ConcurrentHashMap<Resource, Bitmap>()

        fun loadBitmap(resource: Resource) {
            if (resource !in bitmaps) {
                resource.inputStream().use { src ->
                    bitmaps[resource] = BitmapFactory.decodeStream(src)
                }
            }
        }

        /**
         * Retrieves the requested bitmap.
         *
         * @param resource the requested image resource
         * @return the requested bitmap, or `null` if it wasn't loaded
         */
        fun getBitmap(resource: Resource): Bitmap? = bitmaps[resource]
    }

    private var surfaceHolder: SurfaceHolder? = surfaceHolder
    private var canvas: Canvas? = null
    private val isLocked = AtomicBoolean(false)

    private val srcRect = Rect()
    private val dstRect = Rect()
    private val paint = Paint()

    private val Int.pxToDp: Float
        get() = toFloat() / scale

    @Volatile override var height: Int = 0
        private set

    @Volatile override var width: Int = 0
        private set

    private fun checkIsLocked() {
        check(isLocked.get()) { "Canvas is not locked!" }
    }

    override fun loadBitmap(resource: Resource) {
        Companion.loadBitmap(resource)
    }

    override fun save() {
        checkIsLocked()
        canvas?.save()
    }

    override fun translate(dx: Float, dy: Float) {
        checkIsLocked()
        canvas?.translate(dx, dy)
    }

    override fun scale(sx: Float, sy: Float) {
        checkIsLocked()
        canvas?.scale(sx, sy)
    }

    override fun rotate(degrees: Float) {
        checkIsLocked()
        canvas?.rotate(degrees)
    }

    override fun restore() {
        checkIsLocked()
        canvas?.restore()
    }

    override fun drawBitmap(
            resource: Resource,
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
        val bitmap = requireNotNull(getBitmap(resource))
        srcRect.set(sx, sy, sx + sw, sy + sh)
        dstRect.set(dx, dy, dx + dw, dy + dh)
        canvas?.drawBitmap(bitmap, srcRect, dstRect, null)
    }

    /*override fun drawBitmap(
            resource: Resource,
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            matrix: Matrix
    ) {
        checkIsLocked()
        val bitmap = requireNotNull(getBitmap(resource)) {
            "Invalid bitmap $resource!"
        }
        srcRect.set(x, y, x + width, y + height)
        dstRect.set(0, 0, width, height)
        for (i in 0..8) {
            matrixValues[i] = matrix[i]
        }
        canvasMatrix.setValues(matrixValues)
        canvasMatrix.postScale(scale, scale)
        canvas?.apply {
            save()
            concat(canvasMatrix)
            drawBitmap(bitmap, srcRect, dstRect, null)
            restore()
        }
    }*/

    override fun drawColor(color: Color) {
        checkIsLocked()
        canvas?.drawColor(color.color)
    }

    override fun drawRectangle(color: Color, x: Int, y: Int, w: Int, h: Int) {
        checkIsLocked()
        paint.color = color.color
        canvas?.drawRect(1F * x, 1F * y, 1F * w, 1F * h, paint)
    }

    override val isCanvasReady: Boolean
        get() = surfaceHolder?.surface?.isValid ?: false

    override fun lockCanvas() {
        check(isCanvasReady) { "Canvas is not ready!" }
        check(isLocked.compareAndSet(false, true)) { "Canvas already locked!" }
        surfaceHolder?.apply {
            canvas = lockCanvas()
            height = surfaceFrame.height().pxToDp.toInt()
            width = surfaceFrame.width().pxToDp.toInt()
        }
    }

    override fun unlockAndPostCanvas() {
        check(isLocked.compareAndSet(true, false)) { "Canvas is not locked!" }
        canvas?.let { surfaceHolder?.unlockCanvasAndPost(it) }
        canvas = null
    }

    override fun release() {
        surfaceHolder = null
        bitmaps.clear()
        canvas = null
        height = 0
        width = 0
    }
}
