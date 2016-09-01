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

package com.aheidelbacher.algostorm.engine.tiled

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

import com.aheidelbacher.algostorm.engine.tiled.Layer.ImageLayer
import com.aheidelbacher.algostorm.engine.tiled.Layer.ObjectGroup
import com.aheidelbacher.algostorm.engine.tiled.Layer.ObjectGroup.DrawOrder.TOP_DOWN
import com.aheidelbacher.algostorm.engine.tiled.Layer.TileLayer
import com.aheidelbacher.algostorm.engine.tiled.Properties.Color
import com.aheidelbacher.algostorm.engine.tiled.Properties.File
import com.aheidelbacher.algostorm.engine.tiled.Properties.PropertyType
import com.fasterxml.jackson.annotation.JsonInclude

/**
 * An abstract layer in the game world.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        JsonSubTypes.Type(value = TileLayer::class, name = "tilelayer"),
        JsonSubTypes.Type(value = ImageLayer::class, name = "imagelayer"),
        JsonSubTypes.Type(value = ObjectGroup::class, name = "objectgroup")
)
sealed class Layer : MutableProperties {
    /**
     * The name of this layer. Two layers are equal if and only if they have the
     * same name.
     */
    abstract val name: String

    /**
     * Whether this layer should be rendered or not.
     */
    abstract var visible: Boolean

    /**
     * The opacity of this layer. Should be a value between `0` and `1`.
     */
    abstract var opacity: Float

    /**
     * The x-axis rendering offset in pixels.
     */
    abstract val offsetX: Int

    /**
     * The y-axis rendering offset in pixels.
     */
    abstract val offsetY: Int

    final override fun equals(other: Any?): Boolean =
            other is Layer && name == other.name

    final override fun hashCode(): Int = name.hashCode()

    /**
     * A layer which consists of `width x height` tiles, where `width` and
     * `height` are the dimensions of the containing [Map].
     *
     * @property data the global ids of the tiles on the containing map. Index
     * `i` of this array represents the tile with `x = i % width` and
     * `y = i / width`.
     */
    class TileLayer(
            override val name: String,
            val data: LongArray,
            override var visible: Boolean = true,
            override var opacity: Float = 1F,
            @JsonProperty("offsetx") override val offsetX: Int = 0,
            @JsonProperty("offsety") override val offsetY: Int = 0,
            override val properties: MutableMap<String, Any> = hashMapOf(),
            @JsonProperty("propertytypes") override val propertyTypes
            : MutableMap<String, PropertyType> = hashMapOf()
    ) : Layer()

    /**
     * A layer which consists of a single [image].
     *
     * @param image the URI of the image that should be rendered
     */
    class ImageLayer(
            override val name: String,
            var image: File,
            override var visible: Boolean = true,
            override var opacity: Float = 1F,
            @JsonProperty("offsetx") override val offsetX: Int = 0,
            @JsonProperty("offsety") override val offsetY: Int = 0,
            override val properties: MutableMap<String, Any> = hashMapOf(),
            @JsonProperty("propertytpes") override val propertyTypes
            : MutableMap<String, PropertyType> = hashMapOf()
    ) : Layer()

    /**
     * A layer which contains a set of [objects].
     *
     * @property objects the set of objects contained by this layer
     * @property drawOrder indicates the order in which the objects should be
     * rendered
     * @property color the color with which objects that have their `gid` set to
     * `0` will be filled
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class ObjectGroup(
            override val name: String,
            val objects: MutableList<Object>,
            @JsonProperty("draworder") val drawOrder: DrawOrder = TOP_DOWN,
            val color: Color? = null,
            override var visible: Boolean = true,
            override var opacity: Float = 1F,
            @JsonProperty("offsetx") override val offsetX: Int = 0,
            @JsonProperty("offsety") override val offsetY: Int = 0,
            override val properties: MutableMap<String, Any> = hashMapOf(),
            @JsonProperty("propertytypes") override val propertyTypes
            : MutableMap<String, PropertyType> = hashMapOf()
    ) : Layer() {
        /**
         * The order in which objects are rendered.
         */
        enum class DrawOrder {
            @JsonProperty("topdown") TOP_DOWN,
            @JsonProperty("index") INDEX
        }
    }
}
