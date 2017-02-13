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

import com.aheidelbacher.algostorm.core.engine.driver.Resource
import com.aheidelbacher.algostorm.core.engine.driver.Resource.Companion.SCHEMA
import com.aheidelbacher.algostorm.core.engine.serialization.Deserializer.Companion.readValue
//import com.aheidelbacher.algostorm.drivers.json.JsonDriver
import com.aheidelbacher.algostorm.core.engine.graphics2d.TileSet.Frame

import java.io.ByteArrayOutputStream

import kotlin.test.assertEquals

class TileSetTest {
    /*val serializationDriver = com.aheidelbacher.algostorm.drivers.json.JsonDriver()
    val inputStream = Resource("$SCHEMA/tileSet.${serializationDriver.format}")
            .inputStream()*/
    val tileSet = TileSet(
            name = "world",
            tileWidth = 24,
            tileHeight = 24,
            image = TileSet.Image(Resource("$SCHEMA/image.png"), 288, 240),
            animations = mapOf(
                    "tile:idle" to listOf(Frame(1, 250), Frame(2, 250))
            )
    )

    /*@Test fun testTileSetDeserialization() {
        val actualTileSet = serializationDriver.readValue<TileSet>(inputStream)
        assertEquals(tileSet, actualTileSet)
    }

    @Test fun testTileSetSerialization() {
        val bos = ByteArrayOutputStream()
        serializationDriver.writeValue(bos, tileSet)
        val actualTileSet = serializationDriver.readValue<TileSet>(
                src = bos.toByteArray().inputStream()
        )
        assertEquals(tileSet, actualTileSet)
    }*/
}
