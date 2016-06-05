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

package algostorm.graphics2d

import algostorm.graphics2d.Image.Viewport

/**
 * A tile set container.
 *
 * @property name the name of this tile set
 * @property tileWidth the width of a single tile in pixels
 * @property tileHeight the height of a single tile in pixels
 * @property spacing the empty space between successive rows or columns in the
 * tile set in pixels
 * @property margin the empty space around the borders in pixels
 * @property firstId the first tile id present in this tile set
 * @property tileCount the number of tiles contained in this tile set
 * @property image the image associated to this tileset
 * @throws IllegalArgumentException if [tileWidth], [tileHeight], [spacing] or
 * [margin] or [tileCount] is negative, or if the [image] dimensions are
 * incompatible with the given properties
 */
data class TileSet(
        val name: String,
        val tileWidth: Int,
        val tileHeight: Int,
        val spacing: Int,
        val margin: Int,
        val firstId: Int,
        val tileCount: Int,
        val image: Image
) {
    init {
        require(tileWidth >= 0) { "Tile width must be non-negative!" }
        require(tileHeight >= 0) { "Tile height must be non-negative!" }
        require(spacing >= 0) { "Spacing between tiles must be non-negative!" }
        require(margin >= 0) { "Margin must be non-negative!" }
        require(tileCount >= 0) { "Number of tiles can't be negative!" }
        val widthOffset = (image.width - 2 * margin + spacing) %
                (tileWidth + spacing)
        val heightOffset = (image.height - 2 * margin + spacing) %
                (tileHeight + spacing)
        require(widthOffset == 0 && heightOffset == 0) {
            "Tile size, spacing and margin don't match image size!"
        }
    }

    /**
     * The number of tiles on a single row in the tileset.
     */
    val columns: Int
        get() = (image.width - 2 * margin + spacing) / (tileWidth + spacing)

    /**
     * The last tile id present in this tile set.
     */
    val lastId: Int
        get() = firstId + tileCount - 1

    /**
     * Returns the viewport corresponding to the given tile.
     *
     * @param tileId the id of the requested tile
     * @return the viewport on the tileset [image] corresponding to the
     * requested tile
     * @throws IllegalArgumentException if the given [tileId] isn't contained in
     * this tile set
     */
    fun getViewport(tileId: Int): Viewport {
        val index = tileId - firstId
        require(index in 0 until tileCount) { "Invalid tile id!" }
        val row = index / columns
        val column = index % columns
        return Viewport(
                source = image.source,
                x = margin + column * (tileWidth + spacing),
                y = margin + row * (tileHeight + spacing),
                width = tileWidth,
                height = tileHeight
        )
    }
}
