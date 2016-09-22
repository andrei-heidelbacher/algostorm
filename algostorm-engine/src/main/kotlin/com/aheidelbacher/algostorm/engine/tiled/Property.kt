package com.aheidelbacher.algostorm.engine.tiled

import com.fasterxml.jackson.annotation.JsonProperty

data class Property private constructor(val type: Type, val value: Any) {
    companion object {
        operator fun invoke(type: Type, rawValue: Any): Property = when(type) {
            Type.INT -> Property(type, rawValue as Int)
            Type.FLOAT -> Property(type, rawValue as Float)
            Type.BOOLEAN -> Property(type, rawValue as Boolean)
            Type.STRING -> Property(type, rawValue as String)
            Type.FILE -> Property(type, File(rawValue as String))
            Type.COLOR -> Property(type, Color(rawValue as String))
        }

        operator fun invoke(value: Int): Property = Property(Type.INT, value)

        operator fun invoke(value: Float): Property =
                Property(Type.FLOAT, value)

        operator fun invoke(value: Boolean): Property =
                Property(Type.BOOLEAN, value)

        operator fun invoke(value: String): Property =
                Property(Type.STRING, value)

        operator fun invoke(value: File): Property =
                Property(Type.FILE, value)

        operator fun invoke(value: Color): Property =
                Property(Type.COLOR, value)
    }

    enum class Type {
        @JsonProperty("int") INT,
        @JsonProperty("float") FLOAT,
        @JsonProperty("bool") BOOLEAN,
        @JsonProperty("string") STRING,
        @JsonProperty("file") FILE,
        @JsonProperty("color") COLOR
    }
}
