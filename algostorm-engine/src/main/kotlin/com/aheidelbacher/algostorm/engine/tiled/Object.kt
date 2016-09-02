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

import com.aheidelbacher.algostorm.engine.tiled.Properties.PropertyType
import com.fasterxml.jackson.annotation.JsonCreator

/**
 * A physical and renderable object in the game. Two objects are equal if and
 * only if they have the same [id].
 *
 * @property id the unique identifier of this object
 * @property name the name of this object
 * @property type the type of this object
 * @property visible whether this object should be rendered or not
 * @property properties the properties of this object
 * @throws IllegalArgumentException if [id] is not positive
 */
sealed class Object(
        val id: Int,
        val name: String = "",
        val type: String = "",
        var visible: Boolean = true,
        override val properties: MutableMap<String, Any> = hashMapOf(),
        @JsonProperty("propertytypes") override val propertyTypes
        : MutableMap<String, PropertyType> = hashMapOf()
) : MutableProperties {
    companion object {
        @JsonCreator @JvmStatic operator fun invoke(
                id: Int,
                name: String = "",
                type: String = "",
                x: Int,
                y: Int,
                width: Int = 0,
                height: Int = 0,
                rotation: Float = 0F,
                visible: Boolean = true,
                gid: Long? = null,
                ellipse: Boolean? = null,
                polygon: List<Point>? = null,
                polyline: List<Point>? = null,
                properties: MutableMap<String, Any> = hashMapOf(),
                @JsonProperty("propertytypes") propertyTypes
                : MutableMap<String, PropertyType> = hashMapOf()
        ): Object = when {
            gid == null && ellipse != true
                    && polygon == null && polyline == null -> Rectangle(
                    id = id,
                    name = name,
                    type = type,
                    x = x,
                    y = y,
                    width = width,
                    height = height,
                    rotation = rotation,
                    visible = visible,
                    properties = properties,
                    propertyTypes = propertyTypes
            )
            gid != null -> Tile(
                    id = id,
                    name = name,
                    type = type,
                    x = x,
                    y = y,
                    width = width,
                    height = height,
                    rotation = rotation,
                    visible = visible,
                    gid = gid,
                    properties = properties,
                    propertyTypes = propertyTypes
            )
            ellipse == true -> Ellipse(
                    id = id,
                    name = name,
                    type = type,
                    x = x,
                    y = y,
                    width = width,
                    height = height,
                    rotation = rotation,
                    visible = visible,
                    properties = properties,
                    propertyTypes = propertyTypes
            )
            polygon != null -> Polygon(
                    id = id,
                    name = name,
                    type = type,
                    x = x,
                    y = y,
                    polygon = polygon,
                    rotation = rotation,
                    visible = visible,
                    properties = properties,
                    propertyTypes = propertyTypes
            )
            polyline != null -> Polyline(
                    id = id,
                    name = name,
                    type = type,
                    x = x,
                    y = y,
                    polyline = polyline,
                    rotation = rotation,
                    visible = visible,
                    properties = properties,
                    propertyTypes = propertyTypes
            )
            else -> throw IllegalArgumentException("Invalid object $id!")
        }
    }

    init {
        require(id > 0) { "Object id $id must be positive!" }
    }

    final override fun equals(other: Any?): Boolean =
            other is Object && id == other.id

    final override fun hashCode(): Int = id

    /**
     * @property width the width of this object in pixels
     * @property height the height of this object in pixels
     * @param gid the initial value of the `gid` property
     * @property rotation the clock-wise rotation of this object around the
     * top-left corner in degrees
     */
    class Tile(
            id: Int,
            name: String = "",
            type: String = "",
            var x: Int,
            var y: Int,
            val width: Int,
            val height: Int,
            var rotation: Float = 0F,
            visible: Boolean = true,
            gid: Long = 0L,
            properties: MutableMap<String, Any>,
            @JsonProperty("propertytypes") propertyTypes
            : MutableMap<String, PropertyType>
    ) : Object(id, name, type, visible, properties, propertyTypes) {
        /**
         * The global id of this tile object.
         *
         * A value of `0` indicates the empty tile (nothing to draw).
         *
         * @throws IllegalArgumentException if the set value is negative
         */
        var gid: Long = gid
            set(value) {
                require(gid >= 0L) { "Object $id gid $gid can't be negative!" }
                field = value
            }

        init {
            require(gid >= 0L) { "Object $id gid $gid can't be negative!" }
            require(width >= 0) { "Object $id width $width must be positive!" }
            require(height >= 0) {
                "Object $id height $height must be positive!"
            }
        }
    }

    /**
     * @property x the x-axis coordinate of the top-left corner of this object
     * in pixels
     * @property y the y-axis coordinate of the top-left corner of this object
     * in pixels
     */
    class Rectangle(
            id: Int,
            name: String = "",
            type: String = "",
            var x: Int,
            var y: Int,
            val width: Int,
            val height: Int,
            var rotation: Float = 0F,
            visible: Boolean = true,
            properties: MutableMap<String, Any> = hashMapOf(),
            @JsonProperty("propertytypes") propertyTypes
            : MutableMap<String, PropertyType> = hashMapOf()
    ) : Object(id, name, type, visible, properties, propertyTypes) {
        init {
            require(width >= 0) { "Object $id width $width must be positive!" }
            require(height >= 0) {
                "Object $id height $height must be positive!"
            }
        }
    }

    class Ellipse(
            id: Int,
            name: String = "",
            type: String = "",
            var x: Int,
            var y: Int,
            val width: Int,
            val height: Int,
            var rotation: Float = 0F,
            visible: Boolean = true,
            properties: MutableMap<String, Any> = hashMapOf(),
            @JsonProperty("propertytypes") propertyTypes
            : MutableMap<String, PropertyType> = hashMapOf()
    ) : Object(id, name, type, visible, properties, propertyTypes) {
        val ellipse: Boolean = true

        init {
            require(width >= 0) { "Object $id width $width must be positive!" }
            require(height >= 0) {
                "Object $id height $height must be positive!"
            }
        }
    }

    class Polygon(
            id: Int,
            name: String = "",
            type: String = "",
            var x: Int,
            var y: Int,
            val polygon: List<Point>,
            var rotation: Float = 0F,
            visible: Boolean = true,
            properties: MutableMap<String, Any> = hashMapOf(),
            @JsonProperty("propertytypes") propertyTypes
            : MutableMap<String, PropertyType> = hashMapOf()
    ) : Object(id, name, type, visible, properties, propertyTypes) {
        init {
            require(polygon.size > 2) {
                "Polygon $id must have at least three vertices!"
            }
            require(polygon[0] == Point(0, 0)) {
                "First vertex ${polygon[0]} in polygon $id must be (0, 0)!"
            }
        }
    }

    class Polyline(
            id: Int,
            name: String,
            type: String,
            var x: Int,
            var y: Int,
            val polyline: List<Point>,
            var rotation: Float = 0F,
            visible: Boolean = true,
            properties: MutableMap<String, Any> = hashMapOf(),
            @JsonProperty("propertytypes") propertyTypes
            : MutableMap<String, PropertyType> = hashMapOf()
    ) : Object(id, name, type, visible, properties, propertyTypes) {
        init {
            require(polyline.size > 1) {
                "Polyline $id must have at least two vertices!"
            }
            require(polyline[0] == Point(0, 0)) {
                "First vertex ${polyline[0]} in polyline $id must be (0, 0)!"
            }
        }
    }
}
