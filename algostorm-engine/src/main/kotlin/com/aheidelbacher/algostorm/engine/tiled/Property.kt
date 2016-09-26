package com.aheidelbacher.algostorm.engine.tiled

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

import com.aheidelbacher.algostorm.engine.tiled.Property.BooleanProperty
import com.aheidelbacher.algostorm.engine.tiled.Property.ColorProperty
import com.aheidelbacher.algostorm.engine.tiled.Property.FileProperty
import com.aheidelbacher.algostorm.engine.tiled.Property.FloatProperty
import com.aheidelbacher.algostorm.engine.tiled.Property.IntProperty
import com.aheidelbacher.algostorm.engine.tiled.Property.StringProperty

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
    class IntProperty(val value: Int) : Property()

    class FloatProperty(val value: Float) : Property()

    class BooleanProperty(val value: Boolean) : Property()

    class StringProperty(val value: String) : Property()

    class FileProperty(val value: File) : Property()

    class ColorProperty(val value: Color) : Property()
}
