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

import com.aheidelbacher.algostorm.core.ecs.EntityPool
import com.aheidelbacher.algostorm.core.ecs.EntityRef
import com.aheidelbacher.algostorm.core.ecs.EntityRef.Id
import com.aheidelbacher.algostorm.core.ecs.Prefab
import com.aheidelbacher.algostorm.core.ecs.Prefab.Companion.prefabOf
import com.aheidelbacher.algostorm.core.ecs.Prefab.Companion.toPrefab

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

    protected abstract fun createInitialEntities(): Map<Id, Prefab>

    protected abstract fun createEntityPool(
            entities: Map<Id, Prefab>
    ): EntityPool

    protected lateinit var initialEntities: Map<Id, Prefab>
    protected lateinit var entityPool: EntityPool

    @Before fun init() {
        initialEntities = createInitialEntities()
        entityPool = createEntityPool(initialEntities)
    }

    @Test(expected = IllegalArgumentException::class)
    fun createDuplicatedComponentTypeShouldThrow() {
        entityPool.create(prefabOf(ComponentMock(1), ComponentMock(2)))
    }

    @Test fun entitiesShouldReturnAllExistingEntities() {
        assertEquals(
                expected = initialEntities,
                actual = entityPool.group.entities.associate {
                    it.id to it.toPrefab()
                }
        )
    }

    @Test fun getNonExistingShouldReturnNull() {
        val maxId = initialEntities.keys.maxBy { it.value }?.value ?: 0
        assertNull(entityPool.group[Id(maxId + 1)])
    }

    @Test fun getExistingShouldReturnEqualEntity() {
        for ((id, prefab) in initialEntities) {
            assertEquals(id, entityPool.group[id]?.id)
            assertEquals(prefab, entityPool.group[id]?.toPrefab())
        }
    }

    @Test fun containsExistingShouldReturnTrue() {
        for (id in initialEntities.keys) {
            assertTrue(id in entityPool.group)
        }
    }

    @Test fun containsNonExistingShouldReturnFalse() {
        val maxId = initialEntities.keys.maxBy { it.value }?.value ?: 0
        assertFalse(Id(maxId + 1) in entityPool.group)
    }

    @Test fun removeExistingShouldReturnTrue() {
        for (id in initialEntities.keys) {
            assertTrue(entityPool.remove(id))
        }
    }

    @Test fun getAfterRemoveShouldReturnNull() {
        for (id in initialEntities.keys) {
            entityPool.remove(id)
            assertNull(entityPool.group[id])
        }
    }

    @Test fun removeNonExistingShouldReturnFalse() {
        val maxId = initialEntities.keys.maxBy { it.value }?.value ?: 0
        assertFalse(entityPool.remove(Id(maxId + 1)))
    }

    @Test fun getAfterCreateShouldReturnEqualEntity() {
        val maxId = initialEntities.keys.maxBy { it.value }?.value ?: 0
        val entity = entityPool.create(prefabOf(ComponentMock(maxId + 1)))
        assertEquals(entity, entityPool.group[entity.id])
    }

    @Test fun getAfterClearShouldReturnNull() {
        entityPool.clear()
        for (id in initialEntities.keys) {
            assertNull(entityPool.group[id])
        }
    }

    @Test fun filterGroupShouldContainFilteredEntities() {
        val subgroup = entityPool.group.addGroup { it.id.value % 2 == 1 }
        assertEquals(
                expected = entityPool.group.entities.filter {
                    it.id.value % 2 == 1
                }.associateBy {
                    it.id to it.components.toSet()
                },
                actual = subgroup.entities.associateBy {
                    it.id to it.components.toSet()
                }
        )
    }
}
