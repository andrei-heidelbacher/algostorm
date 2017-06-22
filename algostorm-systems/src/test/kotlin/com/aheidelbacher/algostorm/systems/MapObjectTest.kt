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

package com.aheidelbacher.algostorm.systems

import org.junit.Test

import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.Color
import com.aheidelbacher.algostorm.core.drivers.io.Resource.Companion.resourceOf
import com.aheidelbacher.algostorm.core.ecs.EntityRef.Id
import com.aheidelbacher.algostorm.core.ecs.Prefab.Companion.prefabOf
import com.aheidelbacher.algostorm.core.ecs.Prefab.Companion.toPrefab
import com.aheidelbacher.algostorm.core.drivers.serialization.JsonDriver
import com.aheidelbacher.algostorm.systems.MapObject.Builder.Companion.mapObject
import com.aheidelbacher.algostorm.test.ecs.ComponentMock

import java.io.ByteArrayOutputStream

import kotlin.test.assertEquals

class MapObjectTest {
    companion object {
        fun assertEquals(expected: MapObject, actual: MapObject) {
            assertEquals(expected.width, actual.width)
            assertEquals(expected.height, actual.height)
            assertEquals(expected.tileWidth, actual.tileWidth)
            assertEquals(expected.tileHeight, actual.tileHeight)
            assertEquals(expected.backgroundColor, actual.backgroundColor)
            assertEquals(expected.tileSets, actual.tileSets)
            val expectedEntities = expected.entityPool.group.entities
                    .associate { it.id to it.toPrefab() }
            val actualEntities = actual.entityPool.group.entities
                    .associate { it.id to it.toPrefab() }
            assertEquals(expectedEntities, actualEntities)
        }
    }

    val serializationDriver = JsonDriver
    val inputStream = javaClass.getResourceAsStream("/mapObject.json")
    val mapObject = mapObject {
        width = 2
        height = 2
        tileWidth = 24
        tileHeight = 24
        backgroundColor = Color("#FFFFFF5f")
        tileSet(resourceOf("tileSet.json"))
        var id = 1
        for (x in 0..width - 1) {
            for (y in 0..height - 1) {
                entity(Id(id), prefabOf(ComponentMock(id)))
                id += 1
            }
        }
    }

    @Test fun testMapObjectDeserialization() {
        val actualMapObject = serializationDriver.deserialize<MapObject>(
                src = inputStream
        )
        assertEquals(mapObject, actualMapObject)
    }

    @Test fun testMapObjectSerialization() {
        val bos = ByteArrayOutputStream()
        println("hello")
        serializationDriver.serialize(bos, mapObject)
        println("hello2")
        val actualMapObject = serializationDriver.deserialize<MapObject>(
                src = bos.toByteArray().inputStream()
        )
        println("hello3")
        assertEquals(mapObject, actualMapObject)
    }
}
