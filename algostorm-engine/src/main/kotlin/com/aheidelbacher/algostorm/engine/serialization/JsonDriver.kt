/*
 * Copyright (c) 2016  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aheidelbacher.algostorm.engine.serialization

import com.aheidelbacher.algostorm.engine.driver.Resource
import com.aheidelbacher.algostorm.engine.graphics2d.Color
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

import kotlin.reflect.KClass

/** A JSON serialization driver using the Jackson external library. */
class JsonDriver : SerializationDriver {
    companion object {
        /** The serialization format. */
        const val FORMAT: String = "json"
    }

    private val resourceSerializer = object : StdSerializer<Resource>(
            Resource::class.java
    ) {
        override fun serialize(
                value: Resource?,
                gen: JsonGenerator?,
                provider: SerializerProvider?
        ) {
            gen?.writeString(value?.path)
        }
    }

    private val resourceDeserializer = object : StdDeserializer<Resource>(
            Resource::class.java
    ) {
        override fun deserialize(
                p: JsonParser?,
                ctxt: DeserializationContext?
        ): Resource? {
            val path = p?.codec?.readValue<String>(p, String::class.java)
            return if (path != null) Resource(path) else null
        }
    }

    private val colorSerializer = object : StdSerializer<Color>(
            Color::class.java
    ) {
        override fun serialize(
                value: Color?,
                gen: JsonGenerator?,
                provider: SerializerProvider?
        ) {
            gen?.writeString("$value")
        }
    }

    private val colorDeserializer = object : StdDeserializer<Color>(
            Color::class.java
    ) {
        override fun deserialize(
                p: JsonParser?,
                ctxt: DeserializationContext?
        ): Color? {
            val colorCode = p?.codec?.readValue<String>(p, String::class.java)
            return if (colorCode != null) Color(colorCode) else null
        }
    }

    /** The object that handles serialization and deserialization. */
    private var objectMapper: ObjectMapper? = jacksonObjectMapper().apply {
        registerModule(SimpleModule().apply {
            addSerializer(Resource::class.java, resourceSerializer)
            addDeserializer(Resource::class.java, resourceDeserializer)
            addSerializer(Color::class.java, colorSerializer)
            addDeserializer(Color::class.java, colorDeserializer)
        })
        enable(SerializationFeature.INDENT_OUTPUT)
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
        setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
    }

    @Throws(IOException::class)
    override fun writeValue(out: OutputStream, value: Any) {
        objectMapper?.writeValue(out, value) ?: error("$this was released!")
    }

    @Throws(IOException::class)
    override fun <T : Any> readValue(src: InputStream, type: KClass<T>): T =
            objectMapper?.readValue(src, type.java)
                    ?: error("$this was released!")

    /**
     * Releases all resources acquired by this driver.
     *
     * Invoking any method after this driver was released will throw an
     * [IllegalStateException].
     */
    override fun release() {
        objectMapper = null
    }
}
