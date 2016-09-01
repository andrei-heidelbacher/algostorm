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

/**
 * A physical and renderable object in the game. Two objects are equal if and
 * only if they have the same [id].
 *
 * @property id the unique identifier of this object
 * @property name the name of this object
 * @property type the type of this object
 * @property x the x-axis coordinate of the top-left corner of this object in
 * pixels
 * @property y the y-axis coordinate of the top-left corner of this object in
 * pixels
 * @property width the width of this object in pixels
 * @property height the height of this object in pixels
 * @param gid the initial value of the `gid` property
 * @property rotation the clock-wise rotation of this object around the top-left
 * corner in degrees
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
        val width: Int,
        val height: Int,
        @JsonProperty("gid") gid: Long = 0L,
        var rotation: Float = 0F,
        var visible: Boolean = true,
        override val properties: MutableMap<String, Any> = hashMapOf(),
        @JsonProperty("propertytypes") override val propertyTypes
        : MutableMap<String, PropertyType> = hashMapOf()
) : MutableProperties {
    /**
     * The global id of the object tile. A value of `0` indicates the empty tile
     * (nothing to draw).
     *
     * @throws IllegalArgumentException if the set value is negative
     */
    var gid: Long = gid
        set(value) {
            require(gid >= 0L) { "Object $id gid $gid can't be negative!" }
            field = value
        }

    init {
        require(id >= 0) { "Object id $id can't be negative!" }
        require(width > 0) { "Object $id width $width must be positive!" }
        require(height > 0) { "Object $id height $height must be positive!" }
        require(gid >= 0L) { "Object $id gid $gid can't be negative!" }
    }

    override fun equals(other: Any?): Boolean =
            other is Object && id == other.id

    override fun hashCode(): Int = id
}
