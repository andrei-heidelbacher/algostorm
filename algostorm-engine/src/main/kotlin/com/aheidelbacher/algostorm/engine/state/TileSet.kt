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

package com.aheidelbacher.algostorm.engine.state

import kotlin.collections.Map

/**
 * A tile set used for rendering. Tiles are indexed starting from `0`,
 * increasing from left to right and then from top to bottom.
 *
 * Two tile sets are equal if and only if they have the same [name].
 *
 * @property name the name of this tile set
 * @property tileWidth the width of each tile in this tile set in pixels
 * @property tileHeight the height of each tile in this tile set in pixels
 * @property image the path to the image represented by this tile set
 * @property imageWidth the width of the image in pixels
 * @property imageHeight the height of the image in pixels
 * @property margin the margin in pixels
 * @property spacing the spacing between adjacent tiles in pixels
 * @property tileCount the number of tiles present in this tile set
 * @property properties the properties of this tile set
 * @property tiles meta-data associated to particular tiles of this tile set
 * @throws IllegalArgumentException if [tileWidth], [tileHeight], [imageWidth],
 * [imageHeight] or [tileCount] are not positive or if [margin] or [spacing]
 * are negative or if the specified offsets and sizes don't match the image
 * sizes
 */
class TileSet(
        val name: String,
        val tileWidth: Int,
        val tileHeight: Int,
        val image: String,
        val imageWidth: Int,
        val imageHeight: Int,
        val margin: Int,
        val spacing: Int,
        val tileCount: Int,
        val properties: Map<String, Any> = emptyMap(),
        val tiles: Map<Int, Tile> = emptyMap()
) {
    /**
     * An object containing meta-data associated to this tile.
     *
     * @property animation a list of frames representing an animation. Must be
     * `null` (indicating no animation) or must contain at least two frames.
     * @property properties properties associated to this tile
     * @throws IllegalArgumentException if [animation] is not `null` and
     * contains less than two frames
     */
    class Tile(
            val animation: List<Frame>? = null,
            val properties: Map<String, Any> = emptyMap()
    ) {
        companion object {
            /**
             * Whether this global tile id is flipped horizontally.
             */
            val Int.isFlippedHorizontally: Boolean
                get() = and(0x40000000) != 0

            /**
             * Whether this global tile id is flipped vertically.
             */
            val Int.isFlippedVertically: Boolean
                get() = and(0x20000000) != 0

            /**
             * Whether this global tile id is flipped diagonally.
             */
            val Int.isFlippedDiagonally: Boolean
                get() = and(0x10000000) != 0

            /**
             * Flips this global tile id horizontally.
             */
            fun Int.flipHorizontally(): Int = xor(0x4000000)

            /**
             * Flips this global tile id vertically.
             */
            fun Int.flipVertically(): Int = xor(0x20000000)

            /**
             * Flips this global tile id diagonally.
             */
            fun Int.flipDiagonally(): Int = xor(0x10000000)
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
                require(tileId >= 0) {
                    "Frame tile id $tileId can't be negative!"
                }
                require(duration > 0) {
                    "Frame duration $duration must be positive!"
                }
            }
        }

        init {
            require(animation?.isNotEmpty() ?: true) {
                "Animation can't have empty frame sequence!"
            }
        }
    }

    /**
     * A rectangle projected over the image at the specified location.
     *
     * @property image the location of the image
     * @property x the x-axis coordinate of the top-left corner of this viewport
     * in pixels
     * @property y the y-axis coordinate of the top-left corner of this viewport
     * in pixels
     * @property width the width of this viewport in pixels
     * @property height the height of this viewport in pixels
     * @throws IllegalArgumentException if [width] or [height] are negative
     */
    data class Viewport(
            val image: String,
            val x: Int,
            val y: Int,
            val width: Int,
            val height: Int
    ) {
        init {
            require(width >= 0 && height >= 0) {
                "Viewport sizes ($width, $height) can't be negative!"
            }
        }
    }

    init {
        require(tileWidth > 0 && tileHeight > 0) {
            "$name tile sizes ($tileWidth, $tileHeight) must be positive!"
        }
        require(imageWidth > 0 && imageHeight > 0) {
            "$name image sizes ($imageWidth, $imageHeight) must be positive!"
        }
        require(margin >= 0) { "$name margin $margin can't be negative!" }
        require(spacing >= 0) { "$name spacing $spacing can't be negative!" }
        require(tileCount > 0) { "$name tile count $tileCount isn't positive!" }
        val widthOffset =
                (imageWidth - 2 * margin + spacing) % (tileWidth + spacing)
        val heightOffset =
                (imageHeight - 2 * margin + spacing) % (tileHeight + spacing)
        require(widthOffset == 0 && heightOffset == 0) {
            "$name image sizes don't match margin, spacing and tile sizes!"
        }
    }

    /**
     * Returns a viewport corresponding to the given tile id, by applying the
     * appropriate margin and spacing offsets.
     *
     * @param tileId the id of the requested tile
     * @return the viewport associated to the requested tile
     * @throws IllegalArgumentException if the given [tileId] is negative or if
     * it is greater than or equal to [tileCount]
     */
    fun getViewport(tileId: Int): Viewport {
        require(tileId in 0..tileCount - 1)
        val columns = (imageWidth - 2 * margin + spacing) /
                (tileWidth + spacing)
        val row = tileId / columns
        val column = tileId % columns
        return Viewport(
                image = image,
                x = margin + column * (tileWidth + spacing),
                y = margin + row * (tileHeight + spacing),
                width = tileWidth,
                height = tileHeight
        )
    }

    override fun equals(other: Any?): Boolean =
            other is TileSet && name == other.name

    override fun hashCode(): Int = name.hashCode()
}