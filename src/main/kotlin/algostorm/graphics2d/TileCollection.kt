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
 * A collection of tile sets which maps global tile ids to viewports.
 *
 * @property tileSets the tile sets of this collection
 */
data class TileCollection(private val tileSets: List<TileSet>) {
    @Transient private val tiles = run {
        val container = hashMapOf<Int, Viewport>()
        for (tileSet in tileSets) {
            for (tileId in tileSet.firstId..tileSet.lastId) {
                require(tileId !in container) {
                    "Tile id is contained in multiple tile sets!"
                }
                container[tileId] = tileSet.getViewport(tileId)
            }
        }
        container
    }

    /**
     * Returns the [Viewport] corresponding to the given [tileId].
     *
     * @param tileId the id of the requested tile
     * @return the requested viewport
     */
    operator fun get(tileId: Int): Viewport =
            tiles[tileId] ?: error("Missing tile id!")
}
