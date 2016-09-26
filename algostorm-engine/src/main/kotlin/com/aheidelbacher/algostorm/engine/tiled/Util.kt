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

@file:JvmName("Util")

package com.aheidelbacher.algostorm.engine.tiled

import com.aheidelbacher.algostorm.engine.serialization.Serializer
import com.aheidelbacher.algostorm.engine.tiled.Layer.ImageLayer
import com.aheidelbacher.algostorm.engine.tiled.Layer.ObjectGroup
import com.aheidelbacher.algostorm.engine.tiled.Layer.ObjectGroup.DrawOrder
import com.aheidelbacher.algostorm.engine.tiled.Layer.TileLayer
import com.aheidelbacher.algostorm.engine.tiled.MapObject.Orientation
import com.aheidelbacher.algostorm.engine.tiled.MapObject.RenderOrder
import com.aheidelbacher.algostorm.engine.tiled.Property.BooleanProperty
import com.aheidelbacher.algostorm.engine.tiled.Property.ColorProperty
import com.aheidelbacher.algostorm.engine.tiled.Property.FileProperty
import com.aheidelbacher.algostorm.engine.tiled.Property.FloatProperty
import com.aheidelbacher.algostorm.engine.tiled.Property.IntProperty
import com.aheidelbacher.algostorm.engine.tiled.Property.StringProperty
import com.aheidelbacher.algostorm.engine.tiled.TileSet.Tile
import com.aheidelbacher.algostorm.engine.tiled.TileSet.Tile.Frame
import com.aheidelbacher.algostorm.engine.tiled.TileSet.Viewport

import java.io.ByteArrayOutputStream

fun mapObjectOf(
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

fun propertyOf(value: Int): Property = IntProperty(value)

fun propertyOf(value: Float): Property = FloatProperty(value)

fun propertyOf(value: Boolean): Property = BooleanProperty(value)

fun propertyOf(value: String): Property = StringProperty(value)

fun propertyOf(value: File): Property = FileProperty(value)

fun propertyOf(value: Color): Property = ColorProperty(value)

fun tileSetOf(
        name: String,
        tileWidth: Int,
        tileHeight: Int,
        image: File,
        imageWidth: Int,
        imageHeight: Int,
        margin: Int = 0,
        spacing: Int = 0,
        columns: Int,
        tileCount: Int,
        tileOffsetX: Int = 0,
        tileOffsetY: Int = 0,
        tiles: Map<Int, Tile> = emptyMap(),
        properties: Map<String, Property> = emptyMap()
): TileSet = TileSet(
        name = name,
        tileWidth = tileWidth,
        tileHeight = tileHeight,
        image = image,
        imageWidth = imageWidth,
        imageHeight = imageHeight,
        margin = margin,
        spacing = spacing,
        columns = columns,
        tileCount = tileCount,
        tileOffsetX = tileOffsetX,
        tileOffsetY = tileOffsetY,
        tiles = tiles,
        properties = properties
)

fun tileOf(
        animation: List<Frame>? = null,
        objectGroup: ObjectGroup? = null,
        properties: Map<String, Property> = emptyMap()
): Tile = Tile(animation, objectGroup, properties)

fun objectOf(
        id: Int,
        name: String = "",
        type: String = "",
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        isVisible: Boolean = true,
        gid: Long = 0L,
        properties: Map<String, Property> = emptyMap()
) = Object(id, name, type, x, y, width, height, isVisible, gid).apply {
    this.properties.putAll(properties)
}

fun tileLayerOf(
        name: String,
        data: LongArray,
        isVisible: Boolean = true,
        offsetX: Int = 0,
        offsetY: Int = 0,
        properties: Map<String, Property> = emptyMap()
): TileLayer = TileLayer(name, data, isVisible, offsetX, offsetY).apply {
    this.properties.putAll(properties)
}

fun imageLayerOf(
        name: String,
        image: File,
        isVisible: Boolean = true,
        offsetX: Int = 0,
        offsetY: Int = 0,
        properties: Map<String, Property> = emptyMap()
): ImageLayer = ImageLayer(name, image, isVisible, offsetX, offsetY).apply {
    this.properties.putAll(properties)
}

fun objectGroupOf(
        name: String,
        objects: MutableList<Object>,
        drawOrder: DrawOrder = DrawOrder.TOP_DOWN,
        color: Color? = null,
        isVisible: Boolean = true,
        offsetX: Int = 0,
        offsetY: Int = 0,
        properties: Map<String, Property> = emptyMap()
): ObjectGroup = ObjectGroup(
        name,
        objects,
        drawOrder,
        color,
        isVisible,
        offsetX,
        offsetY
).apply { this.properties.putAll(properties) }

fun MapObject.getViewport(gid: Long, currentTimeMillis: Long): Viewport {
    val tileSet = getTileSet(gid)
    val localTileId = getTileId(gid)
    val animation = tileSet.getTile(localTileId).animation
    val tileId = if (animation == null) {
        localTileId
    } else {
        var elapsedTimeMillis =
                currentTimeMillis % animation.sumBy(Frame::duration)
        var i = 0
        do {
            elapsedTimeMillis -= animation[i].duration
            ++i
        } while (elapsedTimeMillis >= 0)
        animation[i - 1].tileId
    }
    return tileSet.getViewport(tileId)
}

inline fun <reified T : Any> Properties.get(name: String): T? =
        getString(name)?.byteInputStream()?.use {
            Serializer.readValue(it)
        }

operator fun <T : Any> MutableProperties.set(name: String, value: T) {
    ByteArrayOutputStream().use {
        Serializer.writeValue(it, value)
        set(name, it.toString())
    }
}
