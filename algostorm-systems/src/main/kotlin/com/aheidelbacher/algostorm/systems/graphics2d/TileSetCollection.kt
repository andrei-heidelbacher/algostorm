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

package com.aheidelbacher.algostorm.systems.graphics2d

import com.aheidelbacher.algostorm.core.engine.graphics2d.TileSet
import com.aheidelbacher.algostorm.core.engine.graphics2d.TileSet.Companion.clearFlags
import com.aheidelbacher.algostorm.core.engine.graphics2d.TileSet.Frame
import com.aheidelbacher.algostorm.core.engine.graphics2d.TileSet.Viewport

data class TileSetCollection(val tileSets: List<TileSet>) {
    @Transient private val gidToViewport: Array<Viewport>
    @Transient private val gidToTileSet: Array<TileSet>
    @Transient private val gidToTileId: IntArray
    @Transient private val animations: Map<String, List<Frame>>

    init {
        require(tileSets.distinctBy(TileSet::name).size == tileSets.size) {
            "$this can't contain multiple tile sets with the same name!"
        }
        val animationList = tileSets.flatMap { it.animations.toList() }
        require(
                animationList.distinctBy { it.first }.size == animationList.size
        ) { "$this tile sets can't contain animations with the same name!" }
        animations = animationList.toMap()
        val totalGidCount = tileSets.sumBy(TileSet::tileCount)
        val viewports = arrayListOf<Viewport>()
        val tileSetsByGid = arrayListOf<TileSet>()
        gidToTileId = IntArray(totalGidCount)
        var firstGid = 1
        for (tileSet in tileSets) {
            for (tileId in 0 until tileSet.tileCount) {
                viewports.add(tileSet.getViewport(tileId))
                tileSetsByGid.add(tileSet)
                gidToTileId[tileId + firstGid - 1] = tileId
            }
            firstGid += tileSet.tileCount
        }
        gidToViewport = viewports.toTypedArray()
        gidToTileSet = tileSetsByGid.toTypedArray()
    }

    /**
     * Returns the local tile id of the given [gid].
     *
     * @param gid the searched global tile id
     * @return the requested local tile id
     * @throws IndexOutOfBoundsException if the given [gid] is not positive or
     * is greater than the total number of tiles contained in the map tile sets
     */
    fun getTileId(gid: Int): Int = gidToTileId[gid.clearFlags() - 1]

    fun getAnimation(animation: String): List<Frame>? = animations[animation]

    fun getViewport(gid: Int): Viewport = gidToViewport[gid.clearFlags() - 1]

    /*fun getViewport(gid: Int, elapsedMillis: Long): Viewport {
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
    }*/
}
