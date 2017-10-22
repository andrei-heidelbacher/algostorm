/*
 * Copyright 2017 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.andreihh.algostorm.core.drivers.graphics2d

/**
 * A color in the `ARGB8888` format.
 *
 * @property color the `ARGB8888` encoded color
 */
data class Color(val color: Int) {
    /**
     * Builds an `ARGB8888` encoded color from a color `code`.
     *
     * @param code the color code
     * @throws IllegalArgumentException if the given `code` doesn't conform to
     * the `#AARRGGBB` or `#RRGGBB` format (base `16`, case insensitive)
     */
    constructor(code: String) : this (code.let {
        require((code.length == 7 || code.length == 9) && code[0] == '#') {
            "Color '$code' has invalid format!"
        }
        val argbCode = code.drop(1)
        require(argbCode.none { Character.digit(it, 16) == -1 }) {
            "Color '$code' contains invalid characters!"
        }
        val argb = argbCode.toLong(16).toInt()
        if (code.length == 9) argb else argb or (0xFF shl 24)
    })

    /** The alpha component of this color (a value between `0` and `255). */
    val a: Int
        get() = color shr 24 and 0xFF

    /** The red component of this color (a value between `0` and `255`). */
    val r: Int
        get() = color shr 16 and 0xFF

    /** The green component of this color (a value between `0` and `255`). */
    val g: Int
        get() = color shr 8 and 0xFF

    /** The blue component of this color (a value between `0` and `255`). */
    val b: Int
        get() = color and 0xFF

    /** Returns the color in `#AARRGGBB` format. */
    override fun toString(): String = "#${String.format("%08x", color)}"
}
