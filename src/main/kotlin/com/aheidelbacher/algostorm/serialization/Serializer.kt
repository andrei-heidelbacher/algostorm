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

package com.aheidelbacher.algostorm.serialization

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.JsonGenerationException
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

import kotlin.reflect.KClass

/**
 * Serialization and deserialization utility methods.
 */
object Serializer {
    /**
     * The serialization format.
     */
    const val FORMAT: String = "json"

    /**
     * The object that handles serialization and deserialization.
     */
    @JvmField val objectMapper = jacksonObjectMapper().apply {
        enable(SerializationFeature.INDENT_OUTPUT)
        setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
        setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        enableDefaultTyping(
                ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT,
                JsonTypeInfo.As.PROPERTY
        )
    }

    @Throws(
            IOException::class,
            JsonGenerationException::class,
            JsonMappingException::class
    )
    @JvmStatic fun writeValue(out: OutputStream, value: Any) {
        objectMapper.writeValue(out, value)
    }

    @Throws(
            IOException::class,
            JsonGenerationException::class,
            JsonMappingException::class
    )
    @JvmStatic fun writeValueAsString(value: Any): String =
            objectMapper.writeValueAsString(value)

    @Throws(
            IOException::class,
            JsonParseException::class,
            JsonMappingException::class
    )
    @JvmStatic fun <T : Any> readValue(src: InputStream, type: KClass<T>): T =
            objectMapper.readValue(src, type.java)

    @Throws(
            IOException::class,
            JsonParseException::class,
            JsonMappingException::class
    )
    @JvmStatic fun <T : Any> readValue(src: String, type: KClass<T>): T =
            objectMapper.readValue(src, type.java)

    @Throws(
            IOException::class,
            JsonParseException::class,
            JsonMappingException::class
    )
    @JvmStatic inline fun <reified T : Any> readValue(src: InputStream): T =
            objectMapper.readValue(src)

    @Throws(
            IOException::class,
            JsonParseException::class,
            JsonMappingException::class
    )
    @JvmStatic inline fun <reified T : Any> readValue(src: String): T =
            objectMapper.readValue(src)
}
