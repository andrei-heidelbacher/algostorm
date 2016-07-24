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

package algostorm.state

class Map(
        val width: Int,
        val height: Int,
        val tileWidth: Int,
        val tileHeight: Int,
        val orientation: Orientation,
        val renderOrder: RenderOrder = RenderOrder.RIGHT_DOWN,
        val tileSets: List<TileSet>,
        val layers: List<Layer>,
        val properties: MutableMap<String, Any> = hashMapOf(),
        val version: String = "1.0",
        private var nextObjectId: Int
) {
    enum class Orientation {
        ORTHOGONAL, ISOMETRIC
    }

    enum class RenderOrder {
        RIGHT_DOWN, RIGHT_UP, LEFT_DOWN, LEFT_UP
    }

    @Transient private val gidToTileSet = hashMapOf<Int, TileSet>()
    @Transient private val gidToTileId = hashMapOf<Int, Int>()

    init {
        require(nextObjectId >= 0) {
            "Map next object id $nextObjectId can't be negative!"
        }
        var firstGid = 1
        for (tileSet in tileSets) {
            for (tileId in 0..tileSet.tileCount - 1) {
                gidToTileSet[tileId + firstGid] = tileSet
                gidToTileId[tileId + firstGid] = tileId
            }
            firstGid += tileSet.tileCount
        }
        require(tileSets.distinct().size == tileSets.size) {
            "Different tile sets can't have the same name!"
        }
        require(layers.distinct().size == layers.size) {
            "Different layers can't have the same name!"
        }
    }

    /**
     * @throws IllegalStateException if there are too many objects
     */
    fun getNextObjectId(): Int {
        check(nextObjectId < Int.MAX_VALUE) { "Too many objects!" }
        val id = nextObjectId
        nextObjectId++
        return id
    }

    fun getTileSet(gid: Int): TileSet? =
            gidToTileSet[gid.and(0x0FFFFFFF)]

    fun getTileId(gid: Int): Int? =
            gidToTileId[gid.and(0x0FFFFFFF)]
}
