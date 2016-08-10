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
     * Loads the image at the given location.
     *
     * @param image the location of the image
     * @throws IllegalArgumentException if the given image doesn't exist
     */
    fun loadBitmap(image: String): Unit

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
