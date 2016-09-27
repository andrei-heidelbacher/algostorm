package com.aheidelbacher.algostorm.engine.tiled

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

import com.aheidelbacher.algostorm.engine.serialization.Serializer

import java.io.ByteArrayOutputStream
import java.io.FileInputStream

import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.memberProperties

class MapObjectTest {
    val fileStream = FileInputStream(
            java.io.File("src/test/resources/testMapObject.json")
    )
    val mapObject = mapObjectOf(
            width = 2,
            height = 2,
            tileWidth = 24,
            tileHeight = 24,
            backgroundColor = Color("#FFFFFF5f"),
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

    private fun <T : Any> assertEquals(
            expected: T,
            actual: T,
            vararg props: T.() -> Any?
    ) {
        props.forEach { prop -> println(expected.prop()); assertEquals(expected.prop(), actual.prop()) }
    }

    private inline fun <reified T : Any> assertPropertyEquals(
            expected: T,
            actual: T
    ) {
        assertEquals(
                expected,
                actual,
                *T::class.memberProperties
                        .filter(KProperty1<T, *>::isAccessible)
                        .toTypedArray()
        )
    }

    private fun assertMapObjectEquals(
            expectedMapObject: MapObject,
            actualMapObject: MapObject
    ) {
        expectedMapObject.layers.zip(actualMapObject.layers).forEach {
            val (expected, actual) = it
            when {
                expected is Layer.ImageLayer && actual is Layer.ImageLayer ->
                    assertPropertyEquals(expected, actual)
                expected is Layer.ObjectGroup && actual is Layer.ObjectGroup ->
                    assertPropertyEquals(expected, actual)
                expected is Layer.TileLayer && actual is Layer.TileLayer -> {
                    assertPropertyEquals<Layer>(expected, actual)
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
        assertPropertyEquals(mapObject, actualMapObject)
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
