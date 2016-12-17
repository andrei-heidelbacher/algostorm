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

package com.aheidelbacher.algostorm.engine.graphics2d

/**
 * A color in the ARGB8888 format.
 *
 * @property color the ARGB8888 encoded color
 */
data class Color(val color: Int) {
    /**
     * Builds an ARGB8888 encoded color from a color code.
     *
     * @param colorCode the color code
     * @throws IllegalArgumentException if the given string doesn't conform to
     * the `#AARRGGBB` or `#RRGGBB` format (base 16, case insensitive)
     */
    constructor(colorCode: String) : this (colorCode.let { code ->
        require((code.length == 7 || code.length == 9) && code[0] == '#') {
            "Invalid color code $code format!"
        }
        val argbCode = code.drop(1)
        require(argbCode.none { Character.digit(it, 16) == -1 }) {
            "Color $code contains invalid characters!"
        }
        val argb = java.lang.Long.parseLong(argbCode, 16).toInt()
        if (code.length == 9) argb else argb or (255 shl 24)
    })

    /** The alpha component of this color (a value between `0` and `255). */
    val a: Int
        get() = color shr 24 and 255

    /** The red component of this color (a value between `0` and `255`). */
    val r: Int
        get() = color shr 16 and 255

    /** The green component of this color (a value between `0` and `255`). */
    val g: Int
        get() = color shr 8 and 255

    /** The blue component of this color (a value between `0` and `255`). */
    val b: Int
        get() = color and 255

    override fun toString(): String = "#${Integer.toHexString(color)}"
}
