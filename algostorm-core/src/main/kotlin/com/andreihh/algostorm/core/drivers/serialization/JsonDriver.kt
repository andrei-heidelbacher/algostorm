/*
 * Copyright (c) 2017  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.andreihh.algostorm.core.drivers.serialization

import com.andreihh.algostorm.core.drivers.graphics2d.Color
import com.andreihh.algostorm.core.drivers.io.Resource
import com.andreihh.algostorm.core.ecs.Component
import com.andreihh.algostorm.core.ecs.ComponentLibrary
import com.andreihh.algostorm.core.ecs.EntityRef.Id
import com.andreihh.algostorm.core.ecs.Prefab
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.KeyDeserializer
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import kotlin.reflect.KClass

/** A JSON serialization and deserialization driver. */
object JsonDriver {
    private fun <T : Any> serializer(
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

    private fun <T : Any> deserializer(
            deserialize: (JsonParser) -> T
    ): JsonDeserializer<T> = object : JsonDeserializer<T>() {
        override fun deserialize(
                p: JsonParser,
                ctxt: DeserializationContext?
        ): T = deserialize(p)
    }

    private fun keyDeserializer(
            deserialize: (String) -> Any?
    ): KeyDeserializer = object : KeyDeserializer() {
        override fun deserializeKey(
                key: String,
                ctxt: DeserializationContext?
        ): Any? = deserialize(key)
    }

    private val resourceSerializer = serializer<Resource<*>> { resource, gen ->
        gen.writeString(resource.uri)
    }

    private val resourceDeserializer = deserializer { p ->
        Resource<Any?>(p.codec.readValue<String>(p, String::class.java))
    }

    private val colorSerializer = serializer<Color> { value, gen ->
        gen.writeString("$value")
    }

    private val colorDeserializer = deserializer { p ->
        Color(p.codec.readValue<String>(p, String::class.java))
    }

    private val idSerializer = serializer<Id> { value, gen ->
        gen.writeNumber(value.value)
    }

    private val idDeserializer = deserializer { p ->
        Id(p.codec.readValue<Int>(p, Int::class.java))
    }

    private val idKeySerializer = serializer<Id> { value, gen ->
        gen.writeFieldName("$value")
    }

    private val idKeyDeserializer = keyDeserializer { key ->
        Id(key.toInt())
    }

    private val prefabSerializer = serializer<Prefab> { value, gen ->
        gen.writeStartObject()
        value.components.forEach { component ->
            val name = ComponentLibrary[component::class]
                    ?: throw JsonException("'$component' is not registered!")
            gen.writeFieldName(name)
            gen.writeObject(component)
        }
        gen.writeEndObject()
    }

    private val prefabDeserializer = deserializer { p ->
        val components = arrayListOf<Component>()
        p.codec.readTree<JsonNode>(p).fields().forEach { (name, value) ->
            val type = ComponentLibrary[name]
                    ?: throw JsonException("'$name' is not a component name!")
            val component = value.traverse(p.codec).readValueAs(type.java)
            components.add(component)
        }
        Prefab(components)
    }

    private val objectMapper = jacksonObjectMapper().apply {
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

    /**
     * Serializes the given object to the given stream.
     *
     * @param out the stream to which the given object is serialized
     * @param value the object which should be serialized
     * @throws JsonException if there are any serialization errors
     * @throws IOException if there were any output related errors
     */
    fun serialize(out: OutputStream, value: Any) {
        try {
            objectMapper.writeValue(out, value)
        } catch (e: JsonProcessingException) {
            throw JsonException(e)
        }
    }

    /**
     * Deserializes an object of the given `type` from the given stream.
     *
     * @param T the type of the deserialized object; must not be generic
     * @param src the stream from which the object is deserialized
     * @param type the class object of the deserialized object
     * @return the deserialized object
     * @throws JsonException if there are any deserialization errors
     * @throws IOException if there are any input related errors
     */
    fun <T : Any> deserialize(src: InputStream, type: KClass<T>): T = try {
        objectMapper.readValue(src, type.java)
    } catch (e: JsonProcessingException) {
        throw JsonException(e)
    }

    /** Utility deserialization method. */
    inline fun <reified T : Any> deserialize(src: InputStream): T =
            deserialize(src, T::class)
}

