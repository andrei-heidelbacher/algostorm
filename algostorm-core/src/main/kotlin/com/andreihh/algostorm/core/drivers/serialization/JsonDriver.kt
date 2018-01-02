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
import com.andreihh.algostorm.core.ecs.EntityPool
import com.andreihh.algostorm.core.ecs.EntityRef.Id
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.KeyDeserializer
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTypeResolverBuilder
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE
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

    private val entityPoolSerializer = serializer<EntityPool> { value, gen ->
        gen.writeStartObject()
        for (entity in value) {
            gen.writeFieldName("${entity.id.value}")
            gen.writeStartArray()
            entity.components.forEach(gen::writeObject)
            gen.writeEndArray()
        }
        gen.writeEndObject()
    }

    private val entityPoolDeserializer = deserializer { p ->
        val entities = hashMapOf<Id, Collection<Component>>()
        for ((id, components) in p.codec.readTree<JsonNode>(p).fields()) {
            val cp = components.traverse(p.codec)
            val entityComponents = arrayListOf<Component>()
            for (component in cp.codec.readTree<JsonNode>(cp).elements()) {
                entityComponents += component.traverse(cp.codec)
                    .readValueAs(Component::class.java)
            }
            entities[Id(id.toInt())] = entityComponents
        }
        EntityPool.of(entities)
    }

    private val typeResolver =
        object : DefaultTypeResolverBuilder(OBJECT_AND_NON_CONCRETE) {
            override fun useForType(t: JavaType): Boolean =
                Component::class.java.isAssignableFrom(t.rawClass)
        }.init(JsonTypeInfo.Id.CLASS, null)
            .inclusion(JsonTypeInfo.As.PROPERTY)
            .typeProperty("@class")

    private val objectMapper = jacksonObjectMapper().apply {
        enable(SerializationFeature.INDENT_OUTPUT)
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
        setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        setDefaultTyping(typeResolver)
        registerModule(SimpleModule().apply {
            addSerializer(Resource::class.java, resourceSerializer)
            addDeserializer(Resource::class.java, resourceDeserializer)
            addSerializer(Color::class.java, colorSerializer)
            addDeserializer(Color::class.java, colorDeserializer)
            addSerializer(Id::class.java, idSerializer)
            addDeserializer(Id::class.java, idDeserializer)
            addKeySerializer(Id::class.java, idKeySerializer)
            addKeyDeserializer(Id::class.java, idKeyDeserializer)
            addSerializer(EntityPool::class.java, entityPoolSerializer)
            addDeserializer(EntityPool::class.java, entityPoolDeserializer)
        })
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

