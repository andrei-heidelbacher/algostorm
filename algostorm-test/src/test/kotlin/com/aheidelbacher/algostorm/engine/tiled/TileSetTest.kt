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

package com.aheidelbacher.algostorm.engine.tiled

import org.junit.Assert.assertEquals
import org.junit.Test

import com.aheidelbacher.algostorm.engine.serialization.Serializer

import java.io.ByteArrayOutputStream
import java.io.FileInputStream

class TileSetTest {
    val fileStream = FileInputStream(
            java.io.File("src/test/resources/testTileSet.json")
    )
    val tileSet = tileSetOf(
            name = "world",
            tileWidth = 24,
            tileHeight = 24,
            image = File(
                    "D:/Dropbox/Private/Oryx tileset/algoventure/world.png"
            ),
            imageHeight = 240,
            imageWidth = 288,
            columns = 12,
            tileCount = 120,
            tiles = mapOf(
                    2 to tileOf(objectGroup = objectGroupOf(
                            name = "",
                            properties = mapOf(
                                    "z" to propertyOf("someString")
                            ),
                            objects = mutableListOf(objectOf(
                                    height = 24,
                                    width = 24,
                                    id = 1,
                                    name = "",
                                    type = "",
                                    properties = mapOf(
                                            "z" to propertyOf(5)
                                    ),
                                    x = 0,
                                    y = 0
                            ))
                    ))
            )
    )

    @Test
    fun testTileSetDeserialization() {
        val actualTileSet = Serializer.readValue<TileSet>(fileStream)
        assertEquals(tileSet, actualTileSet)
    }

    @Test
    fun testTileSetSerialization() {
        val bos = ByteArrayOutputStream()
        Serializer.writeValue(bos, tileSet)
        val actualTileSet = Serializer.readValue<TileSet>(
                src = bos.toByteArray().inputStream()
        )
        assertEquals(tileSet, actualTileSet)
    }
}