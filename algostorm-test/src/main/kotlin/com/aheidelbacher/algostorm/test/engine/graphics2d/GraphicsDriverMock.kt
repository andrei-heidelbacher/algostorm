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

package com.aheidelbacher.algostorm.test.engine.graphics2d

import com.aheidelbacher.algostorm.engine.driver.Resource
import com.aheidelbacher.algostorm.engine.graphics2d.Color
import com.aheidelbacher.algostorm.engine.graphics2d.GraphicsDriver
import com.aheidelbacher.algostorm.engine.graphics2d.Matrix

import java.util.LinkedList
import java.util.Queue

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GraphicsDriverMock(
        override val width: Int,
        override val height: Int
) : GraphicsDriver {
    private interface DrawCall {
        data class Bitmap(
                val image: Resource,
                val x: Int,
                val y: Int,
                val width: Int,
                val height: Int,
                val matrix: Matrix
        ) : DrawCall {
            override fun equals(other: Any?): Boolean = other is Bitmap &&
                    image == other.image && x == other.x && y == other.y &&
                    width == other.width && height == other.height &&
                    matrix.eq(other.matrix)

            override fun hashCode(): Int = super.hashCode()
        }

        data class ColorCanvas(val color: Color) : DrawCall

        data class Rectangle(
                val color: Color,
                val width: Int,
                val height: Int,
                val matrix: Matrix
        ) : DrawCall {
            override fun equals(other: Any?): Boolean = other is Rectangle &&
                    color == other.color &&
                    width == other.width && height == other.height &&
                    matrix.eq(other.matrix)

            override fun hashCode(): Int = super.hashCode()
        }

        object Clear : DrawCall
    }

    private val bitmaps = mutableSetOf<Resource>()
    private var isLocked = false
    private val queue: Queue<DrawCall> = LinkedList()

    override fun loadBitmap(resource: Resource) {
        bitmaps.add(resource)
    }

    override fun release() {
        bitmaps.clear()
    }

    override val isCanvasReady: Boolean
        get() = true

    override fun lockCanvas() {
        require(!isLocked) { "Canvas is already locked!" }
        isLocked = true
    }

    override fun clear() {
        require(isLocked) { "Canvas is not locked!" }
        queue.add(DrawCall.Clear)
    }

    override fun drawBitmap(
            resource: Resource,
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            matrix: Matrix
    ) {
        require(isLocked) { "Canvas is not locked!" }
        require(resource in bitmaps) { "Invalid bitmap $resource!" }
        val m = matrix.copy()
        queue.add(DrawCall.Bitmap(resource, x, y, width, height, m))
    }

    override fun drawColor(color: Color) {
        require(isLocked) { "Canvas is not locked!" }
        queue.add(DrawCall.ColorCanvas(color))
    }

    override fun drawRectangle(
            color: Color,
            width: Int,
            height: Int,
            matrix: Matrix
    ) {
        require(isLocked) { "Canvas is not locked!" }
        val m = matrix.copy()
        queue.add(DrawCall.Rectangle(color, width, height, m))
    }

    override fun unlockAndPostCanvas() {
        require(isLocked) { "Canvas is not locked!" }
        isLocked = false
    }

    fun assertLocked() {
        assertTrue(isLocked)
    }

    fun assertBitmap(
            image: Resource,
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            matrix: Matrix
    ) {
        assertEquals(
                expected = DrawCall.Bitmap(image, x, y, width, height, matrix),
                actual = queue.poll()
        )
    }

    fun assertColor(color: Color) {
        assertEquals(DrawCall.ColorCanvas(color), queue.poll())
    }

    fun assertRectangle(color: Color, width: Int, height: Int, matrix: Matrix) {
        assertEquals(
                expected = DrawCall.Rectangle(color, width, height, matrix),
                actual = queue.poll()
        )
    }

    fun assertClear() {
        assertEquals(DrawCall.Clear, queue.poll())
    }

    fun assertEmptyDrawQueue() {
        assertTrue(queue.isEmpty())
    }

    fun assertNotLocked() {
        assertFalse(isLocked)
    }
}
