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

package com.aheidelbacher.algostorm.core.engine.serialization

import com.aheidelbacher.algostorm.core.ecs.ComponentLibrary
import com.aheidelbacher.algostorm.core.ecs.EntityRef.Id
import com.aheidelbacher.algostorm.core.ecs.Prefab.Companion.prefabOf
import com.aheidelbacher.algostorm.core.engine.driver.Resource.Companion.resourceOf
import com.aheidelbacher.algostorm.core.engine.graphics2d.Color
import com.aheidelbacher.algostorm.test.ecs.ComponentMock
import com.aheidelbacher.algostorm.test.engine.serialization.DataMock
import com.aheidelbacher.algostorm.test.engine.serialization.DataMock.InnerDataMock
import com.aheidelbacher.algostorm.test.engine.serialization.SerializationDriverTest

class JsonDriverTest : SerializationDriverTest() {
    init {
        ComponentLibrary.registerComponentType(ComponentMock::class)
    }

    override val driver = JsonDriver
    override val inputStream =
            resourceOf("/data.${driver.format}").inputStream()
    override val data = DataMock(
            primitiveField = 1,
            innerData = InnerDataMock("non-empty"),
            list = listOf(1, 2, 3, 4, 5),
            primitiveFloatField = 1.5F,
            resource = resourceOf("/data.${driver.format}"),
            color = Color("#ff00ff00"),
            id = Id(17),
            prefabs = mapOf(
                    Id(1) to prefabOf(ComponentMock(1)),
                    Id(2) to prefabOf(ComponentMock(2))
            )
    )
}
