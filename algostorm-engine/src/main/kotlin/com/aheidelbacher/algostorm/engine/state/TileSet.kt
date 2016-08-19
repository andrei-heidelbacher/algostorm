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
 * @property columns the number of tiles per row
 * @property tileCount the number of tiles present in this tile set
 * @property tileOffset the rendering offset which should be applied
 * @property properties the properties of this tile set
 * @property tileProperties properties of individual tiles
 * @property tiles meta-data associated to particular tiles of this tile set
 * @throws IllegalArgumentException if [tileWidth], [tileHeight], [imageWidth],
 * [imageHeight], [columns] or [tileCount] are not positive or if [margin] or
 * [spacing] are negative or if the specified offsets, tile sizes and tile count
 * exceed the image dimensions
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
        val columns: Int,
        val tileCount: Int,
        val tileOffset: TileOffset = TileOffset(0, 0),
        val properties: Map<String, Any> = emptyMap(),
        private val tileProperties: Map<Int, Map<String, Any>> = emptyMap(),
        private val tiles: Map<Int, Tile> = emptyMap()
) {
    /**
     * Indicates an offset which should be applied when rendering any tile from
     * this tile set.
     *
     * @property x the x-axis offset in pixels
     * @property y the y-axis offset in pixels
     */
    data class TileOffset(val x: Int, val y: Int)

    /**
     * An object containing meta-data associated to this tile.
     *
     * @property animation a list of frames representing an animation. Must be
     * `null` (indicating no animation) or must contain at least two frames.
     * @throws IllegalArgumentException if [animation] is not `null` and
     * contains less than two frames
     */
    data class Tile(val animation: List<Frame>? = null) {
        companion object {
            /**
             * Whether this global tile id is flipped horizontally.
             */
            val Long.isFlippedHorizontally: Boolean
                get() = and(0x80000000) != 0L

            /**
             * Whether this global tile id is flipped vertically.
             */
            val Long.isFlippedVertically: Boolean
                get() = and(0x40000000) != 0L

            /**
             * Whether this global tile id is flipped diagonally.
             */
            val Long.isFlippedDiagonally: Boolean
                get() = and(0x20000000) != 0L

            /**
             * Flips this global tile id horizontally.
             */
            fun Long.flipHorizontally(): Long = xor(0x80000000)

            /**
             * Flips this global tile id vertically.
             */
            fun Long.flipVertically(): Long = xor(0x40000000)

            /**
             * Flips this global tile id diagonally.
             */
            fun Long.flipDiagonally(): Long = xor(0x20000000)

            /**
             * Clears all flag bits.
             */
            fun Long.clearFlags(): Int = and(0x1FFFFFFF).toInt()
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

    @Transient private val tilesArray = Array(tileCount) { tiles[it] ?: Tile() }
    @Transient private val tilePropertiesArray = Array(tileCount) {
        tileProperties[it] ?: emptyMap()
    }
    @Transient private val viewports = Array(tileCount) {
        Viewport(
                image = image,
                x = margin + (it % columns) * (tileWidth + spacing),
                y = margin + (it / columns) * (tileHeight + spacing),
                width = tileWidth,
                height = tileHeight
        )
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
        require(columns > 0) { "$name columns $columns isn't positive!" }
        require(tileCount > 0) { "$name tile count $tileCount isn't positive!" }
        require(tileCount % columns == 0) {
            "$name tile count $tileCount must be divisible by columns $columns!"
        }
        val rows = tileCount / columns
        val reqWidth = 2 * margin - spacing + columns * (tileWidth + spacing)
        val reqHeight = 2 * margin - spacing + rows * (tileHeight + spacing)
        require(reqWidth <= imageWidth) {
            "$name image width $imageWidth must be at least $reqWidth!"
        }
        require(reqHeight <= imageHeight) {
            "$name image height $imageHeight must be at least $reqHeight!"
        }
    }

    /**
     * Returns the [Tile] with the given tile id.
     *
     * @param tileId the id of the requested tile
     * @return the requested tile data
     * @throws IndexOutOfBoundsException if the given [tileId] is negative or if
     * it is greater than or equal to [tileCount]
     */
    fun getTile(tileId: Int): Tile = tilesArray[tileId]

    /**
     * Returns the properties associated to the given tile id.
     *
     * @param tileId the id of the requested tile
     * @return the properties of the requested tile
     * @throws IndexOutOfBoundsException if the given [tileId] is negative or if
     * it is greater than or equal to [tileCount]
     */
    fun getTileProperties(tileId: Int): Map<String, Any> =
            tilePropertiesArray[tileId]

    /**
     * Returns a viewport corresponding to the given tile id, by applying the
     * appropriate margin and spacing offsets.
     *
     * @param tileId the id of the requested tile
     * @return the viewport associated to the requested tile
     * @throws IndexOutOfBoundsException if the given [tileId] is negative or if
     * it is greater than or equal to [tileCount]
     */
    fun getViewport(tileId: Int): Viewport = viewports[tileId]

    override fun equals(other: Any?): Boolean =
            other is TileSet && name == other.name

    override fun hashCode(): Int = name.hashCode()
}
