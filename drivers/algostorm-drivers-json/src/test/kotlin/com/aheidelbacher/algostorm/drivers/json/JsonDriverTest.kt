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

package com.aheidelbacher.algostorm.drivers.json

import org.junit.Test

import com.aheidelbacher.algostorm.core.drivers.Resource.Companion.resourceOf
import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.Color
import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.TileSet
import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.TileSet.Frame
import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.TileSet.Image
import com.aheidelbacher.algostorm.core.drivers.serialization.Deserializer.Companion.readValue
import com.aheidelbacher.algostorm.core.ecs.EntityRef.Id
import com.aheidelbacher.algostorm.core.ecs.Prefab
import com.aheidelbacher.algostorm.core.ecs.Prefab.Companion.prefabOf
import com.aheidelbacher.algostorm.test.ecs.ComponentMock
import com.aheidelbacher.algostorm.test.engine.serialization.DataMock
import com.aheidelbacher.algostorm.test.engine.serialization.DataMock.InnerDataMock
import com.aheidelbacher.algostorm.test.engine.serialization.SerializationDriverTest

import java.io.IOException

class JsonDriverTest : SerializationDriverTest() {
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
            ),
            tileSets = listOf(TileSet.Companion(
                    name = "world",
                    image = Image(
                            resource = resourceOf("/data.${driver.format}"),
                            width = 288,
                            height = 240
                    ),
                    tileWidth = 24,
                    tileHeight = 24,
                    animations = mapOf(
                            "tile:idle" to listOf(Frame(1, 250), Frame(2, 250))
                    )
            ))
    )

    @Test(expected = IOException::class)
    fun testLoadPrefabWithInvalidComponentThrows() {
        driver.readValue<Prefab>(
                src = resourceOf("/invalidComponentPrefab.json").inputStream()
        )
    }

    @Test(expected = IOException::class)
    fun testLoadPrefabWithNonExistingComponentThrows() {
        driver.readValue<Prefab>(
                src = resourceOf("/nonExistingComponentPrefab.json").inputStream()
        )
    }
}
