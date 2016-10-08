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

import com.aheidelbacher.algostorm.engine.state.Layer.ObjectGroup
import com.fasterxml.jackson.annotation.JsonProperty

import com.aheidelbacher.algostorm.engine.state.TileSet.Tile.Companion.clearFlags

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
 * @property backgroundColor the color of the map background
 * @property version the version of this map
 * @property nextObjectId the next available id for an object
 * @throws IllegalArgumentException if [nextObjectId] is negative or if there
 * are multiple tile sets with the same name or multiple layers with the same
 * name or if [width] or [height] or [tileWidth] or [tileHeight] are not
 * positive or if there are multiple objects with the same id or if
 * [nextObjectId] is not greater than the maximum object id
 */
class MapObject private constructor(
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
        private var nextObjectId: Int
) : MutableProperties {
    companion object {
        /** Map object factory method. */
        operator fun invoke(
                width: Int,
                height: Int,
                tileWidth: Int,
                tileHeight: Int,
                orientation: Orientation = Orientation.ORTHOGONAL,
                renderOrder: RenderOrder = RenderOrder.RIGHT_DOWN,
                tileSets: List<TileSet> = emptyList(),
                layers: List<Layer> = emptyList(),
                backgroundColor: Color? = null,
                version: String = "1.0",
                nextObjectId: Int = 1,
                properties: Map<String, Property> = emptyMap()
        ): MapObject = MapObject(
                width = width,
                height = height,
                tileWidth = tileWidth,
                tileHeight = tileHeight,
                orientation = orientation,
                renderOrder = renderOrder,
                tileSets = tileSets,
                layers = layers,
                backgroundColor = backgroundColor,
                version = version,
                nextObjectId = nextObjectId
        ).apply { this.properties.putAll(properties) }
    }

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

    override val properties: MutableMap<String, Property> = hashMapOf()

    @Transient private val gidToTileSet: Array<TileSet?>
    @Transient private val gidToTileId: IntArray

    init {
        require(width > 0) { "$this width must be positive!" }
        require(height > 0) { "$this height must be positive!" }
        require(tileWidth > 0) { "$this tile width must be positive!" }
        require(tileHeight > 0) { "$this tile height must be positive!" }
        require(nextObjectId >= 0) { "$this next object id can't be negative!" }
        require(tileSets.distinctBy(TileSet::name).size == tileSets.size) {
            "Different tile sets in $this can't have the same name!"
        }
        require(layers.distinctBy(Layer::name).size == layers.size) {
            "Different layers in $this can't have the same name!"
        }
        val ids = layers.filterIsInstance<ObjectGroup>()
                .flatMap(ObjectGroup::objectSet).map(Object::id)
        require(ids.distinct().size == ids.size) {
            "$this contains objects with duplicate ids!"
        }
        ids.max()?.let { maxId ->
            require(nextObjectId > maxId) {
                "$this next object id is not greater than max object id $maxId!"
            }
        }
        val totalGidCount = tileSets.sumBy(TileSet::tileCount)
        gidToTileSet = arrayOfNulls<TileSet>(totalGidCount)
        gidToTileId = IntArray(totalGidCount)
        var firstGid = 1
        for (tileSet in tileSets) {
            for (tileId in 0 until tileSet.tileCount) {
                gidToTileSet[tileId + firstGid - 1] = tileSet
                gidToTileId[tileId + firstGid - 1] = tileId
            }
            firstGid += tileSet.tileCount
        }
    }

    /**
     * Returns the next available object id and increments [nextObjectId].
     *
     * @return the next available object id
     * @throws IllegalStateException if there are too many objects
     */
    private fun getAndIncrementNextObjectId(): Int {
        check(nextObjectId < Int.MAX_VALUE) { "Too many objects in $this!" }
        val id = nextObjectId
        nextObjectId++
        return id
    }

    /**
     * Creates an object with the specified parameters.
     *
     * @param name the name of this object
     * @param type the type of this object
     * @param x the horizontal coordinate in pixels of the top-left corner of
     * this object
     * @param y the vertical coordinate in pixels of the top-left corner of this
     * object (positive is down)
     * @param width the width in pixels of this object
     * @param height the height in pixels of this object
     * @param isVisible whether this object should be rendered or not
     * @param gid the global id of the object tile. A value of `0` indicates the
     * empty tile (nothing to draw)
     * @throws IllegalStateException if there are too many objects in this map
     * @throws IllegalArgumentException if [gid] is negative or if [width] or
     * [height] are not positive
     */
    fun createObject(
            name: String = "",
            type: String = "",
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            isVisible: Boolean = true,
            gid: Long = 0L,
            properties: Map<String, Property> = emptyMap()
    ): Object = Object(
            id = getAndIncrementNextObjectId(),
            name = name,
            type = type,
            x = x,
            y = y,
            width = width,
            height = height,
            isVisible = isVisible,
            gid = gid,
            properties = properties
    )

    /**
     * Returns the tile set which contains the given [gid].
     *
     * @param gid the searched global tile id
     * @return the requested tile set
     * @throws IndexOutOfBoundsException if the given [gid] is not positive or
     * is greater than the total number of tiles contained in the map tile sets
     */
    fun getTileSet(gid: Long): TileSet = gidToTileSet[gid.clearFlags() - 1]
            ?: error("Tile set can't be null!")

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
