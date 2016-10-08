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
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

import com.aheidelbacher.algostorm.engine.state.Layer.ObjectGroup
import com.aheidelbacher.algostorm.engine.state.Layer.TileLayer

/**
 * An abstract layer in the game world.
 *
 * @property name the name of this layer
 * @property isVisible whether this layer should be rendered or not
 * @property offsetX the horizontal rendering offset in pixels
 * @property offsetY the vertical rendering offset in pixels (positive is down)
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        JsonSubTypes.Type(value = TileLayer::class, name = "TileLayer"),
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
    class TileLayer private constructor(
            name: String,
            val data: LongArray,
            isVisible: Boolean,
            offsetX: Int,
            offsetY: Int
    ) : Layer(name, isVisible, offsetX, offsetY) {
        companion object {
            /** Tile layer factory method. */
            operator fun invoke(
                    name: String,
                    data: LongArray,
                    isVisible: Boolean = true,
                    offsetX: Int = 0,
                    offsetY: Int = 0,
                    properties: Map<String, Property> = emptyMap()
            ): TileLayer = TileLayer(
                    name = name,
                    data = data.copyOf(),
                    isVisible = isVisible,
                    offsetX = offsetX,
                    offsetY = offsetY
            ).apply { this.properties.putAll(properties) }
        }
    }

    /**
     * A layer which contains a set of [objects].
     *
     * @property objects the set of objects contained by this layer
     * @property drawOrder indicates the order in which the objects should be
     * rendered
     * @property color the color with which objects that have their `gid` set to
     * `0` will be filled
     * @throws IllegalArgumentException if [objects] contains multiple objects
     * with the same id
     */
    class ObjectGroup private constructor(
            name: String,
            private val objects: MutableList<Object>,
            val drawOrder: DrawOrder,
            val color: Color?,
            isVisible: Boolean,
            offsetX: Int,
            offsetY: Int
    ) : Layer(name, isVisible, offsetX, offsetY) {
        companion object {
            /** Object group factory method. */
            operator fun invoke(
                    name: String,
                    objects: List<Object>,
                    drawOrder: DrawOrder = DrawOrder.TOP_DOWN,
                    color: Color? = null,
                    isVisible: Boolean = true,
                    offsetX: Int = 0,
                    offsetY: Int = 0,
                    properties: Map<String, Property> = emptyMap()
            ): ObjectGroup = ObjectGroup(
                    name,
                    objects.toMutableList(),
                    drawOrder,
                    color,
                    isVisible,
                    offsetX,
                    offsetY
            ).apply { this.properties.putAll(properties) }
        }

        /** The order in which objects are rendered. */
        enum class DrawOrder {
            @JsonProperty("top-down") TOP_DOWN,
            @JsonProperty("index") INDEX
        }

        @Transient private val objectMap =
                objects.associateByTo(hashMapOf(), Object::id)

        init {
            require(objects.size == objectMap.size) {
                "$this contains multiple objects with the same id!"
            }
        }

        /** A read-only view of the objects in this group. */
        val objectSet: List<Object>
            get() = objects

        /**
         * Returns the object with the given id.
         *
         * @param objectId the id of the requested object
         * @return the requested object, or `null` if it doesn't exist
         */
        operator fun get(objectId: Int): Object? = objectMap[objectId]

        /**
         * Checks whether this object group contains an object with the given
         * id.
         *
         * @param objectId the id of the requested object
         * @return `true` if the object with the given id exists in this object
         * group, `false` otherwise
         */
        operator fun contains(objectId: Int): Boolean = objectId in objectMap

        /**
         * Adds the given objects to this object group.
         *
         * @throws IllegalArgumentException if the id of the given object is not
         * unique among the objects in this object group
         */
        fun add(obj: Object) {
            require(obj.id !in objectMap) {
                "$obj id is not unique within $this!"
            }
            objects.add(obj)
            objectMap[obj.id] = obj
        }

        /**
         * Removes the object with the given id from this object group.
         *
         * @param objectId the id of the object that should be removed
         * @return `true` if the specified object was successfully removed,
         * `false` if it didn't exist in this object group
         */
        fun remove(objectId: Int): Boolean = objectMap[objectId]?.let { obj ->
            objectMap.remove(objectId)
            objects.remove(obj)
        } ?: false

        /** Removes all objects from this object group. */
        fun clear() {
            objects.clear()
            objectMap.clear()
        }
    }
}
