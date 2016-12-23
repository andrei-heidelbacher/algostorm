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

package com.aheidelbacher.algostorm.android

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.view.SurfaceHolder

import com.aheidelbacher.algostorm.engine.driver.Resource
import com.aheidelbacher.algostorm.engine.graphics2d.Color
import com.aheidelbacher.algostorm.engine.graphics2d.GraphicsDriver
import com.aheidelbacher.algostorm.engine.graphics2d.Matrix

import java.util.concurrent.atomic.AtomicBoolean

class AndroidGraphicsDriver(
        surfaceHolder: SurfaceHolder,
        private val scale: Float
) : GraphicsDriver {
    private var surfaceHolder: SurfaceHolder? = surfaceHolder
    private val bitmaps = hashMapOf<Resource, Bitmap>()
    private var canvas: Canvas? = null
    private val isLocked = AtomicBoolean(false)

    private val srcRect = Rect()
    private val dstRect = Rect()
    private val matrixValues = FloatArray(9) { 0F }
    private val canvasMatrix = android.graphics.Matrix()
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

    override fun loadBitmap(imageSource: Resource) {
        if (imageSource !in bitmaps) {
            imageSource.inputStream().use { src ->
                bitmaps[imageSource] = BitmapFactory.decodeStream(src)
            }
        }
    }

    /**
     * Retrieves the requested bitmap.
     *
     * @param imageSource the requested image resource
     * @return the requested bitmap, or `null` if it wasn't loaded
     */
    fun getBitmap(imageSource: Resource): Bitmap? = bitmaps[imageSource]

    override fun drawBitmap(
            imageSource: Resource,
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            matrix: Matrix
    ) {
        checkIsLocked()
        val bitmap = requireNotNull(bitmaps[imageSource]) {
            "Invalid bitmap $imageSource!"
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
    }

    override fun drawColor(color: Color) {
        checkIsLocked()
        canvas?.drawColor(color.color)
    }

    override fun drawRectangle(
            color: Color,
            width: Int,
            height: Int,
            matrix: Matrix
    ) {
        checkIsLocked()
        for (i in 0..8) {
            matrixValues[i] = matrix[i]
        }
        canvasMatrix.setValues(matrixValues)
        canvasMatrix.postScale(scale, scale)
        paint.color = color.color
        canvas?.apply {
            save()
            concat(canvasMatrix)
            drawRect(0F, 0F, width.toFloat(), height.toFloat(), paint)
            restore()
        }
    }

    override fun clear() {
        checkIsLocked()
        canvas?.drawColor(0, PorterDuff.Mode.CLEAR)
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
