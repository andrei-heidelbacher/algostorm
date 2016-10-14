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

package com.aheidelbacher.algostorm.engine.serialization

import org.junit.Assert.assertEquals
import org.junit.Test

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

class SerializerTest {
    val ext = JsonDriver.FORMAT
    val fileStream = FileInputStream(File("src/test/resources/testData.$ext"))
    val testDataMock = TestDataMock(
            primitiveTestField = 1,
            innerTestData = TestDataMock.InnerTestDataMock("non-empty"),
            testProperties = mapOf(
                    "inner" to TestDataMock.InnerTestDataMock(""),
                    "number" to 5,
                    "string" to "five"
            ),
            testList = listOf(1, 2, 3, 4, 5),
            defaultPrimitiveTestField = 1.5F
    )

    @Test
    fun testDataInlineDeserialization() {
        assertEquals(
                testDataMock,
                JsonDriver.readValue<TestDataMock>(fileStream)
        )
    }

    @Test
    fun testDataDeserialization() {
        assertEquals(
                testDataMock,
                JsonDriver.readValue(fileStream, TestDataMock::class)
        )
    }

    @Test
    fun testDataSerialization() {
        val byteStream = ByteArrayOutputStream()
        JsonDriver.writeValue(byteStream, testDataMock)
        val inputStream = byteStream.toByteArray().inputStream()
        assertEquals(
                testDataMock,
                JsonDriver.readValue<TestDataMock>(inputStream)
        )
    }
}
