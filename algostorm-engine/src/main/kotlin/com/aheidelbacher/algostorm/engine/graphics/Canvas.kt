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

package com.aheidelbacher.algostorm.engine.graphics

import java.io.FileNotFoundException

/**
 * A canvas that allows `draw` calls.
 *
 * Every change to the canvas should be performed after it was locked and the
 * changes should become visible after it was unlocked.
 *
 * After the canvas was locked, it must be unlocked.
 */
interface Canvas {
    /**
     * Loads the image at the given location.
     *
     * @param imageSource the location of the image
     * @throws FileNotFoundException if the given image doesn't exist
     */
    @Throws(FileNotFoundException::class)
    fun loadBitmap(imageSource: String): Unit

    /**
     * The width of this canvas in pixels.
     *
     * @throws IllegalStateException if the canvas is not locked
     */
    val width: Int

    /**
     * The height of this canvas in pixels.
     *
     * @throws IllegalStateException if the canvas is not locked
     */
    val height: Int

    /**
     * Clears the canvas.
     *
     * @throws IllegalStateException if the canvas is not locked
     */
    fun clear(): Unit

    /**
     * Draws the viewport projected on the indicated bitmap to the canvas using
     * the specified [matrix].
     *
     * @param image the location of the bitmap
     * @param x the x-axis coordinate in pixels of the top-left corner of the
     * bitmap viewport which should be rendered
     * @param y the y-axis coordinate in pixels of the top-left corner of the
     * bitmap viewport which should be rendered
     * @param width the width in pixels of the bitmap viewport which should be
     * rendered
     * @param height the height in pixels of the bitmap viewport which should be
     * rendered
     * @param matrix the matrix that should be applied to the viewport when
     * rendering. Initially, the viewport rectangle is considered to have the
     * top-left corner overlap with the top-left corner of the canvas.
     * @throws IllegalStateException if the canvas is not locked
     */
    fun drawBitmap(
            image: String,
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            matrix: Matrix
    ): Unit

    /**
     * Draws the viewport projected on the indicated bitmap to the canvas using
     * the specified [matrix].
     *
     * @param color the color with which the rectangle should be filled in
     * ARGB8888 format
     * @param width the width in pixels of the rectangle which should be
     * rendered
     * @param height the height in pixels of the rectangle which should be
     * rendered
     * @param matrix the matrix that should be applied to the rectangle when
     * rendering. Initially, the rectangle is considered to have the top-left
     * corner overlap with the top-left corner of the canvas.
     * @throws IllegalStateException if the canvas is not locked
     */
    fun drawRectangle(
            color: Int,
            width: Int,
            height: Int,
            matrix: Matrix
    )

    /**
     * Fills the entire canvas with the given color.
     *
     * @param color the color which should fill the canvas in ARGB8888 format
     * @throws IllegalStateException if the canvas is not locked
     */
    fun drawColor(color: Int) : Unit
}
