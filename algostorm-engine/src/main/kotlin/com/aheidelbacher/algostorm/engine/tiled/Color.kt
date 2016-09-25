package com.aheidelbacher.algostorm.engine.tiled

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

/**
 * A color in the ARGB8888 format.
 *
 * @param string the color code
 * @throws IllegalArgumentException if the given string doesn't conform to the
 * "#AARRGGBB" or "#RRGGBB" format (base 16, case insensitive)
 */
class Color @JsonCreator constructor(string: String) {
    /**
     * The ARGB8888 encoded color.
     */
    val color: Int = run {
        val code = string
        require((code.length == 7 || code.length == 9) && code[0] == '#') {
            "Invalid color code $code format!"
        }
        val argbCode = code.drop(1)
        require(argbCode.none { Character.digit(it, 16) == -1 }) {
            "Color $code contains invalid characters!"
        }
        val argb = java.lang.Long.parseLong(argbCode, 16).toInt()
        if (code.length == 9) argb else argb or (255 shl 24)
    }

    /**
     * The alpha component of this color.
     *
     * It is a value between `0` and `255`.
     */
    val a: Int
        get() = color shr 24 and 255

    /**
     * The red component of this color.
     *
     * It is a value between `0` and `255`.
     */
    val r: Int
        get() = color shr 16 and 255

    /**
     * The green component of this color.
     *
     * It is a value between `0` and `255`.
     */
    val g: Int
        get() = color shr 8 and 255

    /**
     * The blue component of this color.
     *
     * It is a value between `0` and `255`.
     */
    val b: Int
        get() = color and 255

    /**
     * Two colors are equal if and only if they have the same [color] property.
     */
    override fun equals(other: Any?): Boolean =
            other is Color && color == other.color

    override fun hashCode(): Int = color

    @JsonValue override fun toString(): String =
            "#${Integer.toHexString(color)}"
}
