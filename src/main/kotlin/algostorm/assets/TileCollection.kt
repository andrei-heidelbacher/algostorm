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

/**
 * Factory object for tile collections.
 */
object TileCollection {
  /**
   * Returns a tile collection created from the given [tileSets].
   *
   * @param tileSets the tilesets in the tile collection
   * @return a tile collection that maps ids to tiles
   */
  @JvmStatic fun invoke(tileSets: Iterable<TileSet>): AssetCollection<Tile> {
    val container = hashMapOf<Int, Tile>()
    for (tileSet in tileSets) {
      for (tileId in tileSet.firstId..tileSet.lastId) {
        require(tileId !in container) { "Tile id is contained in multiple tilesets!" }
        container[tileId] = Tile(tileSet.image, tileSet.getViewport(tileId))
      }
    }
    return AssetCollection(container)
  }
}
