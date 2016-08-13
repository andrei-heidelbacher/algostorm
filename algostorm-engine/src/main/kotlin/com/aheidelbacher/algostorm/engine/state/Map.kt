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

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A map which contains all the game state.
 *
 * The x-axis is increasing from left to right and the y-axis is increasing from
 * top to bottom.
 *
 * @property width the width of the map in tiles
 * @property height the height of the map in tiles
 * @property tileWidth the width of a tile in pixels
 * @property tileHeight the height of a tile in pixels
 * @property orientation the orientation of the map
 * @property renderOrder the order in which objects and tiles are rendered
 * @property tileSets the tile sets used for rendering
 * @property layers the layers of the game
 * @property properties the properties of this map
 * @property version the version of this map
 * @property nextObjectId the next available id for an object
 * @throws IllegalArgumentException if [nextObjectId] is negative or if there
 * are multiple tile sets with the same name or multiple layers with the same
 * name or if [width] or [height] or [tileWidth] or [tileHeight] are not
 * positive
 */
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
        val version: Float = 1F,
        private var nextObjectId: Int
) {
    /**
     * The orientation of the map.
     */
    enum class Orientation {
        @JsonProperty("orthogonal") ORTHOGONAL,
        @JsonProperty("isometric") ISOMETRIC
    }

    /**
     * The rendering order of tiles and objects.
     */
    enum class RenderOrder {
        @JsonProperty("right-down") RIGHT_DOWN,
        @JsonProperty("right-up") RIGHT_UP,
        @JsonProperty("left-down") LEFT_DOWN,
        @JsonProperty("left-up") LEFT_UP
    }

    @Transient private val gidToTileSet = hashMapOf<Long, TileSet>()
    @Transient private val gidToTileId = hashMapOf<Long, Int>()

    init {
        require(width > 0 && height > 0) {
            "Map sizes ($width, $height) must be positive!"
        }
        require(tileWidth > 0 && tileHeight > 0) {
            "Map tile sizes ($tileWidth, $tileHeight) must be positive!"
        }
        require(nextObjectId >= 0) {
            "Map next object id $nextObjectId can't be negative!"
        }
        var firstGid = 1L
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
     * Returns the next available object id and increments [nextObjectId].
     *
     * @return the next available object id
     * @throws IllegalStateException if there are too many objects
     */
    fun getNextObjectId(): Int {
        check(nextObjectId < Int.MAX_VALUE) { "Too many objects!" }
        val id = nextObjectId
        nextObjectId++
        return id
    }

    /**
     * Returns the tile set which contains the given [gid].
     *
     * @param gid the searched global tile id
     * @return the requested tile set, or `null` if the given [gid] isn't
     * contained by any tile sets
     */
    fun getTileSet(gid: Long): TileSet? = gidToTileSet[gid.and(0x1FFFFFFF)]

    /**
     * Returns the local tile id of the given [gid].
     *
     * @param gid the searched global tile id
     * @return the requested local tile id, or `null` if the given [gid] isn't
     * contained by any tile sets
     */
    fun getTileId(gid: Long): Int? = gidToTileId[gid.and(0x1FFFFFFF)]
}
