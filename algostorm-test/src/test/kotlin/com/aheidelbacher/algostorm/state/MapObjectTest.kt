/*
 * Copyright (c) 2016  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.aheidelbacher.algostorm.state

import org.junit.Test

import com.aheidelbacher.algostorm.engine.serialization.Deserializer.Companion.readValue
import com.aheidelbacher.algostorm.engine.serialization.JsonDriver
import com.aheidelbacher.algostorm.state.Layer.EntityGroup
import com.aheidelbacher.algostorm.state.Layer.TileLayer
import com.aheidelbacher.algostorm.state.builders.entity
import com.aheidelbacher.algostorm.state.builders.entityGroup
import com.aheidelbacher.algostorm.state.builders.mapObject
import com.aheidelbacher.algostorm.state.builders.tileLayer
import com.aheidelbacher.algostorm.state.builders.tileSet

import java.io.ByteArrayOutputStream
import java.io.FileInputStream

import kotlin.test.assertEquals
import kotlin.test.fail

class MapObjectTest {
    private val jsonDriver = JsonDriver()
    private val fileStream = FileInputStream(
            java.io.File("src/test/resources/testMapObject.json")
    )
    private val mapObject = mapObject {
        width = 2
        height = 2
        tileWidth = 24
        tileHeight = 24
        backgroundColor = Color("#FFFFFF5f")
        +tileSet {
            name = "world"
            image("/world.png", 48, 72)
            tileWidth = 24
            tileHeight = 24
        }
        +tileLayer {
            name = "floor"
            data = IntArray(width * height) { 1 }
        }
        +entityGroup {
            name = "entities"
            +entity {
                +ComponentMock(1)
            }
        }
    }

    fun assertMapObjectEquals(expectedMap: MapObject, actualMap: MapObject) {
        assertEquals(expectedMap.width, actualMap.width)
        assertEquals(expectedMap.height, actualMap.height)
        assertEquals(expectedMap.tileWidth, actualMap.tileWidth)
        assertEquals(expectedMap.tileHeight, actualMap.tileHeight)
        assertEquals(expectedMap.backgroundColor, actualMap.backgroundColor)
        assertEquals(expectedMap.orientation, actualMap.orientation)
        assertEquals(expectedMap.renderOrder, actualMap.renderOrder)
        assertEquals(expectedMap.tileSets, actualMap.tileSets)
        assertEquals(expectedMap.layers, actualMap.layers)
        expectedMap.layers.zip(actualMap.layers).forEach {
            val (expected, actual) = it
            assertEquals(expected.isVisible, actual.isVisible)
            assertEquals(expected.offsetX, actual.offsetX)
            assertEquals(expected.offsetY, actual.offsetY)
            when {
                expected is EntityGroup && actual is EntityGroup ->
                    assertEquals(
                            expected = expected.entities.toSet(),
                            actual = actual.entities.toSet()
                    )
                expected is TileLayer && actual is TileLayer -> assertEquals(
                        expected = (0 until expected.size).map { expected[it] },
                        actual = (0 until actual.size).map { actual[it] }
                )
                else -> fail()
            }
        }
    }

    @Test
    fun testMapObjectDeserialization() {
        val actualMapObject = jsonDriver.readValue<MapObject>(fileStream)
        assertMapObjectEquals(mapObject, actualMapObject)
    }

    @Test
    fun testMapObjectSerialization() {
        val bos = ByteArrayOutputStream()
        jsonDriver.writeValue(bos, mapObject)
        val actualMapObject = jsonDriver.readValue<MapObject>(
                src = bos.toByteArray().inputStream()
        )
        assertMapObjectEquals(mapObject, actualMapObject)
    }
}
