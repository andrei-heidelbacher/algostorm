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

package com.aheidelbacher.algostorm.data

import com.aheidelbacher.algostorm.data.TileSet.Builder
import com.aheidelbacher.algostorm.engine.driver.Resource
import com.aheidelbacher.algostorm.data.TileSet.Tile.Frame

import kotlin.properties.Delegates

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
 * @property tiles meta-data associated to particular tiles of this tile set
 * @throws IllegalArgumentException if [tileWidth], [tileHeight], [columns] or
 * [tileCount] are not positive or if [margin] or [spacing] are negative or if
 * the specified offsets, tile sizes and tile count exceed the image dimensions
 * or if [tiles] has duplicate tile ids or if [tiles] contains tile ids or
 * animation frames with tile ids not in the range `0..tileCount - 1`
 */
data class TileSet private constructor(
        val name: String,
        val tileWidth: Int,
        val tileHeight: Int,
        val image: Image,
        val margin: Int,
        val spacing: Int,
        val columns: Int,
        val tileCount: Int,
        val tiles: Set<Tile>
) {
    class Builder {
        companion object {
            fun tileSet(init: Builder.() -> Unit): TileSet =
                    Builder().apply(init).build()
        }

        lateinit var name: String
        var tileWidth: Int by Delegates.notNull<Int>()
        var tileHeight: Int by Delegates.notNull<Int>()
        private lateinit var image: Image
        var margin: Int = 0
        var spacing: Int = 0
        private val tiles = hashSetOf<Tile>()

        fun image(resource: Resource, width: Int, height: Int) {
            image = Image(resource, width, height)
        }

        operator fun Tile.unaryPlus() {
            tiles.add(this)
        }

        fun build(): TileSet = TileSet(
                name = name,
                tileWidth = tileWidth,
                tileHeight = tileHeight,
                image = image,
                margin = margin,
                spacing = spacing,
                columns = image.width / tileWidth,
                tileCount = image.width / tileWidth * image.height / tileWidth,
                tiles = tiles
        )
    }

    /**
     * Meta-data associated to an image.
     *
     * @property resource the image resource
     * @property width the width in pixels of this image
     * @property height the height in pixels of this image
     * @throws IllegalArgumentException if [width] or [height] are not positive
     */
    data class Image(val resource: Resource, val width: Int, val height: Int) {
        init {
            require(width > 0) { "$this width must be positive!" }
            require(height > 0) { "$this height must be positive!" }
        }
    }

    /**
     * Meta-data associated to a tile.
     *
     * @property id the local id of this tile
     * @property animation a list of frames representing an animation. Must be
     * either `null` (indicating no animation) or non-empty.
     * @throws IllegalArgumentException if [animation] is not `null` and empty
     * or the tile id of the first animation frame differs from [id]
     */
    data class Tile internal constructor(
            val id: Int,
            val animation: List<Frame>?
    ) {
        companion object {
            /** Whether this global tile id is flipped horizontally. */
            val Int.isFlippedHorizontally: Boolean
                get() = and(0x40000000) != 0

            /** Whether this global tile id is flipped vertically. */
            val Int.isFlippedVertically: Boolean
                get() = and(0x20000000) != 0

            /** Whether this global tile id is flipped diagonally. */
            val Int.isFlippedDiagonally: Boolean
                get() = and(0x10000000) != 0

            /** Flips this global tile id horizontally. */
            fun Int.flipHorizontally(): Int = xor(0x40000000)

            /** Flips this global tile id vertically. */
            fun Int.flipVertically(): Int = xor(0x20000000)

            /** Flips this global tile id diagonally. */
            fun Int.flipDiagonally(): Int = xor(0x10000000)

            /** Clears all flag bits. */
            fun Int.clearFlags(): Int = and(0x0FFFFFFF)
        }

        class Builder {
            companion object {
                fun tile(init: Builder.() -> Unit): Tile =
                        Builder().apply(init).build()
            }

            var id: Int by Delegates.notNull<Int>()
            private val animation = arrayListOf<Frame>()

            operator fun Frame.unaryPlus() {
                animation.add(this)
            }

            fun build(): Tile = Tile(
                    id = id,
                    animation = if (animation.isEmpty()) null else animation
            )
        }

        /**
         * A frame within an animation.
         *
         * @property tileId the local id of the tile used for this frame
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
            require(id >= 0) { "$this id can't be negative!" }
            if (animation != null) {
                require(animation.isNotEmpty()) {
                    "$this animation can't have empty frame sequence!"
                }
                require(animation.first().tileId == id) {
                    "$this first animation frame must be equal to tile id!"
                }
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

    @Transient private val tilesArray = Array(tileCount) { Tile(it, null) }
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
        require(tiles.distinctBy { it.id }.size == tiles.size) {
            "$this local tile ids must be unique!"
        }
        val tileIds = tiles.map(Tile::id) + tiles.flatMap {
            it.animation?.map(Frame::tileId) ?: emptyList()
        }
        require(tileIds.all { it in 0 until tileCount }) {
            "$this all tile ids must be between 0 and ${tileCount - 1}"
        }
        tiles.forEach { tilesArray[it.id] = it }
    }

    /**
     * Returns the [Tile] with the given tile id.
     *
     * @param tileId the local id of the requested tile
     * @return the requested tile data
     * @throws IndexOutOfBoundsException if the given [tileId] is negative or if
     * it is greater than or equal to [tileCount]
     */
    fun getTile(tileId: Int): Tile = tilesArray[tileId]

    /**
     * Returns a viewport corresponding to the given tile id, by applying the
     * appropriate margin and spacing offsets.
     *
     * @param tileId the local id of the requested tile
     * @return the viewport associated to the requested tile
     * @throws IndexOutOfBoundsException if the given [tileId] is negative or if
     * it is greater than or equal to [tileCount]
     */
    fun getViewport(tileId: Int): Viewport = viewports[tileId]
}
