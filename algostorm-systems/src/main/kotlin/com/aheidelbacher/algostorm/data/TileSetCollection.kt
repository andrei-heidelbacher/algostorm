/*
 * Copyright 2017 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.aheidelbacher.algostorm.data

import com.aheidelbacher.algostorm.data.TileSet.Tile.Companion.clearFlags
import com.aheidelbacher.algostorm.data.TileSet.Tile.Frame
import com.aheidelbacher.algostorm.data.TileSet.Viewport

data class TileSetCollection(val tileSets: List<TileSet>) {
    @Transient private val gidToTileSet: Array<TileSet>
    @Transient private val gidToTileId: IntArray

    init {
        require(tileSets.distinctBy(TileSet::name).size == tileSets.size) {
            "$this can't contain multiple tile sets with the same name!"
        }
        val totalGidCount = tileSets.sumBy(TileSet::tileCount)
        val tileSetsByGid = arrayListOf<TileSet>()
        gidToTileId = IntArray(totalGidCount)
        var firstGid = 1
        for (tileSet in tileSets) {
            for (tileId in 0 until tileSet.tileCount) {
                tileSetsByGid.add(tileSet)
                gidToTileId[tileId + firstGid - 1] = tileId
            }
            firstGid += tileSet.tileCount
        }
        gidToTileSet = tileSetsByGid.toTypedArray()
    }

    /**
     * Returns the tile set which contains the given [gid].
     *
     * @param gid the searched global tile id
     * @return the requested tile set
     * @throws IndexOutOfBoundsException if the given [gid] is not positive or
     * is greater than the total number of tiles contained in the map tile sets
     */
    fun getTileSet(gid: Int): TileSet = gidToTileSet[gid.clearFlags() - 1]

    /**
     * Returns the local tile id of the given [gid].
     *
     * @param gid the searched global tile id
     * @return the requested local tile id
     * @throws IndexOutOfBoundsException if the given [gid] is not positive or
     * is greater than the total number of tiles contained in the map tile sets
     */
    fun getTileId(gid: Int): Int = gidToTileId[gid.clearFlags() - 1]

    fun getViewport(gid: Int, elapsedMillis: Long): Viewport {
        val tileSet = getTileSet(gid)
        val localTileId = getTileId(gid)
        val animation = tileSet.getTile(localTileId).animation
        val tileId = if (animation == null) {
            localTileId
        } else {
            var t = elapsedMillis % animation.sumBy(Frame::duration)
            var i = 0
            do {
                t -= animation[i].duration
                ++i
            } while (t >= 0)
            animation[i - 1].tileId
        }
        return tileSet.getViewport(tileId)
    }
}
