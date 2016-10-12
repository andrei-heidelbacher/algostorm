package com.aheidelbacher.algostorm.state

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

/**
 * A color in the ARGB8888 format.
 *
 * @property color the ARGB8888 encoded color
 */
data class Color(val color: Int) {
    /**
     * Builds an ARGB8888 encoded color from a color code.
     *
     * @param string the color code
     * @throws IllegalArgumentException if the given string doesn't conform to
     * the "#AARRGGBB" or "#RRGGBB" format (base 16, case insensitive)
     */
    @JsonCreator constructor(string: String) : this (string.let { code ->
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

    @JsonValue override fun toString(): String =
            "#${Integer.toHexString(color)}"
}
