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

import org.junit.Ignore
import org.junit.Test

import com.aheidelbacher.algostorm.engine.serialization.Deserializer.Companion.readValue
import com.aheidelbacher.algostorm.engine.serialization.SerializationDriver
import com.aheidelbacher.algostorm.test.engine.driver.DriverTest

import java.io.ByteArrayOutputStream
import java.io.InputStream

import kotlin.test.assertEquals

@Ignore
abstract class SerializationDriverTest : DriverTest() {
    override abstract val driver: SerializationDriver

    protected abstract val data: DataMock

    protected abstract val inputStream: InputStream

    @Test fun testDataInlineDeserialization() {
        assertEquals(data, driver.readValue<DataMock>(inputStream))
    }

    @Test fun testDataDeserialization() {
        assertEquals(data, driver.readValue(inputStream, DataMock::class))
    }

    @Test fun testDataSerialization() {
        val byteStream = ByteArrayOutputStream()
        driver.writeValue(byteStream, data)
        val inputStream = byteStream.toByteArray().inputStream()
        assertEquals(data, driver.readValue<DataMock>(inputStream))
    }
}
