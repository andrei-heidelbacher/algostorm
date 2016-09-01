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
import com.fasterxml.jackson.annotation.JsonValue

import kotlin.collections.Map

interface Properties {
    companion object {
        operator fun invoke(
                properties: Map<String, Any>,
                propertyTypes: Map<String, PropertyType>
        ): Properties = object : Properties {
            override val properties: Map<String, Any>
                get() = properties

            override val propertyTypes: Map<String, PropertyType>
                get() = propertyTypes
        }
    }

    enum class PropertyType {
        @JsonProperty("int") INT,
        @JsonProperty("float") FLOAT,
        @JsonProperty("bool") BOOLEAN,
        @JsonProperty("string") STRING,
        @JsonProperty("color") COLOR,
        @JsonProperty("file") FILE
    }

    /**
     * A color in the ARGB8888 format.
     *
     * Two colors are equal if and only if they have the same [color] property.
     *
     * @param string the color code
     * @throws IllegalArgumentException if the given string doesn't conform to
     * the "#AARRGGBB" or "#RRGGBB" format (base 16, case insensitive)
     */
    class Color @JsonCreator constructor(string: String) {
        /**
         * The ARGB8888 encoded color.
         */
        val color: Int

        init {
            val code = string
            require((code.length == 7 || code.length == 9) && code[0] == '#') {
                "Invalid color code $code format!"
            }
            val argbCode = code.drop(1)
            require(argbCode.all { Character.digit(it, 16) != -1 }) {
                "Color $code contains invalid characters!"
            }
            val argb = java.lang.Long.parseLong(argbCode, 16).toInt()
            color = if (code.length == 9) argb else argb or (255 shl 24)
        }

        /**
         * The alpha component of this color. It is a value between `0` and
         * `255`.
         */
        val a: Int
            get() = color shr 24 and 255

        /**
         * The red component of this color. It is a value between `0` and `255`.
         */
        val r: Int
            get() = color shr 16 and 255

        /**
         * The green component of this color. It is a value between `0` and
         * `255`.
         */
        val g: Int
            get() = color shr 8 and 255

        /**
         * The blue component of this color. It is a value between `0` and
         * `255`.
         */
        val b: Int
            get() = color and 255

        override fun equals(other: Any?): Boolean =
                other is Color && color == other.color

        override fun hashCode(): Int = color

        @JsonValue override fun toString(): String =
                "#${java.lang.Integer.toHexString(color)}"
    }

    /**
     * A path to a file.
     *
     * @param string the path of this file
     */
    class File @JsonCreator constructor(string: String) {
        /**
         * The relative or absolute path of this file.
         */
        val path: String = string

        override fun equals(other: Any?): Boolean =
                other is File && path == other.path

        override fun hashCode(): Int = path.hashCode()

        @JsonValue override fun toString(): String = path
    }

    /**
     * The existing property values.
     */
    val properties: Map<String, Any>

    /**
     * The types of the existing properties.
     */
    val propertyTypes: Map<String, PropertyType>

    /**
     * Checks whether the given property exists.
     *
     * @param name the name of the checked property
     * @return `true` if the given property [name] is contained in [properties],
     * `false` otherwise
     */
    operator fun contains(name: String): Boolean = name in properties

    operator fun get(name: String): Any? = properties[name]?.let { value ->
        when (propertyTypes[name] ?: PropertyType.STRING) {
            PropertyType.INT -> value as Int
            PropertyType.FLOAT -> value as Float
            PropertyType.BOOLEAN -> value as Boolean
            PropertyType.STRING -> value as String
            PropertyType.COLOR -> Color(value as String)
            PropertyType.FILE -> File(value as String)
        }
    }

    fun getInt(name: String): Int? = get(name) as Int?

    fun getFloat(name: String): Float? = get(name) as Float?

    fun getBoolean(name: String): Boolean? = get(name) as Boolean?

    fun getString(name: String): String? = get(name) as String?

    fun getColor(name: String): Color? = get(name) as Color?

    fun getFile(name: String): File? = get(name) as File?
}
