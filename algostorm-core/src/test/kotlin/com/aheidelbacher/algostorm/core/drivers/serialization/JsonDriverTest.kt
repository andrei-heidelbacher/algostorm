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

package com.aheidelbacher.algostorm.core.drivers.serialization

import org.junit.Test

import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.Color
import com.aheidelbacher.algostorm.core.drivers.io.Resource.Companion.resourceOf
import com.aheidelbacher.algostorm.core.drivers.serialization.DataMock.InnerDataMock
import com.aheidelbacher.algostorm.core.ecs.EntityRef.Id
import com.aheidelbacher.algostorm.core.ecs.Prefab
import com.aheidelbacher.algostorm.core.ecs.Prefab.Companion.prefabOf
import com.aheidelbacher.algostorm.test.ecs.ComponentMock

import java.io.ByteArrayOutputStream

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class JsonDriverTest {
    private val driver = JsonDriver
    private val inputStream = javaClass.getResourceAsStream("/data.json")
    private val data = DataMock(
            primitiveField = 1,
            innerData = InnerDataMock("non-empty"),
            list = listOf(1, 2, 3, 4, 5),
            primitiveFloatField = 1.5F,
            resource = resourceOf("data.json"),
            color = Color("#ff00ff00"),
            id = Id(17),
            prefabs = mapOf(
                    Id(1) to prefabOf(ComponentMock(1)),
                    Id(2) to prefabOf(ComponentMock(2))
            )
    )

    @Test fun `test data deserialization`() {
        assertEquals(data, driver.deserialize(inputStream))
    }

    @Test fun `test data serialization`() {
        val byteStream = ByteArrayOutputStream()
        driver.serialize(byteStream, data)
        val inputStream = byteStream.toByteArray().inputStream()
        assertEquals(data, driver.deserialize(inputStream))
    }

    @Test fun `test load prefab with invalid component throws`() {
        val src = javaClass.getResourceAsStream("/invalid_prefab.json")
        assertFailsWith<JsonException> {
            driver.deserialize<Prefab>(src)
        }
    }
}
