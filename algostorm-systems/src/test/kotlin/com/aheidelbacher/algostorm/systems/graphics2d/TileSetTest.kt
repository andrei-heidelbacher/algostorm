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

package com.aheidelbacher.algostorm.systems.graphics2d

import org.junit.Test

import com.aheidelbacher.algostorm.core.drivers.io.Resource.Companion.resourceOf
import com.aheidelbacher.algostorm.core.drivers.serialization.JsonDriver
import com.aheidelbacher.algostorm.systems.graphics2d.TileSet.Frame
import com.aheidelbacher.algostorm.systems.graphics2d.TileSet.Image
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

class TileSetTest {
    private val name = "world"
    private val image = Image(resourceOf("image.png"), 288, 240)
    private val tileWidth = 24
    private val tileHeight = 24
    private val animations = mapOf("tile:idle" to listOf(
            Frame(tileId = 1, duration = 250),
            Frame(tileId = 2, duration = 250))
    )
    private val tileSet = TileSet(
            name = name,
            image = image,
            tileWidth = tileWidth,
            tileHeight = tileHeight,
            animations = animations
    )
    private val tileSetSrc =
            javaClass.classLoader.getResourceAsStream("tileSet.json")

    @Test(expected = IllegalArgumentException::class)
    fun testNegativeTileWidthShouldThrow() {
        TileSet(
                name = name,
                image = image,
                tileWidth = -1,
                tileHeight = tileHeight,
                animations = animations
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testNegativeTileHeightShouldThrow() {
        TileSet(
                name = name,
                image = image,
                tileWidth = tileWidth,
                tileHeight = -1,
                animations = animations
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testEmptyAnimationShouldThrow() {
        TileSet(
                name = name,
                image = image,
                tileWidth = tileWidth,
                tileHeight = tileHeight,
                animations = mapOf("tile:idle" to emptyList())
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidFrameIdShouldThrow() {
        TileSet(
                name = name,
                image = image,
                tileWidth = tileWidth,
                tileHeight = tileHeight,
                animations = mapOf("tile:idle" to listOf(Frame(1000, 100)))
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testNegativeMarginShouldThrow() {
        TileSet(
                name = name,
                image = image,
                tileWidth = tileWidth,
                tileHeight = tileHeight,
                animations = animations,
                margin = -1
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testNegativeSpacingShouldThrow() {
        TileSet(
                name = name,
                image = image,
                tileWidth = tileWidth,
                tileHeight = tileHeight,
                animations = animations,
                spacing = -1
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidDimensionsShouldThrow() {
        TileSet(
                name = name,
                image = image,
                tileWidth = tileWidth + 1,
                tileHeight = tileHeight,
                animations = animations
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidDimensionsShouldThrow2() {
        TileSet(
                name = name,
                image = image,
                tileWidth = tileWidth,
                tileHeight = tileHeight + 1,
                animations = animations
        )
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun testGetViewportWithInvalidTileIdShouldThrow() {
        TileSet(
                name = name,
                image = image,
                tileWidth = tileWidth,
                tileHeight = tileHeight,
                animations = animations
        ).getViewport(139)
    }

    @Test fun testDeserialize() {
        val actualTileSet = JsonDriver.deserialize<TileSet>(tileSetSrc)
        assertEquals(tileSet, actualTileSet)
    }

    @Test fun testSerializeAndDeserialize() {
        val bos = ByteArrayOutputStream()
        JsonDriver.serialize(bos, tileSet)
        val actualTileSet =
                JsonDriver.deserialize<TileSet>(bos.toByteArray().inputStream())
        assertEquals(tileSet, actualTileSet)
    }
}
