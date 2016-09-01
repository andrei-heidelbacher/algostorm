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

package com.aheidelbacher.algostorm.engine.serialization

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

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
    private val objectMapper = jacksonObjectMapper().apply {
        enable(SerializationFeature.INDENT_OUTPUT)
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
        setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        enableDefaultTyping(
                ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT,
                JsonTypeInfo.As.PROPERTY
        )
    }

    @Throws(IOException::class)
    @JvmStatic fun writeValue(out: OutputStream, value: Any) {
        objectMapper.writeValue(out, value)
    }

    @Throws(IOException::class)
    @JvmStatic fun <T : Any> readValue(src: InputStream, type: Class<T>): T =
            objectMapper.readValue(src, type)

    @Throws(IOException::class)
    @JvmStatic fun <T : Any> readValue(src: InputStream, type: KClass<T>): T =
            readValue(src, type.java)

    @Throws(IOException::class)
    @JvmStatic fun <T : Any> readValue(
            src: InputStream,
            typeReference: TypeReference<T>
    ): T = objectMapper.readValue(src, typeReference)

    @Throws(IOException::class)
    @JvmStatic inline fun <reified T : Any> readValue(src: InputStream): T =
            readValue(src, object : TypeReference<T>() {})
}
