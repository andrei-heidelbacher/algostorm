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

package com.aheidelbacher.algostorm.data

import org.junit.Test

import com.aheidelbacher.algostorm.data.MapObject.Builder.Companion.mapObject
import com.aheidelbacher.algostorm.data.TileSet.Builder.Companion.tileSet
import com.aheidelbacher.algostorm.drivers.json.JsonDriver
import com.aheidelbacher.algostorm.ecs.EntityRef.Id
import com.aheidelbacher.algostorm.ecs.Prefab.Companion.prefabOf
import com.aheidelbacher.algostorm.ecs.Prefab.Companion.toPrefab
import com.aheidelbacher.algostorm.engine.driver.Resource
import com.aheidelbacher.algostorm.engine.driver.Resource.Companion.SCHEMA
import com.aheidelbacher.algostorm.engine.graphics2d.Color
import com.aheidelbacher.algostorm.engine.serialization.Deserializer.Companion.readValue
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
            assertEquals(expected.tileSetCollection, actual.tileSetCollection)
            val expectedEntities = expected.entityPool.group.entities
                    .associate { it.id to it.toPrefab() }
            val actualEntities = actual.entityPool.group.entities
                    .associate { it.id to it.toPrefab() }
            assertEquals(expectedEntities, actualEntities)
        }
    }

    val serializationDriver = JsonDriver()
    val inputStream =
            Resource("$SCHEMA/mapObject.${serializationDriver.format}")
                    .inputStream()
    val mapObject = mapObject {
        width = 2
        height = 2
        tileWidth = 24
        tileHeight = 24
        backgroundColor = Color("#FFFFFF5f")
        +tileSet {
            name = "world"
            image(Resource("$SCHEMA/image.png"), 48, 72)
            tileWidth = 24
            tileHeight = 24
        }
        var id = 1
        for (x in 0..width - 1) {
            for (y in 0..height - 1) {
                entity(Id(id), prefabOf(ComponentMock(id)))
                id += 1
            }
        }
    }

    @Test fun testMapObjectDeserialization() {
        val actualMapObject = serializationDriver.readValue<MapObject>(inputStream)
        assertEquals(mapObject, actualMapObject)
    }

    @Test fun testMapObjectSerialization() {
        val bos = ByteArrayOutputStream()
        serializationDriver.writeValue(bos, mapObject)
        val actualMapObject = serializationDriver.readValue<MapObject>(
                src = bos.toByteArray().inputStream()
        )
        assertEquals(mapObject, actualMapObject)
    }
}
