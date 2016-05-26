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

package algostorm.serialization

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.JsonGenerationException
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

import algostorm.assets.AssetCollection
import algostorm.assets.Sound
import algostorm.assets.TileSet
import algostorm.ecs.Component
import algostorm.event.Event

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

    private val objectMapper: ObjectMapper = jacksonObjectMapper().apply {
        enable(SerializationFeature.INDENT_OUTPUT)
        setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
        setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        enableDefaultTyping(
                ObjectMapper.DefaultTyping.NON_FINAL,
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
            JsonParseException::class,
            JsonMappingException::class
    )
    @JvmStatic fun readComponents(
            src: InputStream
    ): List<Component> = objectMapper.readValue(
            src,
            object : TypeReference<List<Component>>() {}
    )

    @Throws(
            IOException::class,
            JsonParseException::class,
            JsonMappingException::class
    )
    @JvmStatic fun readEntitiesWithIds(
            src: InputStream
    ): Map<Int, List<Component>> = objectMapper.readValue(
            src,
            object : TypeReference<Map<Int, List<Component>>>() {}
    )

    @Throws(
            IOException::class,
            JsonParseException::class,
            JsonMappingException::class
    )
    @JvmStatic fun readEntities(
            src: InputStream
    ): List<List<Component>> = objectMapper.readValue(
            src,
            object : TypeReference<List<List<Component>>>() {}
    )

    @Throws(
            IOException::class,
            JsonParseException::class,
            JsonMappingException::class
    )
    @JvmStatic fun <T : Event> readEvent(src: InputStream, type: KClass<T>): T =
            objectMapper.readValue(src, type.java)

    @Throws(
            IOException::class,
            JsonParseException::class,
            JsonMappingException::class
    )
    @JvmStatic inline fun <reified T : Event> readEvent(src: InputStream): T =
            readEvent(src, T::class)

    @Throws(
            IOException::class,
            JsonParseException::class,
            JsonMappingException::class
    )
    @JvmStatic fun readTileSets(
            src: InputStream
    ): List<TileSet> = objectMapper.readValue(
            src,
            object : TypeReference<List<TileSet>>() {}
    )

    @Throws(
            IOException::class,
            JsonParseException::class,
            JsonMappingException::class
    )
    @JvmStatic fun readSoundCollection(
            src: InputStream
    ): AssetCollection<Sound> = objectMapper.readValue(
            src,
            object : TypeReference<AssetCollection<Sound>>() {}
    )
}
