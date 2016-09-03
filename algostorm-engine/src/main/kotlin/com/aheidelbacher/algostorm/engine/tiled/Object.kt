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

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

import com.aheidelbacher.algostorm.engine.geometry2d.Point
import com.aheidelbacher.algostorm.engine.tiled.Properties.PropertyType

/**
 * A physical and renderable object in the game.
 *
 * @property id the unique identifier of this object
 * @property name the name of this object
 * @property type the type of this object
 * @property visible whether this object should be rendered or not
 * @property x the x-axis coordinate of the anchor point of this object in
 * pixels
 * @property y the y-axis coordinate of the anchor point of this object in
 * pixels
 * @property rotation the clock-wise rotation of this object in degrees around
 * the anchor point
 * @throws IllegalArgumentException if [id] is not positive
 */
sealed class Object(
        val id: Int,
        val name: String = "",
        val type: String = "",
        var visible: Boolean = true,
        var x: Int,
        var y: Int,
        var rotation: Float = 0F
) : MutableProperties {
    companion object {
        @JsonCreator @JvmStatic operator fun invoke(
                id: Int,
                name: String = "",
                type: String = "",
                visible: Boolean = true,
                x: Int,
                y: Int,
                rotation: Float = 0F,
                width: Int = 0,
                height: Int = 0,
                gid: Long? = null,
                ellipse: Boolean? = null,
                polygon: List<Point>? = null,
                polyline: List<Point>? = null
        ): Object = when {
            gid == null && ellipse != true
                    && polygon == null && polyline == null -> Rectangle(
                    id = id,
                    name = name,
                    type = type,
                    visible = visible,
                    x = x,
                    y = y,
                    rotation = rotation,
                    width = width,
                    height = height
            )
            gid != null -> Tile(
                    id = id,
                    name = name,
                    type = type,
                    visible = visible,
                    x = x,
                    y = y,
                    rotation = rotation,
                    width = width,
                    height = height,
                    gid = gid
            )
            ellipse == true ->
                Ellipse(id, name, type, visible, x, y, rotation, width, height)
            polygon != null ->
                Polygon(id, name, type, visible, x, y, rotation, polygon)
            polyline != null ->
                Polyline(id, name, type, visible, x, y, rotation, polyline)
            else -> throw IllegalArgumentException("Invalid object $id!")
        }
    }

    final override val properties: MutableMap<String, Any> = hashMapOf()

    @JsonProperty("propertytypes")
    final override val propertyTypes: MutableMap<String, PropertyType> =
            hashMapOf()

    init {
        require(id > 0) { "Object id $id must be positive!" }
        this.properties.putAll(properties)
        this.propertyTypes.putAll(propertyTypes)
    }

    /**
     * Two objects are equal if and only if they have the same [id].
     *
     * @param other the object with which equality is checked
     * @return `true` if the two objects have the same [id], `false` otherwise
     */
    final override fun equals(other: Any?): Boolean =
            other is Object && id == other.id

    final override fun hashCode(): Int = id

    final override fun toString(): String = "${javaClass.simpleName}($id)"

    /**
     * A tile object.
     *
     * Tile objects are anchored on the bottom-left corner.
     *
     * @property width the width of this object in pixels
     * @property width the height of this object in pixels
     * @param gid the initial value of the `gid` property
     * @throws IllegalArgumentException if [gid] is negative or if [width] or
     * [height] are not positive
     */
    class Tile(
            id: Int,
            name: String = "",
            type: String = "",
            visible: Boolean = true,
            x: Int,
            y: Int,
            rotation: Float = 0F,
            val width: Int,
            val height: Int,
            gid: Long = 0L
    ) : Object(id, name, type, visible, x, y, rotation) {
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
            require(width > 0) { "Object $id width $width must be positive!" }
            require(height > 0) {
                "Object $id height $height must be positive!"
            }
        }
    }

    /**
     * A rectangle object.
     *
     * Rectangle objects are anchored on the top-left corner.
     *
     * @property width the width of this object in pixels
     * @property width the height of this object in pixels
     * @throws IllegalArgumentException if [width] or [height] are not positive
     */
    class Rectangle(
            id: Int,
            name: String = "",
            type: String = "",
            visible: Boolean = true,
            x: Int,
            y: Int,
            rotation: Float = 0F,
            val width: Int,
            val height: Int
    ) : Object(id, name, type, visible, x, y, rotation) {
        init {
            require(width > 0) { "Object $id width $width must be positive!" }
            require(height > 0) {
                "Object $id height $height must be positive!"
            }
        }
    }

    /**
     * An ellipse object.
     *
     * Ellipse objects are anchored on the top-left corner of their bounding
     * box.
     *
     * @property width the width of this ellipse in pixels
     * @property width the height of this ellipse in pixels
     * @throws IllegalArgumentException if [width] or [height] are not positive
     */
    class Ellipse(
            id: Int,
            name: String = "",
            type: String = "",
            visible: Boolean = true,
            x: Int,
            y: Int,
            rotation: Float = 0F,
            val width: Int,
            val height: Int
    ) : Object(id, name, type, visible, x, y, rotation) {
        /**
         * Property indicating that this object is an ellipse.
         *
         * Always equals to `true`. Used for serialization purposes.
         */
        val ellipse: Boolean = true

        init {
            require(width > 0) { "Object $id width $width must be positive!" }
            require(height > 0) {
                "Object $id height $height must be positive!"
            }
        }
    }

    /**
     * A polygon object.
     *
     * The [polygon] points are given in relative coordinates to the object
     * anchor point `([x], [y])`. The first point in the polygon must be
     * `(0, 0)`.
     *
     * @property polygon the point coordinates which make up the polygon, given
     * in pixels
     * @throws IllegalArgumentException if [polygon] doesn't contain at least
     * three points or if the first point is not equal to `(0, 0)`
     */
    class Polygon(
            id: Int,
            name: String = "",
            type: String = "",
            visible: Boolean = true,
            x: Int,
            y: Int,
            rotation: Float = 0F,
            val polygon: List<Point>
    ) : Object(id, name, type, visible, x, y, rotation) {
        init {
            require(polygon.size > 2) {
                "Polygon $id must have at least three vertices!"
            }
            require(polygon[0] == Point(0, 0)) {
                "First vertex ${polygon[0]} in polygon $id must be (0, 0)!"
            }
        }
    }

    /**
     * A polyline object.
     *
     * The [polyline] points are given in relative coordinates to the object
     * anchor point `([x], [y])`. The first point in the polyline must be
     * `(0, 0)`.
     *
     * @property polyline the point coordinates which make up the polyline,
     * given in pixels
     * @throws IllegalArgumentException if [polyline] doesn't contain at least
     * two points or if the first point is not equal to `(0, 0)`
     */
    class Polyline(
            id: Int,
            name: String,
            type: String,
            visible: Boolean = true,
            x: Int,
            y: Int,
            rotation: Float = 0F,
            val polyline: List<Point>
    ) : Object(id, name, type, visible, x, y, rotation) {
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
