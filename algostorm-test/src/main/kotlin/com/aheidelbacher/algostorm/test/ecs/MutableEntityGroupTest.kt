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

package com.aheidelbacher.algostorm.test.ecs

import org.junit.Before
import org.junit.Ignore
import org.junit.Test

import com.aheidelbacher.algostorm.core.ecs.EntityRef
import com.aheidelbacher.algostorm.core.ecs.MutableEntityGroup
import com.aheidelbacher.algostorm.core.ecs.Prefab

import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Ignore
abstract class MutableEntityGroupTest : EntityGroupTest() {
    private lateinit var group: MutableEntityGroup

    override abstract fun createGroup(
            entities: Map<EntityRef.Id, Prefab>
    ): MutableEntityGroup

    @Before fun initMutableGroup() {
        group = createGroup(createInitialEntities())
    }

    @Test fun testChangedEntityRemovedFromSubgroup() {
        val subgroup = group.addGroup {
            val id = it[ComponentMock::class]?.id
            id != null && id % 2 == 1
        }
        group.entities.forEach {
            if (it.id in subgroup) {
                it.remove(ComponentMock::class)
                assertFalse(it.id in subgroup)
            }
        }
    }

    @Test fun testChangedEntityAddedToSubgroup() {
        val subgroup = group.addGroup {
            val id = it[ComponentMock::class]?.id
            id != null && id % 2 == 0
        }
        group.entities.forEach {
            if (it.id !in subgroup) {
                it.set(ComponentMock(it.id.value - it.id.value % 2))
                assertTrue(it.id in subgroup)
            }
        }
    }
}