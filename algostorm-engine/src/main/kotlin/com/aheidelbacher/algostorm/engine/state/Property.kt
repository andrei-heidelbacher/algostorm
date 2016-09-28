package com.aheidelbacher.algostorm.engine.state

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

import com.aheidelbacher.algostorm.engine.state.Property.BooleanProperty
import com.aheidelbacher.algostorm.engine.state.Property.ColorProperty
import com.aheidelbacher.algostorm.engine.state.Property.FileProperty
import com.aheidelbacher.algostorm.engine.state.Property.FloatProperty
import com.aheidelbacher.algostorm.engine.state.Property.IntProperty
import com.aheidelbacher.algostorm.engine.state.Property.StringProperty

/**
 * An immutable value of a property.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        JsonSubTypes.Type(value = IntProperty::class, name = "int"),
        JsonSubTypes.Type(value = FloatProperty::class, name = "float"),
        JsonSubTypes.Type(value = BooleanProperty::class, name = "bool"),
        JsonSubTypes.Type(value = StringProperty::class, name = "string"),
        JsonSubTypes.Type(value = FileProperty::class, name = "file"),
        JsonSubTypes.Type(value = ColorProperty::class, name = "color")
)
sealed class Property {
    class IntProperty(val value: Int) : Property() {
        override fun equals(other: Any?): Boolean =
                other is IntProperty && value == other.value

        override fun hashCode(): Int = value
    }

    class FloatProperty(val value: Float) : Property() {
        override fun equals(other: Any?): Boolean =
                other is FloatProperty && value == other.value

        override fun hashCode(): Int = value.hashCode()
    }

    class BooleanProperty(val value: Boolean) : Property() {
        override fun equals(other: Any?): Boolean =
                other is BooleanProperty && value == other.value

        override fun hashCode(): Int = if (value) 1 else 0
    }

    class StringProperty(val value: String) : Property() {
        override fun equals(other: Any?): Boolean =
                other is StringProperty && value == other.value

        override fun hashCode(): Int = value.hashCode()
    }

    class FileProperty(val value: File) : Property() {
        override fun equals(other: Any?): Boolean =
                other is FileProperty && value == other.value

        override fun hashCode(): Int = value.hashCode()
    }

    class ColorProperty(val value: Color) : Property() {
        override fun equals(other: Any?): Boolean =
                other is ColorProperty && value == other.value

        override fun hashCode(): Int = value.hashCode()
    }
}
