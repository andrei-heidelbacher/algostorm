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

package com.aheidelbacher.algostorm.engine.graphics2d

import java.io.FileNotFoundException

/** A canvas that allows primitive `draw` calls. */
interface Canvas {
    /**
     * Synchronously loads the image resource located at the given path.
     *
     * Paths are relative to a specialized location of image resources.
     *
     * @param imageSource the path of the image resource which should be loaded
     * @throws FileNotFoundException if the given resource doesn't exist
     */
    @Throws(FileNotFoundException::class)
    fun loadBitmap(imageSource: String): Unit

    /** The width of this canvas in pixels. */
    val width: Int

    /** The height of this canvas in pixels. */
    val height: Int

    /** Clears this canvas. */
    fun clear(): Unit

    /**
     * Draws the viewport projected on the indicated bitmap to this canvas using
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
     * Draws the viewport projected on the indicated bitmap to this canvas using
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
     */
    fun drawColor(color: Int) : Unit
}
