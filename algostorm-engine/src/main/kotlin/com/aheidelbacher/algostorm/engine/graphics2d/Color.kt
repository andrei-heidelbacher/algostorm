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
 * Utility methods for manipulating colors.
 */
object Color {
    private const val BITS = 8
    private const val ALPHA_OFFSET = 3 * BITS
    private const val RED_OFFSET = 2 * BITS
    private const val GREEN_OFFSET = 1 * BITS
    private const val BLUE_OFFSET = 0 * BITS
    private const val MAX = (1 shl BITS) - 1
    private const val BASE = 16

    /**
     * @param a the alpha component of the color
     * @param r the red component of the color
     * @param g the green component of the color
     * @param b the blue component of the color
     * @return the requested color in ARGB8888 format
     * @throws IllegalArgumentException if any of the provided components is not
     * in the range `0..255`
     */
    operator fun invoke(a: Int, r: Int, g: Int, b: Int): Int {
        require(a in 0..MAX) { "Color alpha $a is out of bounds!" }
        require(r in 0..MAX) { "Color red $r is out of bounds!" }
        require(g in 0..MAX) { "Color green $g is out of bounds!" }
        require(b in 0..MAX) { "Color blue $b is out of bounds!" }
        return (a shl ALPHA_OFFSET) or (r shl RED_OFFSET) or
                (g shl GREEN_OFFSET) or (b shl BLUE_OFFSET)
    }

    /**
     * Converts the given html color code to ARGB8888.
     *
     * @param htmlColorCode the requested color in "#AARRGGBB" format (base 16,
     * case insensitive)
     * @return the requested color in ARGB8888 format
     * @throws IllegalArgumentException if the given [htmlColorCode] doesn't
     * conform to the "#AARRGGBB" format
     */
    @JvmStatic fun fromHtmlARGB8888(htmlColorCode: String): Int {
        require(htmlColorCode.length == 9 && htmlColorCode[0] == '#') {
            "Invalid color $htmlColorCode!"
        }
        val colorString = htmlColorCode.drop(1)
        require(colorString.all { Character.digit(it, BASE) != -1 }) {
            "Invalid characters in color $htmlColorCode!"
        }
        return java.lang.Long.parseLong(colorString, BASE).toInt()
    }
    /**
     * The alpha component of this color. It is guaranteed to be in the range
     * `0..255`.
     */
    val Int.alpha: Int
        get() = (this shr ALPHA_OFFSET) and MAX

    /**
     * The red component of this color. It is guaranteed to be in the range
     * `0..255`.
     */
    val Int.red: Int
        get() = (this shr RED_OFFSET) and MAX

    /**
     * The green component of this color. It is guaranteed to be in the range
     * `0..255`.
     */
    val Int.green: Int
        get() = (this shr GREEN_OFFSET) and MAX

    /**
     * The blue component of this color. It is guaranteed to be in the range
     * `0..255`.
     */
    val Int.blue: Int
        get() = (this shr BLUE_OFFSET) and MAX
}
