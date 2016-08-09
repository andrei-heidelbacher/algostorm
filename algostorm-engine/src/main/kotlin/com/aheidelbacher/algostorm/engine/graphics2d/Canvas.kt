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

import com.aheidelbacher.algostorm.engine.state.TileSet.Viewport

import java.io.InputStream

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
     * Loads the image from the given [inputStream] and saves it under the
     * [image] name.
     *
     * @param image the identifier of the image
     * @param inputStream the stream from which the image should be read
     */
    fun loadBitmap(image: String, inputStream: InputStream): Unit

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
     * Locks this canvas and allows editing the canvas content.
     *
     * @throws IllegalStateException if the canvas is already locked
     */
    fun lock(): Unit

    /**
     * Clears the canvas.
     *
     * @throws IllegalStateException if the canvas is not locked
     */
    fun clear(): Unit

    /**
     * Draws the viewport projected on the indicated bitmap. The drawing
     * coordinates are given relative to this canvas. If necessary, it will
     * scale the viewport to the destination area.
     *
     * @param viewport the viewport which should be rendered
     * @param flipHorizontally whether the image should be flipped horizontally
     * before rendering
     * @param flipVertically whether the image should be flipped vertically
     * before rendering
     * @param flipDiagonally whether the image should be flipped diagonally
     * before rendering
     * @param opacity the opacity of the image. Should be between `0` and `1`.
     * @param x the x-axis coordinate of the top-left corner of the rendered
     * image in pixels
     * @param y the y-axis coordinate of the top-left corner of the rendered
     * image in pixels
     * @param width the width of the rendered image in pixels
     * @param height the height of the rendered image in pixels
     * @param rotation the rotation of the image around the top-left corner in
     * clock-wise degrees
     * @throws IllegalStateException if the canvas is not locked
     */
    fun drawBitmap(
            viewport: Viewport,
            flipHorizontally: Boolean,
            flipVertically: Boolean,
            flipDiagonally: Boolean,
            opacity: Float,
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            rotation: Float
    ): Unit

    /**
     * Draws the viewport projected on the indicated bitmap to the canvas using
     * the specified [matrix].
     *
     * @param viewport the viewport which should be rendered
     * @param matrix the matrix that should be applied to the viewport when
     * rendering. Initially, the viewport rectangle is considered to have the
     * top-left corner overlap with the top-left corner of the canvas.
     * @param opacity the opacity of the image. Should be between `0` and `1`.
     * @throws IllegalStateException if the canvas is not locked
     */
    fun drawBitmap(viewport: Viewport, matrix: Matrix, opacity: Float): Unit

    /**
     * Unlocks this canvas and posts all the changes made since the canvas was
     * locked.
     *
     * @throws IllegalStateException if the canvas is not locked
     */
    fun unlockAndPost(): Unit
}