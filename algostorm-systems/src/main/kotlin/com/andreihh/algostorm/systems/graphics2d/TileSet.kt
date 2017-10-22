/*
 * Copyright (c) 2017  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.andreihh.algostorm.systems.graphics2d

import com.andreihh.algostorm.core.drivers.graphics2d.Bitmap
import com.andreihh.algostorm.core.drivers.io.Resource

/**
 * A tile set used for rendering.
 *
 * Tiles are indexed starting from `0`, increasing from left to right and then
 * Tiles are indexed starting from `0`, increasing from left to right and then
 * from top to bottom.
 *
 * @property name the name of this tile set
 * @property image the resource of the image represented by this tile set
 * @property tileWidth the width of each tile in this tile set in pixels
 * @property tileHeight the height of each tile in this tile set in pixels
 * @property margin the margin in pixels
 * @property spacing the spacing between adjacent tiles in pixels
 * @property columns the number of tiles per row
 * @property tileCount the number of tiles present in this tile set
 * @property animations the named animations in this tile set
 * @throws IllegalArgumentException if [tileWidth], [tileHeight], [columns] or
 * [tileCount] are not positive or if [margin] or [spacing] are negative or if
 * the specified offsets, tile sizes and tile count don't match the image
 * dimensions or if [animations] contains animation frames with tile ids not in
 * the range `0..tileCount - 1` or if [animations] contains empty frame
 * sequences
 */
data class TileSet(
        val name: String,
        val image: Image,
        val tileWidth: Int,
        val tileHeight: Int,
        val margin: Int = 0,
        val spacing: Int = 0,
        val animations: Map<String, List<Frame>> = emptyMap()
) {
    companion object {
        /** Whether this tile id is flipped horizontally. */
        val Int.isFlippedHorizontally: Boolean
            get() = and(0x40000000) != 0

        /** Whether this tile id is flipped vertically. */
        val Int.isFlippedVertically: Boolean
            get() = and(0x20000000) != 0

        /** Whether this tile id is flipped diagonally. */
        val Int.isFlippedDiagonally: Boolean
            get() = and(0x10000000) != 0

        /** Flips this tile id horizontally. */
        fun Int.flipHorizontally(): Int = xor(0x40000000)

        /** Flips this tile id vertically. */
        fun Int.flipVertically(): Int = xor(0x20000000)

        /** Flips this tile id diagonally. */
        fun Int.flipDiagonally(): Int = xor(0x10000000)

        /** Clears all flag bits. */
        fun Int.clearFlags(): Int = and(0x0FFFFFFF)
    }

    /**
     * Meta-data associated to an image.
     *
     * @property source the bitmap resource
     * @property width the width in pixels of this image
     * @property height the height in pixels of this image
     * @throws IllegalArgumentException if [width] or [height] are not positive
     */
    data class Image(
            val source: Resource<Bitmap>,
            val width: Int,
            val height: Int
    ) {
        init {
            require(width > 0) { "'$source': width '$width' must be positive!" }
            require(height > 0) {
                "'$source': height '$height' must be positive!"
            }
        }
    }

    /**
     * A frame within an animation.
     *
     * @property tileId the id of the tile used for this frame
     * @property duration the duration of this frame in milliseconds
     * @throws IllegalArgumentException if [tileId] is negative or if
     * [duration] is not positive
     */
    data class Frame(val tileId: Int, val duration: Int) {
        init {
            require(tileId >= 0) { "Tile id '$tileId' can't be negative!" }
            require(duration > 0) { "Duration '$duration' must be positive!" }
        }
    }

    /**
     * A rectangle projected over an image at the specified location.
     *
     * @property image the source image
     * @property x the horizontal coordinate in pixels of the top-left corner of
     * this viewport
     * @property y the vertical coordinate in pixels of the top-left corner of
     * this viewport (positive is down)
     * @property width the width in pixels of this viewport
     * @property height the height in pixels of this viewport
     * @throws IllegalArgumentException if [width] or [height] are negative or
     * if this viewport is not entirely contained within the image
     */
    data class Viewport(
            val image: Image,
            val x: Int,
            val y: Int,
            val width: Int,
            val height: Int
    ) {
        init {
            require(width >= 0) { "'$this': width '$width' can't be negative!" }
            require(height >= 0) {
                "'$this': height '$height' can't be negative!"
            }
            require(0 <= x && x + width <= image.width) {
                "'$this' exceeds horizontal image bounds '(0, ${image.width})'!"
            }
            require(0 <= y && y + height <= image.height) {
                "'$this' exceeds vertical image bounds '(0, ${image.height})'!"
            }
        }

        override fun toString(): String =
                "${image.source}[$x, $y, ${x + width}, ${y + height}]"
    }

    private fun checkAnimation(animationName: String, frames: List<Frame>) {
        require(frames.isNotEmpty()) {
            "'$name': animation '$animationName' can't have empty frame sequence!"
        }
        for ((tileId, _) in frames) {
            require(tileId in 0 until tileCount) {
                val range = "0..${tileCount - 1}"
                "'$name': tile id '$tileId' in animation '$animationName' must be in $range!"
            }
        }
    }

    private val columns: Int
        get() = (image.width - 2 * margin + spacing) / (tileWidth + spacing)

    private val rows: Int
        get() = (image.height - 2 * margin + spacing) / (tileHeight + spacing)

    val tileCount: Int
        get() = columns * rows

    init {
        require(tileWidth > 0) {
            "'$name': tile width '$tileWidth' must be positive!"
        }
        require(tileHeight > 0) {
            "'$name': tile height '$tileHeight' must be positive!"
        }
        require(margin >= 0) { "'$this': margin '$margin' can't be negative!" }
        require(spacing >= 0) {
            "'$name': spacing '$spacing' can't be negative!"
        }
        require(columns > 0) { "'$this': columns '$columns' must be positive!" }
        require(tileCount > 0) {
            "'$name': tile count '$tileCount' must be positive!"
        }
        val reqW = 2 * margin - spacing + columns * (tileWidth + spacing)
        val reqH = 2 * margin - spacing + rows * (tileHeight + spacing)
        val src = image.source
        require(reqW == image.width) {
            "'$name': image '$src' width '${image.width}' must be '$reqW'!"
        }
        require(reqH == image.height) {
            "'$name': image '$src' height '${image.height}' must be '$reqH'!"
        }
        for ((animationName, frames) in animations) {
            checkAnimation(animationName, frames)
        }
    }

    /**
     * Returns a viewport corresponding to the given tile id by applying the
     * appropriate margin and spacing offsets.
     *
     * @param tileId the id of the requested tile
     * @return the viewport associated to the requested tile
     * @throws IndexOutOfBoundsException if the given [tileId] is negative or if
     * it is greater than or equal to [tileCount]
     */
    fun getViewport(tileId: Int): Viewport {
        if (tileId !in 0 until tileCount) {
            throw IndexOutOfBoundsException(
                    "Tile id '$tileId' must be in 0..${tileCount - 1}!"
            )
        }
        return Viewport(
                image = image,
                x = margin + (tileId % columns) * (tileWidth + spacing),
                y = margin + (tileId / columns) * (tileHeight + spacing),
                width = tileWidth,
                height = tileHeight
        )
    }
}
