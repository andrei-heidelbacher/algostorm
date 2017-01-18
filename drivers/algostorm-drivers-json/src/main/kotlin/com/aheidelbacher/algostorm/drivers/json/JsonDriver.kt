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

package com.aheidelbacher.algostorm.drivers.json

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.KeyDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

import com.aheidelbacher.algostorm.core.ecs.Component
import com.aheidelbacher.algostorm.core.ecs.EntityRef.Id
import com.aheidelbacher.algostorm.core.ecs.Prefab
import com.aheidelbacher.algostorm.core.ecs.Prefab.Companion.prefabOf
import com.aheidelbacher.algostorm.core.engine.driver.Resource
import com.aheidelbacher.algostorm.core.engine.graphics2d.Color
import com.aheidelbacher.algostorm.core.engine.serialization.SerializationDriver

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

import kotlin.reflect.KClass

/** A JSON serialization driver using the Jackson external library. */
class JsonDriver : SerializationDriver {
    private companion object {
        fun <T : Any> serializer(
                serialize: (T, JsonGenerator) -> Unit
        ): JsonSerializer<T> = object : JsonSerializer<T>() {
            override fun serialize(
                    value: T,
                    gen: JsonGenerator,
                    serializers: SerializerProvider?
            ) {
                serialize(value, gen)
            }
        }

        fun <T : Any> deserializer(
                deserialize: (JsonParser) -> T?
        ): JsonDeserializer<T> = object : JsonDeserializer<T>() {
            override fun deserialize(
                    p: JsonParser,
                    ctxt: DeserializationContext?
            ): T? = deserialize(p)
        }

        fun keyDeserializer(
                deserialize: (String?) -> Any?
        ): KeyDeserializer = object : KeyDeserializer() {
            override fun deserializeKey(
                    key: String?,
                    ctxt: DeserializationContext?
            ): Any? = deserialize(key)
        }
    }

    override val format: String
        get() = "json"

    private val resourceSerializer = serializer<Resource> { value, gen ->
        gen.writeString("$value")
    }

    private val resourceDeserializer = deserializer { p ->
        p.codec.readValue<String>(p, String::class.java)?.let(::Resource)
    }

    private val colorSerializer = serializer<Color> { value, gen ->
        gen.writeString("$value")
    }

    private val colorDeserializer = deserializer { p ->
        p.codec.readValue<String>(p, String::class.java)?.let(::Color)
    }

    private val idSerializer = serializer<Id> { value, gen ->
        gen.writeNumber(value.value)
    }

    private val idDeserializer = deserializer { p ->
        p.codec.readValue<Int>(p, Int::class.java)?.let(::Id)
    }

    private val idKeySerializer = serializer<Id> { value, gen ->
        gen.writeFieldName("$value")
    }

    private val idKeyDeserializer = keyDeserializer { key ->
        key?.toInt()?.let(::Id)
    }

    private val prefabSerializer = serializer<Prefab> { value, gen ->
        gen.writeStartObject()
        value.components.forEachIndexed { i, component ->
            gen.writeFieldName(component.javaClass.canonicalName)
            gen.writeObject(component)
        }
        gen.writeEndObject()
    }

    private val prefabDeserializer = deserializer { p ->
        val components = arrayListOf<Component>()
        p.codec.readTree<JsonNode>(p)?.fields()?.forEach {
            val type = Class.forName(it.key)
            val component = it.value.traverse(p.codec).readValueAs(type)
            components.add(component as Component)
        }
        prefabOf(*components.toTypedArray())
    }

    private var objectMapper: ObjectMapper? = jacksonObjectMapper().apply {
        registerModule(SimpleModule().apply {
            addSerializer(Resource::class.java, resourceSerializer)
            addDeserializer(Resource::class.java, resourceDeserializer)
            addSerializer(Color::class.java, colorSerializer)
            addDeserializer(Color::class.java, colorDeserializer)
            addSerializer(Id::class.java, idSerializer)
            addDeserializer(Id::class.java, idDeserializer)
            addKeySerializer(Id::class.java, idKeySerializer)
            addKeyDeserializer(Id::class.java, idKeyDeserializer)
            addSerializer(Prefab::class.java, prefabSerializer)
            addDeserializer(Prefab::class.java, prefabDeserializer)
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

    override fun release() {
        objectMapper = null
    }
}

