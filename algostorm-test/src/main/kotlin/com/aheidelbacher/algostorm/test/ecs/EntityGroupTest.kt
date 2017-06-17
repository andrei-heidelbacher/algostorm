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

import com.aheidelbacher.algostorm.core.ecs.EntityGroup
import com.aheidelbacher.algostorm.core.ecs.EntityGroup.Companion.getSnapshot
import com.aheidelbacher.algostorm.core.ecs.EntityRef
import com.aheidelbacher.algostorm.core.ecs.EntityRef.Id
import com.aheidelbacher.algostorm.core.ecs.Prefab
import com.aheidelbacher.algostorm.core.ecs.Prefab.Companion.prefabOf

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Ignore
abstract class EntityGroupTest {
    private lateinit var initialEntities: Map<Id, Prefab>
    private lateinit var group: EntityGroup

    protected open fun createInitialEntities(): Map<Id, Prefab> =
            (1 until 1000).associate {
                Id(it) to prefabOf(ComponentMock(it))
            }

    protected abstract fun createGroup(entities: Map<Id, Prefab>): EntityGroup

    @Before fun initGroup() {
        initialEntities = createInitialEntities()
        group = createGroup(initialEntities)
    }

    @Test fun testGetSnapshotReturnsSameEntities() {
        assertEquals(initialEntities, group.getSnapshot())
    }

    @Test fun testGroupIsValid() {
        assertTrue(group.isValid)
    }

    @Test fun testContainsReturnsTrue() {
        for (id in initialEntities.keys) {
            assertTrue(id in group)
        }
    }

    @Test fun testContainsAbsentReturnsFalse() {
        val maxId = initialEntities.keys.maxBy { it.value }?.value ?: 0
        assertFalse(Id(maxId + 1) in group)
    }

    @Test fun testFilterGroupShouldContainFilteredEntities() {
        val subgroup = group.addGroup { it.id.value % 2 == 1 }
        assertEquals(
                expected = initialEntities.filter { it.key.value % 2 == 1 },
                actual = subgroup.getSnapshot()
        )
    }

    @Test fun testRemoveAddedGroupShouldReturnTrue() {
        val subgroup = group.addGroup { true }
        assertTrue(group.removeGroup(subgroup))
    }

    @Test fun testRemovedGroupIsInvalid() {
        val subgroup = group.addGroup { true }
        group.removeGroup(subgroup)
        assertFalse(subgroup.isValid)
    }

    @Test fun testRemovedGroupIsEmpty() {
        val subgroup = group.addGroup { true }
        group.removeGroup(subgroup)
        assertTrue(subgroup.entities.count() == 0)
    }

    @Test fun testRemoveRemovedGroupReturnsFalse() {
        val subgroup = group.addGroup { true }
        group.removeGroup(subgroup)
        assertFalse(group.removeGroup(subgroup))
    }

    @Test fun testRemoveNonSubgroupReturnsFalse() {
        val subgroup = object : EntityGroup {
            override val entities: Iterable<EntityRef>
                get() = throw UnsupportedOperationException()

            override val isValid: Boolean
                get() = throw UnsupportedOperationException()

            override fun get(id: Id): EntityRef? =
                    throw UnsupportedOperationException()

            override fun contains(id: Id): Boolean =
                    throw UnsupportedOperationException()

            override fun addGroup(filter: (EntityRef) -> Boolean): EntityGroup =
                    throw UnsupportedOperationException()

            override fun removeGroup(group: EntityGroup): Boolean =
                    throw UnsupportedOperationException()
        }
        assertFalse(group.removeGroup(subgroup))
    }

    @Test(expected = IllegalStateException::class)
    fun testAddGroupToInvalidGroupThrows() {
        val subgroup = group.addGroup { true }
        group.removeGroup(subgroup)
        subgroup.addGroup { true }
    }
}
