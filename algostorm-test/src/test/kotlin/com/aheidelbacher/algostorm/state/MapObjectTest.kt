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
                expected is TileLayer && actual is TileLayer ->
                    assertEquals(expected.tiles, actual.tiles)
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
                inputStream = bos.toByteArray().inputStream()
        )
        assertMapObjectEquals(mapObject, actualMapObject)
    }
}
