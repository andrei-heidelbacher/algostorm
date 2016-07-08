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

package algostorm.tiled

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo

import algostorm.tiled.Tiled.Map.RenderOrder.RIGHT_DOWN

import kotlin.collections.Map as ImmutableMap

object Tiled {
    class Map(
            val width: Int,
            val height: Int,
            @JsonProperty("tilewidth") val tileWidth: Int,
            @JsonProperty("tileheight") val tileHeight: Int,
            val orientation: String,
            @JsonProperty("renderorder") val renderOrder: String = RIGHT_DOWN,
            @JsonProperty("tilesets") val tileSets: List<TileSet>,
            val layers: List<Layer>,
            @JsonProperty("nextobjectid") private var nextObjectId: Int,
            val properties: MutableMap<String, Any> = hashMapOf(),
            val version: Float = 1F
    ) {
        object Orientation {
            const val ORTHOGONAL: String = "orthogonal"
            const val ISOMETRIC: String = "isometric"
        }

        object RenderOrder {
            const val RIGHT_DOWN: String = "right-down"
            const val RIGHT_UP: String = "right-up"
            const val LEFT_DOWN: String = "left-down"
            const val LEFT_UP: String = "left-up"
        }

        init {
            require(nextObjectId > 0) {
                "Map next object id $nextObjectId must be positive!"
            }
            require(tileSets.zip(tileSets.drop(1)).all {
                it.first.firstGid + it.first.tileCount == it.second.firstGid
            }) { "Tile sets first global ids are not aligned!" }
        }

        /**
         * @throws IllegalStateException if there are too many objects
         */
        fun getNextObjectId(): Int {
            check(nextObjectId > 0 && nextObjectId < Int.MAX_VALUE) {
                "Too many objects!"
            }
            val id = nextObjectId
            nextObjectId++
            return id
        }
    }

    data class TileSet(
            val name: String,
            @JsonProperty("tilewidth") val tileWidth: Int,
            @JsonProperty("tileheight") val tileHeight: Int,
            val image: String,
            @JsonProperty("imagewidth") val imageWidth: Int,
            @JsonProperty("imageheight") val imageHeight: Int,
            val margin: Int,
            val spacing: Int,
            @JsonProperty("firstgid") val firstGid: Int,
            @JsonProperty("tilecount") val tileCount: Int,
            val properties: ImmutableMap<String, Any> = emptyMap(),
            val terrains: List<Terrain> = emptyList(),
            @JsonProperty("tileproperties") private val tileProperties: ImmutableMap<String, ImmutableMap<String, Any>> = emptyMap(),
            private val tiles: ImmutableMap<String, Tile> = emptyMap()
    ) {
        data class Viewport(
                val image: String,
                val x: Int,
                val y: Int,
                val width: Int,
                val height: Int
        ) {
            init {
                require(width >= 0 && height >= 0) {
                    "Viewport sizes ($width, $height) can't be negative!"
                }
            }
        }

        init {
            require(tileWidth > 0 && tileHeight > 0) {
                "$name tile sizes ($tileWidth, $tileHeight) must be positive!"
            }
            require(imageWidth > 0 && imageHeight > 0) {
                "$name image sizes ($imageWidth, $imageHeight) aren't positive!"
            }
            require(margin >= 0) { "$name margin $margin can't be negative!" }
            require(spacing >= 0) {
                "$name spacing $spacing can't be negative!"
            }
            require(firstGid > 0) {
                "$name first gid $firstGid must be positive!"
            }
            require(tileCount > 0) {
                "$name tile count $tileCount must be positive!"
            }
            val widthOffset = (imageWidth - 2 * margin + spacing) %
                    (tileWidth + spacing)
            val heightOffset = (imageHeight - 2 * margin + spacing) %
                    (tileHeight + spacing)
            require(widthOffset == 0 && heightOffset == 0) {
                "$name image sizes don't match margin, spacing and tile sizes!"
            }
        }

        private fun getTileId(gid: Long): Int {
            val tileId = gid.and(0x1FFFFFFF).toInt() - firstGid
            require(tileId in 0..tileCount - 1) {
                "Gid $gid is not part of the $name tile set!"
            }
            return tileId
        }

        fun getTileProperties(gid: Long): ImmutableMap<String, Any> =
                tileProperties["${getTileId(gid)}"] ?: emptyMap()

        fun getTile(gid: Long): Tile = tiles["${getTileId(gid)}"] ?: Tile()

        fun getViewport(gid: Long): Viewport {
            val tileId = getTileId(gid)
            val columns = (imageWidth - 2 * margin + spacing) /
                    (tileWidth + spacing)
            val row = tileId / columns
            val column = tileId % columns
            return Viewport(
                    image = image,
                    x = margin + column * (tileWidth + spacing),
                    y = margin + row * (tileHeight + spacing),
                    width = tileWidth,
                    height = tileHeight
            )
        }
    }

