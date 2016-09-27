package com.aheidelbacher.algostorm.engine.tiled

import org.junit.Assert.assertEquals
import org.junit.Test

import com.aheidelbacher.algostorm.engine.serialization.Serializer
import java.io.ByteArrayOutputStream

import java.io.FileInputStream

class MapObjectTest {
    val fileStream = FileInputStream(
            java.io.File("src/test/resources/testMapObject.json")
    )
    val mapObject = mapObjectOf(
            width = 2,
            height = 2,
            tileWidth = 24,
            tileHeight = 24,
            tileSets = listOf(
                    tileSetOf(
                            name = "world",
                            tileWidth = 24,
                            tileHeight = 24,
                            image = File("/world.png"),
                            columns = 2,
                            tileCount = 6,
                            imageWidth = 48,
                            imageHeight = 72
                    )
            ),
            layers = listOf(
                    imageLayerOf(
                            name = "bg",
                            image = File("/bg.png")
                    ),
                    tileLayerOf(
                            name = "floor",
                            data = LongArray(2 * 2) { 1 },
                            properties = mapOf("collide" to propertyOf(false))
                    ),
                    objectGroupOf(
                            name = "objects",
                            objects = arrayListOf(objectOf(
                                    id = 1,
                                    name = "",
                                    type = "",
                                    x = 0,
                                    y = 0,
                                    width = 24,
                                    height = 24,
                                    gid = 1,
                                    properties = mapOf(
                                            "z" to propertyOf(2),
                                            "sound" to propertyOf(File("s.mp3"))
                                    )
                            ))
                    )
            ),
            properties = mapOf("time" to propertyOf(0.0F)),
            nextObjectId = 2
    )

    private fun assertEquals(expected: MapObject, actual: MapObject) {
        fun assertEquals() = 0
        assertEquals(expected.width, actual.width)
    }

    @Test
    fun testMapObjectDeserialization() {
        val actualMapObject = Serializer.readValue<MapObject>(fileStream)
        assertEquals(mapObject, actualMapObject)
    }

    @Test
    fun testMapObjectSerialization() {
        val bos = ByteArrayOutputStream()
        Serializer.writeValue(bos, mapObject)
        val actualMapObject = Serializer.readValue<MapObject>(
                src = bos.toByteArray().inputStream()
        )
        assertEquals(mapObject, actualMapObject)
    }
}
