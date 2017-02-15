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

package com.aheidelbacher.algostorm.core.engine.graphics2d

import org.junit.Test

import com.aheidelbacher.algostorm.core.engine.driver.Resource.Companion.resourceOf
import com.aheidelbacher.algostorm.core.engine.graphics2d.TileSet.Frame
import com.aheidelbacher.algostorm.core.engine.graphics2d.TileSet.Image

import java.io.IOException

import kotlin.test.assertEquals

class TileSetTest {
    @Test fun testLoadTileSet() {
        assertEquals(
                expected = TileSet(
                        name = "world",
                        tileWidth = 24,
                        tileHeight = 24,
                        image = Image(resourceOf("/data.json"), 288, 240),
                        animations = mapOf("tile:idle" to listOf(
                                Frame(1, 250),
                                Frame(2, 250)
                        ))
                ),
                actual = TileSet.load(resourceOf("/tileSet.json"))
        )
    }

    @Test(expected = IOException::class)
    fun testLoadInvalidTileSetThrows() {
        TileSet.load(resourceOf("/invalidTileSet.json"))
    }
}
