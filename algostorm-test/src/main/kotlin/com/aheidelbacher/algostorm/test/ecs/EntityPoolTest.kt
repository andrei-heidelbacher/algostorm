/*
 * Copyright (c) 2016  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.aheidelbacher.algostorm.test.ecs

import org.junit.Ignore
import org.junit.Test

import com.aheidelbacher.algostorm.ecs.Component
import com.aheidelbacher.algostorm.ecs.EntityGroup
import com.aheidelbacher.algostorm.ecs.EntityPool
import com.aheidelbacher.algostorm.ecs.EntityRef
import org.junit.Before

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Ignore
abstract class EntityPoolTest {
    companion object {
        fun assertEquals(expected: EntityRef?, actual: EntityRef?) {
            kotlin.test.assertEquals(expected, actual)
            assertEquals(
                    expected = expected?.components?.toSet(),
                    actual = actual?.components?.toSet()
            )
        }
    }

    abstract fun createInitialEntities(): Map<Int, Collection<Component>>

    abstract fun createEntityPool(
            entities: Map<Int, Collection<Component>>
    ): EntityPool

    protected lateinit var initialEntities: Map<Int, Collection<Component>>
    protected lateinit var entityPool: EntityPool

    @Before
    fun init() {
        initialEntities = createInitialEntities()
        entityPool = createEntityPool(initialEntities)
    }

    @Test(expected = IllegalArgumentException::class)
    fun getInvalidIdShouldThrow() {
        entityPool.group[-1]
    }

    @Test(expected = IllegalArgumentException::class)
    fun createDuplicatedComponentTypeShouldThrow() {
        entityPool.create(listOf(ComponentMock(1), ComponentMock(2)))
    }

    @Test(expected = IllegalArgumentException::class)
    fun addDuplicatedGroupShouldThrow() {
        val name = "duplicated"
        entityPool.group.addGroup(name) { true }
        entityPool.group.addGroup(name) { true }
    }

    @Test
    fun entitiesShouldReturnAllExistingEntities() {
        assertEquals(
                expected = initialEntities.mapValues { it.value.toSet() },
                actual = entityPool.group.entities.associate {
                    it.id to it.components.toSet()
                }
        )
    }

    @Test
    fun getNonExistingShouldReturnNull() {
        val maxId = initialEntities.keys.max() ?: 0
        assertNull(entityPool.group[maxId + 1])
    }

    @Test
    fun getExistingShouldReturnEqualEntity() {
        for ((id, components) in initialEntities) {
            assertEquals(id, entityPool.group[id]?.id)
            assertEquals(
                    expected = components.toSet(),
                    actual = entityPool.group[id]?.components?.toSet()
            )
        }
    }

    @Test
    fun containsExistingShouldReturnTrue() {
        for (id in initialEntities.keys) {
            assertTrue(id in entityPool.group)
        }
    }

    @Test
    fun containsNonExistingShouldReturnFalse() {
        val maxId = initialEntities.keys.max() ?: 0
        assertFalse((maxId + 1) in entityPool.group)
    }

    @Test
    fun removeExistingShouldReturnTrue() {
        for (id in initialEntities.keys) {
            assertTrue(entityPool.delete(id))
        }
    }

    @Test
    fun getAfterRemoveShouldReturnNull() {
        for (id in initialEntities.keys) {
            entityPool.delete(id)
            assertNull(entityPool.group[id])
        }
    }

    @Test
    fun removeNonExistingShouldReturnFalse() {
        val maxId = initialEntities.keys.max() ?: 0
        assertFalse(entityPool.delete(maxId + 1))
    }

    @Test
    fun getAfterCreateShouldReturnEqualEntity() {
        val maxId = initialEntities.keys.max() ?: 0
        val entity = entityPool.create(listOf(ComponentMock(maxId + 1)))
        assertEquals(entity, entityPool.group[entity.id])
    }

    @Test
    fun getAfterClearShouldReturnNull() {
        entityPool.clear()
        for (id in initialEntities.keys) {
            assertNull(entityPool.group[id])
        }
    }

    @Test
    fun filterGroupShouldContainFilteredEntities() {
        val subgroup = entityPool.group.addGroup("odd-id") { it.id % 2 == 1 }
        assertEquals(
                expected = entityPool.group.entities.filter {
                    it.id % 2 == 1
                }.associateBy {
                    it.id to it.components.toSet()
                },
                actual = subgroup.entities.associateBy {
                    it.id to it.components.toSet()
                }
        )
    }
}
