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

package com.aheidelbacher.algostorm.test.engine.graphics2d

import com.aheidelbacher.algostorm.engine.graphics2d.Canvas
import com.aheidelbacher.algostorm.engine.graphics2d.Matrix
import com.aheidelbacher.algostorm.test.engine.graphics2d.MatrixUtil.eq

import java.util.LinkedList
import java.util.Queue

class CanvasMock : Canvas {
    private interface DrawCall {
        data class Bitmap(
                val image: String,
                val x: Int,
                val y: Int,
                val width: Int,
                val height: Int,
                val matrix: Matrix,
                val opacity: Float
        ) : DrawCall {
            override fun equals(other: Any?): Boolean = other is Bitmap &&
                    image == other.image && x == other.x && y == other.y &&
                    width == other.width && height == other.height &&
                    matrix.eq(other.matrix) && opacity.eq(other.opacity)

            override fun hashCode(): Int = super.hashCode()
        }

        data class Color(val color: Int) : DrawCall

        data class Rectangle(
                val color: Int,
                val width: Int,
                val height: Int,
                val matrix: Matrix,
                val opacity: Float
        ) : DrawCall {
            override fun equals(other: Any?): Boolean = other is Rectangle &&
                    color == other.color &&
                    width == other.width && height == other.height &&
                    matrix.eq(other.matrix) && opacity.eq(other.opacity)

            override fun hashCode(): Int = super.hashCode()
        }

        object Clear : DrawCall
    }

    private val bitmaps = mutableSetOf<String>()
    private var isLocked = false
    private val queue: Queue<DrawCall> = LinkedList()

    override val width: Int
        get() {
            require(isLocked) { "Canvas is not locked!" }
            return 320
        }

    override val height: Int
        get() {
            require(isLocked) { "Canvas is not locked!" }
            return 230
        }

    override fun loadBitmap(image: String) {
        bitmaps.add(image)
    }

    override fun unloadBitmaps() {
        bitmaps.clear()
    }

    override fun lock() {
        require(!isLocked) { "Canvas is already locked!" }
        isLocked = true
    }

    override fun clear() {
        require(isLocked) { "Canvas is not locked!" }
        queue.add(DrawCall.Clear)
    }

    override fun drawBitmap(
            image: String,
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            matrix: Matrix,
            opacity: Float
    ) {
        require(isLocked) { "Canvas is not locked!" }
        require(image in bitmaps) { "Invalid bitmap $image!" }
        val m = matrix.copy()
        queue.add(DrawCall.Bitmap(image, x, y, width, height, m, opacity))
    }

    override fun drawColor(color: Int) {
        require(isLocked) { "Canvas is not locked!" }
        queue.add(DrawCall.Color(color))
    }

    override fun drawRectangle(
            color: Int,
            width: Int,
            height: Int,
            matrix: Matrix,
            opacity: Float
    ) {
        require(isLocked) { "Canvas is not locked!" }
        val m = matrix.copy()
        queue.add(DrawCall.Rectangle(color, width, height, m, opacity))
    }

    override fun unlockAndPost() {
        require(isLocked) { "Canvas is not locked!" }
        isLocked = false
    }

    fun verifyBitmap(
            image: String,
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            matrix: Matrix,
            opacity: Float
    ) {
        val actualCall = queue.poll()
        val expectedCall =
                DrawCall.Bitmap(image, x, y, width, height, matrix, opacity)
        check(actualCall == expectedCall) {
            "The bitmap was not drawn!\n" +
                    "Expected: $expectedCall\n" +
                    "Actual: $actualCall"
        }
    }

    fun verifyColor(color: Int) {
        val actualCall = queue.poll()
        val expectedCall = DrawCall.Color(color)
        check(actualCall == expectedCall) {
            "The color was not drawn!\n" +
                    "Expected: $expectedCall\n" +
                    "Actual: $actualCall"
        }
    }

    fun verifyRectangle(
            color: Int,
            width: Int,
            height: Int,
            matrix: Matrix,
            opacity: Float
    ) {
        val actualCall = queue.poll()
        val expectedCall =
                DrawCall.Rectangle(color, width, height, matrix, opacity)
        check(actualCall == expectedCall) {
            "The rectangle was not drawn!\n" +
                    "Expected: $expectedCall\n" +
                    "Actual: $actualCall"
        }
    }

    fun verifyClear() {
        val actualCall = queue.poll()
        val expectedCall = DrawCall.Clear
        check(actualCall == expectedCall) {
            "The canvas was not cleared!\n" +
                    "Expected: $expectedCall\n" +
                    "Actual: $actualCall"
        }
    }

    fun verifyEmptyDrawQueue() {
        check(queue.isEmpty()) { "There were more draw calls issued!!\n" }
    }
}
