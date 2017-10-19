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

package com.aheidelbacher.algostorm.core.drivers.client.graphics2d

import com.aheidelbacher.algostorm.core.drivers.io.Resource

/** A canvas that allows primitive `draw` calls. */
interface Canvas {
    /** The width of this canvas in pixels. */
    val width: Int

    /** The height of this canvas in pixels. */
    val height: Int

    /**
     * Saves the current transformation on a private stack.
     *
     * Initially, the stack is empty.
     */
    fun save()

    /**
     * Translates the current transform by `dx, dy`
     *
     * @param dx value to translate horizontally
     * @param dy value to translate vertically (positive is down)
     */
    fun translate(dx: Float, dy: Float)

    /**
     * Scales the current transform by `sx, sy`
     *
     * @param sx value to scale horizontally
     * @param sy value to scale vertically
     */
    fun scale(sx: Float, sy: Float)

    /**
     * Rotates the current transform by `degrees`.
     *
     * @param degrees value in degrees to rotate
     */
    fun rotate(degrees: Float)

    /**
     * Pops the state off the stack, setting the current transform to the value
     * at the time it was pushed on the stack.
     *
     * @throws IllegalStateException if the stack is empty
     */
    fun restore()

    /**
     * Draws the given viewport from the `bitmap` to the specified canvas
     * location.
     *
     * @param bitmap the bitmap resource
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
     * by the graphics driver
     */
    fun drawBitmap(
            bitmap: Resource<Bitmap>,
            sx: Int,
            sy: Int,
            sw: Int,
            sh: Int,
            dx: Int,
            dy: Int,
            dw: Int,
            dh: Int
    )

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
    fun drawRectangle(color: Color, x: Int, y: Int, w: Int, h: Int)

    /**
     * Fills the entire canvas with the given `color`.
     *
     * @param color the color which should fill the canvas
     */
    fun drawColor(color: Color)
}
