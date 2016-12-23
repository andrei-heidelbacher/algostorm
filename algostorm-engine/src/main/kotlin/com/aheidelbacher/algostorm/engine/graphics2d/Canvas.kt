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

package com.aheidelbacher.algostorm.engine.graphics2d

import com.aheidelbacher.algostorm.engine.driver.Resource

/** A canvas that allows primitive `draw` calls. */
interface Canvas {
    /**
     * Synchronously loads the given image `resource`, making it available to
     * future calls of [drawBitmap].
     *
     * @param resource the image resource which should be loaded
     */
    fun loadBitmap(resource: Resource): Unit

    /** The width of this canvas in pixels. */
    val width: Int

    /** The height of this canvas in pixels. */
    val height: Int

    /** Clears this canvas. */
    fun clear(): Unit

    /**
     * Draws the viewport projected on the indicated bitmap `resource` to this
     * canvas using the specified `matrix`.
     *
     * @param resource the image resource
     * @param x the horizontal coordinate in pixels of the top-left corner of
     * the bitmap viewport which should be rendered
     * @param y the vertical coordinate in pixels of the top-left corner of the
     * bitmap viewport which should be rendered (positive is down)
     * @param width the width in pixels of the bitmap viewport which should be
     * rendered
     * @param height the height in pixels of the bitmap viewport which should be
     * rendered
     * @param matrix the matrix that should be applied to the viewport when
     * rendering. Initially, the viewport rectangle is considered to have the
     * top-left corner overlap with the top-left corner of the canvas.
     * @throws IllegalArgumentException if the image `resource` was not loaded
     */
    fun drawBitmap(
            resource: Resource,
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            matrix: Matrix
    ): Unit

    /**
     * Draws the given colored rectangle to this canvas using the specified
     * `matrix`.
     *
     * @param color the color with which the rectangle should be filled
     * @param width the width in pixels of the rectangle
     * @param height the height in pixels of the rectangle
     * @param matrix the matrix that should be applied to the rectangle when
     * rendering. Initially, the rectangle is considered to have the top-left
     * corner overlap with the top-left corner of the canvas.
     */
    fun drawRectangle(
            color: Color,
            width: Int,
            height: Int,
            matrix: Matrix
    )

    /**
     * Fills the entire canvas with the given `color`.
     *
     * @param color the color which should fill the canvas
     */
    fun drawColor(color: Color) : Unit
}
