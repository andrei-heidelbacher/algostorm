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
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/** A JSON serialization driver using the Jackson external library. */
class JsonDriver : SerializationDriver {
    companion object {
        /** The serialization format. */
        const val FORMAT: String = "json"
    }

    /** The object that handles serialization and deserialization. */
    private var objectMapper: ObjectMapper? = jacksonObjectMapper().apply {
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
    override fun <T : Any> readValue(
            inputStream: InputStream,
            type: Class<T>
    ): T = objectMapper?.readValue(inputStream, type)
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
