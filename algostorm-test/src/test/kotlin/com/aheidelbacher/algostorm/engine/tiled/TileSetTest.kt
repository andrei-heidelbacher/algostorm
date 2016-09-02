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
import com.aheidelbacher.algostorm.engine.tiled.Properties.Color
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

class TileSetTest {
    val fileStream = FileInputStream(
            File("src/test/resources/testTileSet.json")
    )
    val tileSet = TileSet(
            name = "testTileSet",
            tileWidth = 24,
            tileHeight = 24,
            image = Properties.File("testTileSet.png"),
            imageWidth = 24 * 3,
            imageHeight = 24 * 2,
            columns = 3,
            tileCount = 6,
            margin = 0,
            spacing = 0,
            transparentColor = Color("#00000000")
    )

    @Test
    fun testTileSetDeserialization() {
        println(Serializer.readValue<TileSet>(fileStream))
    }

    @Test
    fun testTileSetSerialization() {
        val bos = ByteArrayOutputStream()
        val ts = Serializer.readValue<TileSet>(fileStream)
        Serializer.writeValue(bos, ts)
        println(bos.toString("UTF-8"))
    }

    sealed class A(val a: Int) {
        companion object {
            @JvmStatic @JsonCreator operator fun invoke(
                    a: Int,
                    b: Int? = null,
                    c: Int? = null
            ): A = if (b != null) B(a, b) else if (c != null) C(a, c) else
                throw IllegalArgumentException()
        }
        class B(a: Int, val b: Int): A(a)
        class C(a: Int, val c: Int): A(a)
    }

    @Test
    fun testABC() {
        val a = A.B(5, 6)
        val bos = ByteArrayOutputStream()
        Serializer.writeValue(bos, a)
        val bis = bos.toByteArray().inputStream()
        val desA = Serializer.readValue<A>(bis)
        println(desA.a)
        println(desA is A.B)
    }
}
