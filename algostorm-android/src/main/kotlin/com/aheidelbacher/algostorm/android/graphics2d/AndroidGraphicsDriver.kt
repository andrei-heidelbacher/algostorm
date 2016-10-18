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

package com.aheidelbacher.algostorm.android.graphics2d

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.util.Log
import android.view.SurfaceHolder

import com.aheidelbacher.algostorm.engine.graphics2d.GraphicsDriver
import com.aheidelbacher.algostorm.engine.graphics2d.Matrix

/**
 * Android implementation of a graphics driver.
 *
 * All image resources are loaded relative to `/assets`.
 */
class AndroidGraphicsDriver(
        private val surfaceHolder: SurfaceHolder,
        private val density: Float,
        private val assetManager: AssetManager,
        @Volatile var zoom: Float
) : GraphicsDriver {
    private var canvas: Canvas? = null
    private var isLocked = false
    private val bitmaps = hashMapOf<String, Bitmap>()
    private val srcRect = Rect()
    private val dstRect = Rect()
    private val matrix = android.graphics.Matrix()
    private var lockedZoom: Float = zoom
    private val paint = Paint()

    private val Int.pxToDp: Float
        get() = toFloat() / density

    override fun loadBitmap(imageSource: String) {
        val stream = requireNotNull(assetManager.open(imageSource.drop(1))) {
            "Image $imageSource doesn't exist!"
        }
        bitmaps[imageSource] = BitmapFactory.decodeStream(stream)
        Log.i("CANVAS", imageSource)
    }

    override fun release() {
        bitmaps.clear()
    }

    private fun checkIsLocked() {
        check(isLocked) { "Canvas not locked!" }
    }

    private fun checkIsNotLocked() {
        check(!isLocked) { "Canvas is already locked!" }
    }

    override val width: Int
        get() {
            val frameWidth = surfaceHolder.surfaceFrame.width()
            return (frameWidth.pxToDp / lockedZoom).toInt()
        }

    override val height: Int
        get() {
            val frameHeight = surfaceHolder.surfaceFrame.height()
            return (frameHeight.pxToDp / lockedZoom).toInt()
        }

    override fun lockCanvas() {
        checkIsNotLocked()
        isLocked = true
        lockedZoom = zoom
        if (surfaceHolder.surface.isValid) {
            canvas = surfaceHolder.lockCanvas()
        }
    }

    override fun clear() {
        checkIsLocked()
        canvas?.drawColor(0, PorterDuff.Mode.CLEAR)
    }

    override fun drawBitmap(
            image: String,
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            matrix: Matrix
    ) {
        checkIsLocked()
        val bitmap = requireNotNull(bitmaps[image]) {
            "Invalid bitmap $image!"
        }
        srcRect.set(x, y, x + width, y + height)
        dstRect.set(0, 0, width, height)
        this.matrix.apply {
            setValues(matrix.getRawValues())
            postScale(density * lockedZoom, density * lockedZoom)
        }
        canvas?.apply {
            save()
            concat(this@AndroidGraphicsDriver.matrix)
            drawBitmap(bitmap, srcRect, dstRect, null)
            restore()
        }
    }

    override fun drawColor(color: Int) {
        checkIsLocked()
        canvas?.drawColor(color)
    }

    override fun drawRectangle(
            color: Int,
            width: Int,
            height: Int,
            matrix: Matrix
    ) {
        checkIsLocked()
        this.matrix.apply {
            setValues(matrix.getRawValues())
            postScale(density * lockedZoom, density * lockedZoom)
        }
        paint.color = color or (255 shl 24)
        canvas?.apply {
            save()
            concat(this@AndroidGraphicsDriver.matrix)
            drawRect(0F, 0F, width.toFloat(), height.toFloat(), paint)
            restore()
        }
    }

    override fun unlockAndPostCanvas() {
        checkIsLocked()
        if (canvas != null) {
            surfaceHolder.unlockCanvasAndPost(canvas)
            canvas = null
        }
        isLocked = false
    }
}
