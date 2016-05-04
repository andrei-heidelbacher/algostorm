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

package algostorm.assets

import algostorm.assets.Image.Viewport

/**
 * A tileset container.
 *
 * @property name the name of this tileset
 * @property tileWidth the width of a single tile in pixels
 * @property tileHeight the height of a single tile in pixels
 * @property spacing the empty space between successive rows or columns in the tileset in pixels
 * @property margins the empty space around the borders in pixels
 * @property firstId the first tile id present in this tileset
 * @property lastId the last tile id present in this tileset
 * @property image the image associated to this tileset
 * @throws IllegalArgumentException if [tileWidth], [tileHeight], [spacing] or [margins] are
 * negative, if [lastId] is less than [firstId], or if the [image] dimensions are incompatible with
 * the given properties
 */
data class TileSet(
    val name: String,
    val tileWidth: Int,
    val tileHeight: Int,
    val spacing: Int,
    val margins: Int,
    val firstId: Int,
    val lastId: Int,
    val image: Image
) {
  init {
    require(tileWidth >= 0 && tileHeight >= 0) { "Tile dimensions must be non-negative!" }
    require(spacing >= 0) { "Spacing between tiles must be non-negative!" }
    require(margins >= 0) { "Margins must be non-negative!" }
    require(firstId <= lastId) { "Tile ids must make a compact interval!" }
    require(
        (image.width - 2 * margins + spacing) % (tileWidth + spacing) == 0 &&
        (image.height - 2 * margins + spacing) % (tileHeight + spacing) == 0
    ) { "Tile set dimensions, spacing and margins don't match the image dimensions!" }
  }

  /**
   * The number of tiles on a single row in the tileset.
   */
  val tilesPerRow: Int
    get() = (image.width - 2 * margins + spacing) / (tileWidth + spacing)

  /**
   * Returns the viewport corresponding to the given tile.
   *
   * @param tileId the id of the requested tile
   * @return the viewport on the tileset [image] corresponding to the requested tile
   */
  fun getViewport(tileId: Int): Viewport {
    require(firstId <= tileId && tileId <= lastId) { "Invalid tile id!" }
    val row = (tileId - firstId) / tilesPerRow
    val column = (tileId - firstId) % tilesPerRow
    return Viewport(
        x = margins + column * (tileWidth + spacing),
        y = margins + row * (tileHeight + spacing),
        width = tileWidth,
        height = tileHeight
    )
  }
}
