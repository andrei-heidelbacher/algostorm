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

/**
 * A physical and renderable object in the game. Two objects are equal if and
 * only if they have the same [id].
 *
 * @property id the unique identifier of this object
 * @property name the name of this object
 * @property type the type of this object
 * @property x the x-axis coordinate of the bottom-left corner of this object in
 * pixels
 * @property y the y-axis coordinate of the bottom-left corner of this object in
 * pixels
 * @property width the initial width of this object in pixels
 * @property height the initial height of this object in pixels
 * @param gid the initial value of the `gid` property
 * @property rotation the clock-wise rotation of this object around the
 * bottom-left corner in degrees
 * @property visible whether this object should be rendered or not
 * @property properties the properties of this object
 * @throws IllegalArgumentException if [id] or [gid] is negative or if [width]
 * or [height] are not positive
 */
class Object(
        val id: Int,
        val name: String = "",
        val type: String = "",
        var x: Int,
        var y: Int,
        @JsonProperty("width") width: Int,
        @JsonProperty("height") height: Int,
        @JsonProperty("gid") gid: Long = 0L,
        var rotation: Float = 0F,
        var visible: Boolean = true,
        val properties: MutableMap<String, Any> = hashMapOf()
) {
    private companion object {
        fun validateWidth(id: Int, width: Int) {
            require(width > 0) { "Object $id width $width must be positive!" }
        }

        fun validateHeight(id: Int, height: Int) {
            require(height > 0) {
                "Object $id height $height must be positive!"
            }
        }

        fun validateGid(id: Int, gid: Long) {
            require(gid >= 0L) { "Object $id gid $gid can't be negative!" }
        }
    }

    init {
        require(id >= 0) { "Object id $id can't be negative!" }
        validateWidth(id, width)
        validateHeight(id, height)
        validateGid(id, gid)
    }

    /**
     * The width of this object in pixels.
     *
     * @throws IllegalArgumentException if the set value is not positive
     */
    var width: Int = width
        set(value) {
            validateWidth(id, value)
            field = value
        }

    /**
     * The height of this object in pixels. Must be positive.
     *
     * @throws IllegalArgumentException if the set value is not positive
     */
    var height: Int = height
        set(value) {
            validateHeight(id, value)
            field = value
        }

    /**
     * The global id of the object tile. A value of `0` indicates the empty tile
     * (nothing to draw).
     *
     * @throws IllegalArgumentException if the set value is negative
     */
    var gid: Long = gid
        set(value) {
            validateGid(id, value)
            field = value
        }

    /**
     * Returns the property with the given name.
     *
     * @param propertyName the name of the requested property
     * @return the property with the given name, or `null` if there is no such
     * property
     */
    operator fun get(propertyName: String): Any? = properties[propertyName]

    /**
     * Sets the value of the property with the given name.
     *
     * @param T the type of the property
     * @param propertyName the name of the property
     * @param value the new value of the property
     */
    operator fun <T : Any> set(propertyName: String, value: T) {
        properties[propertyName] = value
    }

    /**
     * Checks whether this object contains a property with the given name.
     *
     * @param propertyName the name of the property
     * @return whether this object contains the given property
     */
    operator fun contains(propertyName: String): Boolean =
            propertyName in properties

    /**
     * Removes the property with the given name.
     *
     * @param propertyName the name of the property that should be removed
     */
    fun remove(propertyName: String) {
        properties.remove(propertyName)
    }

    override fun equals(other: Any?): Boolean =
            other is Object && id == other.id

    override fun hashCode(): Int = id
}
