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

package com.aheidelbacher.algostorm.ecs

import com.aheidelbacher.algostorm.ecs.EntityPool.Companion.entityPoolOf
import com.aheidelbacher.algostorm.ecs.EntityRef.Id
import com.aheidelbacher.algostorm.ecs.Prefab.Companion.prefabOf
import com.aheidelbacher.algostorm.test.ecs.ComponentMock
import com.aheidelbacher.algostorm.test.ecs.EntityPoolTest

class EntityPoolImplTest : EntityPoolTest() {
    companion object {
        const val ENTITY_COUNT: Int = 1000
    }

    override fun createInitialEntities(): Map<Id, Prefab> =
            (1 until ENTITY_COUNT).associate {
                Id(it) to prefabOf(ComponentMock(it))
            }

    override fun createEntityPool(
            entities: Map<Id, Prefab>
    ): EntityPool = entityPoolOf(entities)
}
