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

package com.aheidelbacher.algostorm.test.engine.serialization

import org.junit.Before
import org.junit.Ignore
import org.junit.Test

import com.aheidelbacher.algostorm.engine.serialization.Deserializer.Companion.readValue
import com.aheidelbacher.algostorm.engine.serialization.SerializationDriver

import java.io.ByteArrayOutputStream
import java.io.InputStream

import kotlin.test.assertEquals

@Ignore
abstract class SerializationDriverTest {
    protected abstract fun createSerializationDriver(): SerializationDriver

    protected lateinit var serializationDriver: SerializationDriver
        private set

    protected abstract val testDataMock: TestDataMock

    protected abstract val inputStream: InputStream

    @Before
    fun initializeSerializationDriver() {
        serializationDriver = createSerializationDriver()
    }

    @Test
    fun testDataInlineDeserialization() {
        assertEquals(
                testDataMock,
                serializationDriver.readValue<TestDataMock>(inputStream)
        )
    }

    @Test
    fun testDataDeserialization() {
        assertEquals(
                testDataMock,
                serializationDriver.readValue(inputStream, TestDataMock::class)
        )
    }

    @Test
    fun testDataSerialization() {
        val byteStream = ByteArrayOutputStream()
        serializationDriver.writeValue(byteStream, testDataMock)
        val inputStream = byteStream.toByteArray().inputStream()
        assertEquals(
                testDataMock,
                serializationDriver.readValue<TestDataMock>(inputStream)
        )
    }
}
