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

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo

import kotlin.collections.Map as ImmutableMap

object State {
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
            require(tileSets.map { it.name }.distinct().size == tileSets.size) {
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

        fun getTileSet(gid: Long): TileSet? =
                gidToTileSet[gid.and(0x1FFFFFF).toInt()]

        fun getTileId(gid: Long): Int? =
                gidToTileId[gid.and(0x1FFFFFF).toInt()]
    }

    data class TileSet(
            val name: String,
            val tileWidth: Int,
            val tileHeight: Int,
            val image: String,
            val imageWidth: Int,
            val imageHeight: Int,
            val margin: Int,
            val spacing: Int,
            val tileCount: Int,
            val properties: ImmutableMap<String, Any> = emptyMap(),
            val tiles: ImmutableMap<Int, Tile> = emptyMap()
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

        fun getViewport(tileId: Int): Viewport {
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

    data class Tile(
            val animations: ImmutableMap<String, List<Frame>> = emptyMap(),
            val properties: ImmutableMap<String, Any> = emptyMap()
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

        data class Frame(val tileId: Int, val duration: Int) {
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
            require(animations.all { it.value.isNotEmpty() }) {
                "Animation can't have empty frame sequence!"
            }
        }
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type"
    )
    @JsonSubTypes(
            Type(value = Layer.TileLayer::class, name = "TileLayer"),
            Type(value = Layer.ImageLayer::class, name = "ImageLayer"),
            Type(value = Layer.ObjectGroup::class, name = "ObjectGroup")
    )
    sealed class Layer {
        abstract val name: String
        abstract var isVisible: Boolean
        abstract var opacity: Float
        abstract val offsetX: Int
        abstract val offsetY: Int
        abstract val properties: MutableMap<String, Any>

        final override fun equals(other: Any?): Boolean =
                other is Layer && name == other.name

        final override fun hashCode(): Int = name.hashCode()

        class TileLayer(
                override val name: String,
                val data: IntArray,
                override var isVisible: Boolean = true,
                override var opacity: Float = 1F,
                override val offsetX: Int = 0,
                override val offsetY: Int = 0,
                override val properties: MutableMap<String, Any> = hashMapOf()
        ) : Layer()

        class ImageLayer(
                override val name: String,
                var image: String,
                override var isVisible: Boolean = true,
                override var opacity: Float = 1F,
                override val offsetX: Int = 0,
                override val offsetY: Int = 0,
                override val properties: MutableMap<String, Any> = hashMapOf()
        ) : Layer()

        class ObjectGroup(
                override val name: String,
                val objects : MutableSet<Object>,
                override var isVisible: Boolean = true,
                override var opacity: Float = 1F,
                override val offsetX: Int = 0,
                override val offsetY: Int = 0,
                override val properties: MutableMap<String, Any> = hashMapOf()
        ) : Layer()
    }

    class Object(
            val id: Int,
            val x: Int,
            val y: Int,
            val width: Int,
            val height: Int,
            val gid: Int = 0,
            val isVisible: Boolean = true,
            val properties: MutableMap<String, Any> = hashMapOf()
    ) {
        init {
            require(id >= 0) { "Object id $id can't be negative!" }
            require(gid >= 0) { "Object gid $gid can't be negative!" }
            require(width > 0 && height > 0) {
                "Object $id sizes ($width, $height) must be positive!"
            }
        }

        override fun equals(other: Any?): Boolean =
                other is Object && id == other.id

        override fun hashCode(): Int = id
    }
}
