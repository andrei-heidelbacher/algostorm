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

package com.aheidelbacher.algostorm.systems.state

import com.aheidelbacher.algostorm.ecs.MutableEntity
import com.aheidelbacher.algostorm.ecs.MutableEntityManager
import com.aheidelbacher.algostorm.systems.state.builders.EntityGroupBuilder
import com.aheidelbacher.algostorm.test.ecs.MutableEntityManagerTest

class EntityGroupTest : MutableEntityManagerTest() {
    companion object {
        const val ENTITY_COUNT: Int = 1000
    }

    override val entityManager: MutableEntityManager = EntityGroupBuilder()
            .apply { name = "entityGroup" }
            .build()
            .apply {
                for (id in 1..ENTITY_COUNT) {
                    create(listOf(ComponentMock(id)))
                }
            }
    override val entities: Set<MutableEntity> = entityManager.entities.toSet()
}
