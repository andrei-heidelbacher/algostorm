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

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

import com.aheidelbacher.algostorm.engine.state.Layer.ImageLayer
import com.aheidelbacher.algostorm.engine.state.Layer.ObjectGroup
import com.aheidelbacher.algostorm.engine.state.Layer.TileLayer

/**
 * An abstract layer in the game world.
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
sealed class Layer {
    /**
     * The name of this layer. Two layers are equal if and only if they have the
     * same name.
     */
    abstract val name: String

    /**
     * Whether this layer should be rendered or not.
     */
    abstract var isVisible: Boolean

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

    /**
     * The properties of this layer.
     */
    abstract val properties: MutableMap<String, Any>

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
            val data: IntArray,
            override var isVisible: Boolean = true,
            override var opacity: Float = 1F,
            override val offsetX: Int = 0,
            override val offsetY: Int = 0,
            override val properties: MutableMap<String, Any> = hashMapOf()
    ) : Layer()

    /**
     * A layer which consists of a single [image].
     *
     * @param image the URI of the image that should be rendered
     */
    class ImageLayer(
            override val name: String,
            var image: String,
            override var isVisible: Boolean = true,
            override var opacity: Float = 1F,
            override val offsetX: Int = 0,
            override val offsetY: Int = 0,
            override val properties: MutableMap<String, Any> = hashMapOf()
    ) : Layer()

    /**
     * A layer which contains a set of [objects].
     *
     * @property objects the set of objects contained by this layer
     */
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
