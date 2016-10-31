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

import com.aheidelbacher.algostorm.state.Entity.Factory
import com.aheidelbacher.algostorm.state.Layer.EntityGroup
import com.aheidelbacher.algostorm.state.Layer.TileLayer
import com.aheidelbacher.algostorm.state.MapObject.Orientation
import com.aheidelbacher.algostorm.state.MapObject.RenderOrder
import com.aheidelbacher.algostorm.state.TileSet.Tile
import com.aheidelbacher.algostorm.state.TileSet.Tile.Frame

import kotlin.properties.Delegates

object Builders {
    class MapObjectBuilder : Factory {
        var width: Int by Delegates.notNull()
        var height: Int by Delegates.notNull()
        var tileWidth: Int by Delegates.notNull()
        var tileHeight: Int by Delegates.notNull()
        var orientation: Orientation = Orientation.ORTHOGONAL
        var renderOrder: RenderOrder = RenderOrder.RIGHT_DOWN
        val tileSets: MutableList<TileSet> = arrayListOf()
        val layers: MutableList<Layer> = arrayListOf()
        var backgroundColor: Color? = null
        var version: String = "1.0"
        private var nextObjectId: Int = 1

        override fun create(components: Collection<Component>): Entity {
            check(nextObjectId < Int.MAX_VALUE) {
                "Too many entities in $this!"
            }
            return Entity(nextObjectId++, components)
        }

        operator fun TileSet.unaryPlus() {
            tileSets.add(this)
        }

        operator fun Layer.unaryPlus() {
            layers.add(this)
        }

        fun build(): MapObject = MapObject(
                width = width,
                height = height,
                tileWidth = tileWidth,
                tileHeight = tileHeight,
                orientation = orientation,
                renderOrder = renderOrder,
                tileSets = tileSets.toList(),
                layers = layers.toList(),
                backgroundColor = backgroundColor,
                version = version,
                nextEntityId = nextObjectId
        )
    }

    class TileSetBuilder {
        lateinit var name: String
        var tileWidth: Int by Delegates.notNull()
        var tileHeight: Int by Delegates.notNull()
        lateinit var image: Image
        var margin: Int = 0
        var spacing: Int = 0
        var tileOffsetX: Int = 0
        var tileOffsetY: Int = 0
        val tiles: MutableSet<Tile> = hashSetOf()

        fun image(source: String, width: Int, height: Int) {
            image = Image(File(source), width, height)
        }

        fun image(source: File, width: Int, height: Int) {
            image = Image(source, width, height)
        }

        operator fun Tile.unaryPlus() {
            tiles.add(this)
        }

        fun build(): TileSet = TileSet(
                name = name,
                tileWidth = tileWidth,
                tileHeight = tileHeight,
                image = image,
                columns = image.width / tileWidth,
                tileCount = image.width / tileWidth * image.height / tileWidth,
                margin = margin,
                spacing = spacing,
                tileOffsetX = tileOffsetX,
                tileOffsetY = tileOffsetY,
                tiles = tiles.toSet()
        )
    }

    class TileBuilder {
        var id: Int by Delegates.notNull()
        val animation: MutableList<Frame> = arrayListOf()

        operator fun Frame.unaryPlus() {
            animation.add(this)
        }

        fun build(): Tile = Tile(
                id = id,
                animation = if (animation.isEmpty()) null else animation
        )
    }

    class TileLayerBuilder {
        lateinit var name: String
        lateinit var data: LongArray
        var isVisible: Boolean = true
        var offsetX: Int = 0
        var offsetY: Int = 0

        fun build(): TileLayer =
                TileLayer(name, isVisible, offsetX, offsetY, data.copyOf())
    }

    class EntityGroupBuilder {
        lateinit var name: String
        var isVisible: Boolean = true
        var offsetX: Int = 0
        var offsetY: Int = 0
        val entities: MutableSet<Entity> = hashSetOf()

        operator fun Entity.unaryPlus() {
            entities.add(this)
        }

        fun build(): Layer.EntityGroup = Layer.EntityGroup(
                name = name,
                isVisible = isVisible,
                offsetX = offsetX,
                offsetY = offsetY,
                entities = entities.toSet()
        )
    }

    class EntityBuilder {
        val components: MutableCollection<Component> = arrayListOf()

        operator fun Component.unaryPlus() {
            components.add(this)
        }

        fun build(id: Int): Entity = Entity(id, components)
    }

    inline fun mapObject(init: MapObjectBuilder.() -> Unit): MapObject =
            MapObjectBuilder().apply(init).build()

    inline fun tileSet(init: TileSetBuilder.() -> Unit): TileSet =
            TileSetBuilder().apply(init).build()

    inline fun tile(init: TileBuilder.() -> Unit): Tile =
            TileBuilder().apply(init).build()

    inline fun tileLayer(init: TileLayerBuilder.() -> Unit): TileLayer =
            TileLayerBuilder().apply(init).build()

    inline fun entityGroup(init: EntityGroupBuilder.() -> Unit): EntityGroup =
            EntityGroupBuilder().apply(init).build()

    inline fun Factory.entity(init: EntityBuilder.() -> Unit): Entity =
            create(EntityBuilder().apply(init).components)

    inline fun entity(id: Int, init: EntityBuilder.() -> Unit): Entity =
            EntityBuilder().apply(init).build(id)
}
