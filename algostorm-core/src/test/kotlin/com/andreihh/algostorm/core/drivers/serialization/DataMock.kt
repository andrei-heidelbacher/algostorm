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

package com.andreihh.algostorm.core.drivers.serialization

import com.andreihh.algostorm.core.drivers.graphics2d.Color
import com.andreihh.algostorm.core.drivers.io.Resource
import com.andreihh.algostorm.core.ecs.Component
import com.andreihh.algostorm.core.ecs.EntityRef.Id

data class DataMock(
        val primitiveField: Int,
        val primitiveFloatField: Float,
        val innerData: InnerDataMock,
        val list: List<Int>,
        val resource: Resource<Any>,
        val color: Color,
        val id: Id,
        val prefabs: Map<Id, Set<Component>>
) {
    data class InnerDataMock(val field: String)
}
