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

package com.aheidelbacher.algostorm.state

import com.aheidelbacher.algostorm.state.Layer.ObjectGroup

/**
 * A tile set used for rendering.
 *
 * Tiles are indexed starting from `0`, increasing from left to right and then
 * from top to bottom.
 *
 * @property name the name of this tile set
 * @property tileWidth the width of each tile in this tile set in pixels
 * @property tileHeight the height of each tile in this tile set in pixels
 * @property image the path to the image represented by this tile set
 * @property margin the margin in pixels
 * @property spacing the spacing between adjacent tiles in pixels
 * @property columns the number of tiles per row
 * @property tileCount the number of tiles present in this tile set
 * @property tileOffsetX the x-axis rendering offset in pixels which should be
 * applied when rendering tiles from this tile set
 * @property tileOffsetY the y-axis rendering offset in pixels which should be
 * applied when rendering tiles from this tile set
 * @property tiles meta-data associated to particular tiles of this tile set
 * @throws IllegalArgumentException if [tileWidth], [tileHeight], [columns] or
 * [tileCount] are not positive or if [margin] or [spacing] are negative or if
 * the specified offsets, tile sizes and tile count exceed the image dimensions
 */
data class TileSet internal constructor(
        val name: String,
        val tileWidth: Int,
        val tileHeight: Int,
        val image: Image,
        val margin: Int,
        val spacing: Int,
        val columns: Int,
        val tileCount: Int,
        val tileOffsetX: Int,
        val tileOffsetY: Int,
        val tiles: Map<Int, Tile>
) {
    /**
     * Meta-data associated to a tile.
     *
     * @property animation a list of frames representing an animation. Must be
     * `null` (indicating no animation) or must contain at least two frames.
     * @property objectGroup an object group containing tile collision
     * information
     * @throws IllegalArgumentException if [animation] is not `null` and
     * contains less than two frames or if [objectGroup] is not `null` and
     * contains an object with a non-zero `gid`
     */
    data class Tile internal constructor(
            val animation: List<Frame>?,
            val objectGroup: ObjectGroup?,
            override val properties: Map<String, Property>
    ) : Properties {
        companion object {
            /** Whether this global tile id is flipped horizontally. */
            val Long.isFlippedHorizontally: Boolean
                get() = and(0x80000000) != 0L

            /** Whether this global tile id is flipped vertically. */
            val Long.isFlippedVertically: Boolean
                get() = and(0x40000000) != 0L

            /** Whether this global tile id is flipped diagonally. */
            val Long.isFlippedDiagonally: Boolean
                get() = and(0x20000000) != 0L

            /** Flips this global tile id horizontally. */
            fun Long.flipHorizontally(): Long = xor(0x80000000)

            /** Flips this global tile id vertically. */
            fun Long.flipVertically(): Long = xor(0x40000000)

            /** Flips this global tile id diagonally. */
            fun Long.flipDiagonally(): Long = xor(0x20000000)

            /** Clears all flag bits. */
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
                require(tileId >= 0) { "$this tile id can't be negative!" }
                require(duration > 0) { "$this duration must be positive!" }
            }
        }

        init {
            require(animation?.isNotEmpty() ?: true) {
                "$this animation can't have empty frame sequence!"
            }
            require(objectGroup?.objectSet?.all { it.gid == 0L } ?: true) {
                "$this object group can't contain tile objects!"
            }
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
            require(width >= 0) { "$this width can't be negative!" }
            require(height >= 0) { "$this height can't be negative!" }
            require(0 <= x && x + width <= image.width) {
                "$this horizontally exceeds the image bounds!"
            }
            require(0 <= y && y + height <= image.height) {
                "$this vertically exceeds the image bounds!"
            }
        }
    }

    @Transient private val tilesArray = Array(tileCount) {
        tiles[it] ?: Tile(null, null, emptyMap())
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
        require(tileWidth > 0) { "$this tile width must be positive!" }
        require(tileHeight > 0) { "$this tile height must be positive!" }
        require(margin >= 0) { "$this margin can't be negative!" }
        require(spacing >= 0) { "$this spacing can't be negative!" }
        require(columns > 0) { "$this columns must be positive!" }
        require(tileCount > 0) { "$this tile count must be positive!" }
        require(tileCount % columns == 0) {
            "$this tile count must be divisible by columns!"
        }
        val rows = tileCount / columns
        val reqWidth = 2 * margin - spacing + columns * (tileWidth + spacing)
        val reqHeight = 2 * margin - spacing + rows * (tileHeight + spacing)
        require(reqWidth <= image.width) {
            "$this image width must be at least $reqWidth!"
        }
        require(reqHeight <= image.height) {
            "$this image height must be at least $reqHeight!"
        }
        require(tiles.all {
            it.key == it.value.animation?.first()?.tileId ?: it.key
        }) {
            "$this first animation frame tile id must equal containing tile id!"
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
     * Returns a viewport corresponding to the given tile id, by applying the
     * appropriate margin and spacing offsets.
     *
     * @param tileId the id of the requested tile
     * @return the viewport associated to the requested tile
     * @throws IndexOutOfBoundsException if the given [tileId] is negative or if
     * it is greater than or equal to [tileCount]
     */
    fun getViewport(tileId: Int): Viewport = viewports[tileId]
}
