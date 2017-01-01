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

package com.aheidelbacher.algostorm.engine.serialization

import com.aheidelbacher.algostorm.engine.driver.Resource
import com.aheidelbacher.algostorm.engine.driver.Resource.Companion.SCHEMA
import com.aheidelbacher.algostorm.engine.graphics2d.Color
import com.aheidelbacher.algostorm.test.engine.serialization.SerializationDriverTest
import com.aheidelbacher.algostorm.test.engine.serialization.TestDataMock
import com.aheidelbacher.algostorm.test.engine.serialization.TestDataMock.InnerTestDataMock

class JsonDriverTest : SerializationDriverTest() {
    override fun createSerializationDriver(): SerializationDriver = JsonDriver()

    override val inputStream =
            Resource("$SCHEMA/testData.${JsonDriver.FORMAT}").inputStream()

    override val testDataMock = TestDataMock(
            primitiveTestField = 1,
            innerTestData = InnerTestDataMock("non-empty"),
            testList = listOf(1, 2, 3, 4, 5),
            defaultPrimitiveTestField = 1.5F,
            testResource = Resource("$SCHEMA/testResource.txt"),
            testColor = Color("#ff00ff00")
    )
}
