/*
 * Copyright 2016 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.aheidelbacher.algostorm.state

import com.aheidelbacher.algostorm.ecs.MutableEntity
import com.aheidelbacher.algostorm.ecs.MutableEntityManager
import com.aheidelbacher.algostorm.state.Entity.Factory
import com.aheidelbacher.algostorm.state.builders.EntityGroupBuilder
import com.aheidelbacher.algostorm.test.ecs.MutableEntityManagerTest

class EntityGroupTest : MutableEntityManagerTest() {
    companion object {
        const val ENTITY_COUNT: Int = 1000
    }

    override fun createInitialEntities(): Set<MutableEntity> = with(Factory()) {
        (1..ENTITY_COUNT).map { id ->
            create(listOf(ComponentMock(id)))
        }.toSet()
    }

    override fun createEmptyMutableEntityManager(): MutableEntityManager =
            EntityGroupBuilder().apply { name = "entityGroup" }.build()
}
