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

package com.andreihh.algostorm.systems

import com.andreihh.algostorm.core.drivers.graphics2d.Color
import com.andreihh.algostorm.core.drivers.serialization.JsonDriver
import com.andreihh.algostorm.core.ecs.Component
import com.andreihh.algostorm.core.ecs.EntityRef.Id
import com.andreihh.algostorm.systems.MapObject.Builder.Companion.mapObject
import org.junit.Test
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
            val expectedEntities = expected.entities
                    .associate { it.id to it.components.toSet() }
            val actualEntities = actual.entities
                    .associate { it.id to it.components.toSet() }
            assertEquals(expectedEntities, actualEntities)
        }
    }

    data class ComponentMock(val id: Int) : Component

    private val serializationDriver = JsonDriver
    private val inputStream = javaClass.getResourceAsStream("/mapObject.json")
    private val mapObject = mapObject {
        width = 2
        height = 2
        tileWidth = 24
        tileHeight = 24
        backgroundColor = Color("#FFFFFF5f")

        tileSet {
            name = "world"
            image {
                source = "res:///image.png"
                width = 288
                height = 240
            }
            tileWidth = 24
            tileHeight = 24
            animation(name = "tile:idle") {
                frame(tileId = 1, duration = 250)
                frame(tileId = 2, duration = 250)
            }
        }

        var id = 1
        for (x in 0 until width) {
            for (y in 0 until height) {
                entity(Id(id)) {
                    +ComponentMock(id)
                }
                id += 1
            }
        }
    }

    /*@Test fun testTileSetCollection() {
        val tileSets = mapObject.tileSets
        val bos = ByteArrayOutputStream()
        serializationDriver.serialize(bos, tileSets)
        System.out.println(String(bos.toByteArray()))
        val tileSets2 = serializationDriver
                .deserialize<TileSetCollection>(bos.toByteArray().inputStream())
        assertEquals(tileSets, tileSets2)
    }*/

    @Test fun testMapObjectDeserialization() {
        val actualMapObject = serializationDriver.deserialize<MapObject>(
                src = inputStream
        )
        assertEquals(mapObject, actualMapObject)
    }

    @Test fun testMapObjectSerialization() {
        val bos = ByteArrayOutputStream()
        serializationDriver.serialize(bos, mapObject)
        val actualMapObject = serializationDriver.deserialize<MapObject>(
                src = bos.toByteArray().inputStream()
        )
        assertEquals(mapObject, actualMapObject)
    }
}
