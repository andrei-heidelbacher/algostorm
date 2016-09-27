package com.aheidelbacher.algostorm.engine.tiled

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

import com.aheidelbacher.algostorm.engine.serialization.Serializer

import java.io.ByteArrayOutputStream
import java.io.FileInputStream

import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.memberProperties

class MapObjectTest {
    companion object {
        fun <T : Any> assertEquals(
                expected: T,
                actual: T,
                props: Iterable<T.() -> Any?>
        ) {
            props.forEach { prop -> assertEquals(expected.prop(), actual.prop()) }
        }

        inline fun <reified T : Any> assertPropsEquals(expected: T, actual: T) {
            val props = T::class.memberProperties.filter { it.isAccessible }
            assertEquals(expected, actual, props)
        }
    }

    val fileStream = FileInputStream(
            java.io.File("src/test/resources/testMapObject.json")
    )
    val mapObject = mapObjectOf(
            width = 2,
            height = 2,
            tileWidth = 24,
            tileHeight = 24,
            backgroundColor = Color("#FFFFFF5f"),
            tileSets = listOf(tileSetOf(
                    name = "world",
                    tileWidth = 24,
                    tileHeight = 24,
                    image = File("/world.png"),
                    columns = 2,
                    tileCount = 6,
                    imageWidth = 48,
                    imageHeight = 72
            )),
            layers = listOf(
                    imageLayerOf(name = "bg", image = File("/bg.png")),
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

    private fun assertMapObjectEquals(
            expectedMapObject: MapObject,
            actualMapObject: MapObject
    ) {
        expectedMapObject.layers.zip(actualMapObject.layers).forEach {
            val (expected, actual) = it
            when {
                expected is Layer.ImageLayer && actual is Layer.ImageLayer ->
                    assertPropsEquals(expected, actual)
                expected is Layer.ObjectGroup && actual is Layer.ObjectGroup ->
                    assertPropsEquals(expected, actual)
                expected is Layer.TileLayer && actual is Layer.TileLayer -> {
                    assertPropsEquals<Layer>(expected, actual)
                    assertArrayEquals(expected.data, actual.data)
                }
            }
        }
        assertEquals(
                expectedMapObject.getAndIncrementNextObjectId(),
                actualMapObject.getAndIncrementNextObjectId()
        )
    }

    @Test
    fun testMapObjectDeserialization() {
        val actualMapObject = Serializer.readValue<MapObject>(fileStream)
        assertPropsEquals(mapObject, actualMapObject)
        assertMapObjectEquals(mapObject, actualMapObject)
    }

    @Test
    fun testMapObjectSerialization() {
        val bos = ByteArrayOutputStream()
        Serializer.writeValue(bos, mapObject)
        val actualMapObject = Serializer.readValue<MapObject>(
                src = bos.toByteArray().inputStream()
        )
        assertMapObjectEquals(mapObject, actualMapObject)
    }
}
