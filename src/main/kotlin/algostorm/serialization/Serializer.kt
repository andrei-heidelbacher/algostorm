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
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

import algostorm.assets.FontSet
import algostorm.assets.TileSet
import algostorm.ecs.Component
import algostorm.ecs.EntityId
import algostorm.event.Event

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
    enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
  }

  @JvmStatic fun writeComponent(out: OutputStream, component: Component) {
    objectMapper.writeValue(out, component)
  }

  @JvmStatic fun <T : Component> readComponent(src: InputStream, type: KClass<T>): T =
      objectMapper.readValue(src, type.java)

  @JvmStatic inline fun <reified T : Component> readComponent(src: InputStream): T =
      readComponent(src, T::class)

  @JvmStatic fun writeComponents(out: OutputStream, components: List<Component>) {
    objectMapper.writeValue(out, components)
  }

  @JvmStatic fun readComponents(src: InputStream): List<Component> =
      objectMapper.readValue(src, object : TypeReference<List<Component>>() {})

  @JvmStatic fun writeEntities(out: OutputStream, entities: Map<EntityId, List<Component>>) {
    objectMapper.writeValue(out, entities)
  }

  @JvmStatic fun readEntitiesWithIds(src: InputStream): Map<EntityId, List<Component>> =
      objectMapper.readValue(src, object : TypeReference<Map<EntityId, List<Component>>>() {})

  @JvmStatic fun writeEntities(out: OutputStream, entities: List<List<Component>>) {
    objectMapper.writeValue(out, entities)
  }

  @JvmStatic fun readEntities(src: InputStream): List<List<Component>> =
      objectMapper.readValue(src, object : TypeReference<List<List<Component>>>() {})

  @JvmStatic fun writeEvent(out: OutputStream, event: Event)  {
    objectMapper.writeValue(out, event)
  }

  @JvmStatic fun <T : Event> readEvent(src: InputStream, type: KClass<T>): T =
      objectMapper.readValue(src, type.java)

  @JvmStatic inline fun <reified T : Event> readEvent(src: InputStream): T =
      readEvent(src, T::class)

  @JvmStatic fun writeTileSets(out: OutputStream, tileSets: List<TileSet>) {
    objectMapper.writeValue(out, tileSets)
  }

  @JvmStatic fun readTileSets(src: InputStream): List<TileSet> =
      objectMapper.readValue(src, object : TypeReference<List<TileSet>>() {})

  @JvmStatic fun writeFontSet(out: OutputStream, fontSet: FontSet) {
    objectMapper.writeValue(out, fontSet)
  }

  @JvmStatic fun readFontSet(src: InputStream): FontSet =
      objectMapper.readValue(src, FontSet::class.java)

  @JvmStatic fun <T : Any> writeValue(out: OutputStream, value: T) {
    objectMapper.writeValue(out, value)
  }

  @JvmStatic fun <T : Any> readValue(src: InputStream, type: KClass<T>): T =
      objectMapper.readValue(src, type.java)

  @JvmStatic inline fun <reified T : Any> readValue(src: InputStream): T =
      readValue(src, T::class)
}
