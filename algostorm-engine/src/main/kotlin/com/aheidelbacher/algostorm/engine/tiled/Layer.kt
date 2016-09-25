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
import com.aheidelbacher.algostorm.engine.tiled.Layer.TileLayer

/**
 * An abstract layer in the game world.
 *
 * @property name the name of this layer
 * @property isVisible whether this layer should be rendered or not
 * @property offsetX the x-axis rendering offset in pixels
 * @property offsetY the y-axis rendering offset in pixels
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        JsonSubTypes.Type(value = TileLayer::class, name = "TileLayer"),
        JsonSubTypes.Type(value = ImageLayer::class, name = "ImageLayer"),
        JsonSubTypes.Type(value = ObjectGroup::class, name = "ObjectGroup")
)
sealed class Layer(
        val name: String,
        var isVisible: Boolean,
        val offsetX: Int,
        val offsetY: Int
) : MutableProperties {
    final override val properties: MutableMap<String, Property> = hashMapOf()

    /**
     * Two layers are equal if and only if they have the same name.
     *
     * @param other the layer with which equality is checked
     * @return `true` if the two layers have the same name, `false` otherwise
     */
    final override fun equals(other: Any?): Boolean =
            other is Layer && name == other.name

    final override fun hashCode(): Int = name.hashCode()

    final override fun toString(): String = "${javaClass.simpleName}($name)"

    /**
     * A layer which consists of `width x height` tiles, where `width` and
     * `height` are the dimensions of the containing [MapObject].
     *
     * @property data the global ids of the tiles on the containing map. Index
     * `i` of this array represents the tile with `x = i % width` and
     * `y = i / width`.
     */
    class TileLayer(
            name: String,
            val data: LongArray,
            isVisible: Boolean,
            offsetX: Int,
            offsetY: Int
    ) : Layer(name, isVisible, offsetX, offsetY)

    /**
     * A layer which consists of a single [image].
     *
     * @param image the URI of the image that should be rendered
     */
    class ImageLayer(
            name: String,
            var image: File,
            isVisible: Boolean,
            offsetX: Int,
            offsetY: Int
    ) : Layer(name, isVisible, offsetX, offsetY)

    /**
     * A layer which contains a set of [objects].
     *
     * @property objects the set of objects contained by this layer
     * @property drawOrder indicates the order in which the objects should be
     * rendered
     * @property color the color with which objects that have their `gid` set to
     * `0` will be filled
     */
    class ObjectGroup(
            name: String,
            val objects: MutableList<Object>,
            val drawOrder: DrawOrder,
            val color: Color?,
            isVisible: Boolean,
            offsetX: Int,
            offsetY: Int
    ) : Layer(name, isVisible, offsetX, offsetY) {
        /**
         * The order in which objects are rendered.
         */
        enum class DrawOrder {
            @JsonProperty("top-down") TOP_DOWN,
            @JsonProperty("index") INDEX
        }
    }
}
