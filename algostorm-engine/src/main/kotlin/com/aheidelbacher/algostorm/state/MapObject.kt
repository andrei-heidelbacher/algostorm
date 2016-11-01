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

import com.fasterxml.jackson.annotation.JsonProperty

import com.aheidelbacher.algostorm.state.Layer.EntityGroup
import com.aheidelbacher.algostorm.state.TileSet.Tile.Companion.clearFlags

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
 * @property renderOrder the order in which tiles are rendered
 * @property tileSets the tile sets used for rendering
 * @property layers the layers of the game
 * @property backgroundColor the color of the map background
 * @property version the version of this map
 * @property nextEntityId the next available id for an entity
 * @throws IllegalArgumentException if [nextEntityId] is not positive or if
 * there are multiple tile sets with the same name or multiple layers with the
 * same name or if [width] or [height] or [tileWidth] or [tileHeight] are not
 * positive or if there are multiple entities with the same id or if
 * [nextEntityId] is not greater than the maximum entity id
 */
class MapObject internal constructor(
        val width: Int,
        val height: Int,
        val tileWidth: Int,
        val tileHeight: Int,
        val orientation: Orientation,
        val renderOrder: RenderOrder,
        val tileSets: List<TileSet>,
        val layers: List<Layer>,
        val backgroundColor: Color?,
        val version: String,
        private var nextEntityId: Int
) : Entity.Factory {
    /** The orientation of the map. */
    enum class Orientation {
        @JsonProperty("orthogonal") ORTHOGONAL
    }

    /** The rendering order of tiles. */
    enum class RenderOrder {
        @JsonProperty("right-down") RIGHT_DOWN,
        @JsonProperty("right-up") RIGHT_UP,
        @JsonProperty("left-down") LEFT_DOWN,
        @JsonProperty("left-up") LEFT_UP
    }

    @Transient private val gidToTileSet: Array<TileSet>
    @Transient private val gidToTileId: IntArray

    init {
        require(width > 0) { "$this width must be positive!" }
        require(height > 0) { "$this height must be positive!" }
        require(tileWidth > 0) { "$this tile width must be positive!" }
        require(tileHeight > 0) { "$this tile height must be positive!" }
        require(nextEntityId > 0) { "$this next entity id must be positive!" }
        require(tileSets.distinctBy(TileSet::name).size == tileSets.size) {
            "Different tile sets in $this can't have the same name!"
        }
        require(layers.distinctBy(Layer::name).size == layers.size) {
            "Different layers in $this can't have the same name!"
        }
        val ids = layers.filterIsInstance<EntityGroup>()
                .flatMap(EntityGroup::entities).map(Entity::id)
        require(ids.distinct().size == ids.size) {
            "$this contains entities with duplicate ids!"
        }
        ids.max()?.let { maxId ->
            require(nextEntityId > maxId) {
                "$this next entity id is not greater than max entity id $maxId!"
            }
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

    override fun create(components: Collection<Component>): Entity {
        check(nextEntityId < Int.MAX_VALUE) { "Too many entities in $this!" }
        return Entity(nextEntityId++, components)
    }

    /**
     * Returns the tile set which contains the given [gid].
     *
     * @param gid the searched global tile id
     * @return the requested tile set
     * @throws IndexOutOfBoundsException if the given [gid] is not positive or
     * is greater than the total number of tiles contained in the map tile sets
     */
    fun getTileSet(gid: Long): TileSet = gidToTileSet[gid.clearFlags() - 1]

    /**
     * Returns the local tile id of the given [gid].
     *
     * @param gid the searched global tile id
     * @return the requested local tile id
     * @throws IndexOutOfBoundsException if the given [gid] is not positive or
     * is greater than the total number of tiles contained in the map tile sets
     */
    fun getTileId(gid: Long): Int = gidToTileId[gid.clearFlags() - 1]
}
