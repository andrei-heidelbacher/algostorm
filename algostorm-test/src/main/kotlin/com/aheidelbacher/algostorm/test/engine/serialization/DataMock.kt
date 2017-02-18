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

import com.aheidelbacher.algostorm.core.drivers.Resource
import com.aheidelbacher.algostorm.core.ecs.EntityRef.Id
import com.aheidelbacher.algostorm.core.ecs.Prefab
import com.aheidelbacher.algostorm.core.engine.graphics2d.Color
import com.aheidelbacher.algostorm.core.engine.graphics2d.TileSet

data class DataMock(
        val primitiveField: Int,
        val primitiveFloatField: Float,
        val innerData: InnerDataMock,
        val list: List<Int>,
        val resource: Resource,
        val color: Color,
        val id: Id,
        val prefabs: Map<Id, Prefab>,
        val tileSets: List<TileSet>
) {
    data class InnerDataMock(val field: String)
}
