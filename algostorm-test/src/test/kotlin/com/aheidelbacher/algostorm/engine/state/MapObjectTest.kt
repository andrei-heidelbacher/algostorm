package com.aheidelbacher.algostorm.engine.state

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

import com.aheidelbacher.algostorm.engine.serialization.Serializer

import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.lang.reflect.Modifier

import kotlin.reflect.jvm.javaGetter
import kotlin.reflect.memberProperties

class MapObjectTest {
    companion object {
        fun <T : Any> assertEquals(
                expected: T,
                actual: T,
                props: Iterable<T.() -> Any?>
        ) {
            props.forEach { prop ->
                assertEquals(expected.prop(), actual.prop())
            }
        }

        inline fun <reified T : Any> assertPropsEquals(expected: T, actual: T) {
            val props = T::class.memberProperties.filter {
                it.javaGetter?.modifiers?.let(Modifier::isPublic) ?: false
            }
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
                    image = Image(File("/world.png"), 48, 72),
                    columns = 2,
                    tileCount = 6
            )),
            layers = listOf(
                    imageLayerOf(
                            name = "bg",
                            image = Image(File("/bg.png"), 24, 24)
                    ),
                    tileLayerOf(
                            name = "floor",
                            data = LongArray(2 * 2) { 1 },
                            properties = mapOf("collider" to false)
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
                                            "z" to 2,
                                            "sound" to "s.mp3"
                                    )
                            ))
                    )
            ),
            properties = mapOf("time" to 0),
            nextObjectId = 2
    )

    private fun assertMapObjectEquals(
            expectedMapObject: MapObject,
            actualMapObject: MapObject
    ) {
        assertPropsEquals(expectedMapObject, actualMapObject)
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
