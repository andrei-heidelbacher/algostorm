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

package com.aheidelbacher.algostorm.core.engine.graphics2d

import com.aheidelbacher.algostorm.core.engine.driver.Resource

/** A canvas that allows primitive `draw` calls. */
interface Canvas {
    /**
     * Synchronously loads the given image `resource`, making it available to
     * future calls of [drawBitmap].
     *
     * If the same resource is loaded multiple times, this method has no effect.
     *
     * @param resource the image resource which should be loaded
     */
    fun loadBitmap(resource: Resource): Unit

    /** The width of this canvas in pixels. */
    val width: Int

    /** The height of this canvas in pixels. */
    val height: Int

    fun save(): Unit

    fun translate(dx: Float, dy: Float): Unit

    fun scale(sx: Float, sy: Float): Unit

    fun rotate(degrees: Float): Unit

    fun restore(): Unit

    /**
     * Draws the given viewport from the bitmap `resource` to the specified
     * canvas location.
     *
     * @param resource the image resource
     * @param sx the horizontal coordinate in pixels of the top-left corner of
     * the bitmap viewport
     * @param sy the vertical coordinate in pixels of the top-left corner of the
     * bitmap viewport which should be rendered (positive is down)
     * @param sw the width in pixels of the bitmap viewport
     * @param sh the height in pixels of the bitmap viewport
     * @param dx the horizontal coordinate in pixels of the top-left corner of
     * the destination location
     * @param dy the vertical coordinate in pixels of the top-left corner of the
     * destination location
     * @param dw the width in pixels of the destination location
     * @param dh the height in pixels of the destination location
     * @throws IllegalArgumentException if the image `resource` was not loaded
     */
    fun drawBitmap(
            resource: Resource,
            sx: Int,
            sy: Int,
            sw: Int,
            sh: Int,
            dx: Int,
            dy: Int,
            dw: Int,
            dh: Int
    ): Unit

    /**
     * Draws the given colored rectangle to this canvas.
     *
     * @param color the color with which the rectangle should be filled
     * @param x the horizontal coordinate in pixels of the top-left corner of
     * the rectangle
     * @param y the vertical coordinate in pixels of the top-left corner of the
     * rectangle (positive is down)
     * @param w the width in pixels of the rectangle
     * @param h the height in pixels of the rectangle
     */
    fun drawRectangle(color: Color, x: Int, y: Int, w: Int, h: Int): Unit

    /**
     * Fills the entire canvas with the given `color`.
     *
     * @param color the color which should fill the canvas
     */
    fun drawColor(color: Color) : Unit
}