    data class Terrain(
            val name: String,
            @JsonProperty("tileid") val tileId: Int
    ) {
        init {
            require(tileId >= 0) {
                "Terrain $name tile id $tileId can't be negative!"
            }
        }
    }

    data class Tile(
            val animation: List<Frame>? = null,
            val terrain: List<Int?>? = null
    ) {
        companion object {
            val Long.isFlippedHorizontally: Boolean
                get() = and(0x80000000) != 0L

            val Long.isFlippedVertically: Boolean
                get() = and(0x40000000) != 0L

            val Long.isFlippedDiagonally: Boolean
                get() = and(0x20000000) != 0L

            fun Long.flipHorizontally(): Long = xor(0x8000000)

            fun Long.flipVertically(): Long = xor(0x40000000)

            fun Long.flipDiagonally(): Long = xor(0x20000000)
        }

        data class Frame(
                @JsonProperty("tileid") val tileId: Int,
                val duration: Int
        ) {
            init {
                require(tileId >= 0) {
                    "Frame tile id $tileId can't be negative!"
                }
                require(duration > 0) {
                    "Frame duration $duration must be positive!"
                }
            }
        }

        init {
            require(animation == null || animation.isNotEmpty()) {
                "Animation can't be empty!"
            }
            require(terrain == null || terrain.size == 4) {
                "Terrain must contain exactly four elements!"
            }
        }
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type"
    )
    @JsonSubTypes(
            Type(value = Layer.TileLayer::class, name = "tilelayer"),
            Type(value = Layer.ImageLayer::class, name = "imagelayer"),
            Type(value = Layer.ObjectGroup::class, name = "objectgroup")
    )
    sealed class Layer {
        abstract val name: String
        abstract var isVisible: Boolean
        abstract var opacity: Float
        abstract val offsetX: Int
        abstract val offsetY: Int
        abstract val properties: MutableMap<String, Any>

        class TileLayer(
                override val name: String,
                val data: IntArray,
                @JsonProperty("visible") override var isVisible: Boolean = true,
                override var opacity: Float = 1F,
                @JsonProperty("offsetx") override val offsetX: Int = 0,
                @JsonProperty("offsety") override val offsetY: Int = 0,
                override val properties: MutableMap<String, Any> = hashMapOf()
        ) : Layer()

        class ImageLayer(
                override val name: String,
                var image: String,
                @JsonProperty("visible") override var isVisible: Boolean = true,
                override var opacity: Float = 1F,
                @JsonProperty("offsetx") override val offsetX: Int = 0,
                @JsonProperty("offsety") override val offsetY: Int = 0,
                override val properties: MutableMap<String, Any> = hashMapOf()
        ) : Layer()

        class ObjectGroup(
                override val name: String,
                val objects : MutableSet<Object>,
                @JsonProperty("visible") override var isVisible: Boolean = true,
                override var opacity: Float = 1F,
                @JsonProperty("offsetx") override val offsetX: Int = 0,
                @JsonProperty("offsety") override val offsetY: Int = 0,
                override val properties: MutableMap<String, Any> = hashMapOf()
        ) : Layer()
    }

    class Object(
            val id: Int,
            val type: String = "",
            val name: String = "",
            @JsonProperty("visible") var isVisible: Boolean = true,
            var x: Int = 0,
            var y: Int = 0,
            var width: Int = 0,
            var height: Int = 0,
            var rotation: Float = 0F,
            var gid: Long = 0,
            val properties: MutableMap<String, Any> = hashMapOf()
    ) {
        init {
            require(id > 0) { "Object id $id must be positive!" }
            require(width >= 0 && height >= 0) {
                "Object $id sizes ($width, $height) can't be negative!"
            }
            require(gid >= 0) { "Object $id gid $gid can't be negative!" }
        }
        override fun equals(other: Any?): Boolean =
                other is Object && id == other.id

        override fun hashCode(): Int = id
    }
}
