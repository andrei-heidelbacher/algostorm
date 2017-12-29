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

package com.andreihh.algostorm.core.ecs

import com.andreihh.algostorm.core.ecs.EntityRef.Id
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Ignore
abstract class EntityPoolTest {
    companion object {
        fun assertEquals(expected: EntityRef?, actual: EntityRef?) {
            kotlin.test.assertEquals(expected?.hashCode(), actual?.hashCode())
            kotlin.test.assertEquals(expected, actual)
            assertEquals(
                    expected = expected?.components?.toSet(),
                    actual = actual?.components?.toSet()
            )
        }
    }

    protected abstract fun createInitialEntities(
    ): Map<Id, Collection<Component>>

    protected abstract fun createEntityPool(
            entities: Map<Id, Collection<Component>>
    ): EntityPool

    protected lateinit var initialEntities: Map<Id, Collection<Component>>
    protected lateinit var entityPool: EntityPool

    @Before fun init() {
        initialEntities = createInitialEntities()
        entityPool = createEntityPool(initialEntities)
    }

    @Test fun testGetSnapshotReturnsSameEntities() {
        assertEquals(
                expected = initialEntities,
                actual = entityPool.entities
                        .associate { it.id to it.components.toSet() }
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun createDuplicatedComponentTypeShouldThrow() {
        entityPool.create(setOf(ComponentMock(1), ComponentMock(2)))
    }

    @Test fun entitiesShouldReturnAllExistingEntities() {
        assertEquals(
                expected = initialEntities,
                actual = entityPool.entities.associate {
                    it.id to it.components.toSet()
                }
        )
    }

    @Test fun getNonExistingShouldReturnNull() {
        val maxId = initialEntities.keys.maxBy { it.value }?.value ?: 0
        assertNull(entityPool[Id(maxId + 1)])
    }

    @Test fun getExistingShouldReturnEqualEntity() {
        for ((id, components) in initialEntities) {
            assertEquals(id, entityPool[id]?.id)
            assertEquals(
                    expected = components.toSet(),
                    actual = entityPool[id]?.components?.toSet()
            )
        }
    }

    @Test fun containsExistingShouldReturnTrue() {
        for (id in initialEntities.keys) {
            assertTrue(id in entityPool)
        }
    }

    @Test fun containsNonExistingShouldReturnFalse() {
        val maxId = initialEntities.keys.maxBy { it.value }?.value ?: 0
        assertFalse(Id(maxId + 1) in entityPool)
    }

    @Test fun removeExistingShouldReturnTrue() {
        for (id in initialEntities.keys) {
            assertTrue(entityPool.remove(id))
        }
    }

    @Test fun getAfterRemoveShouldReturnNull() {
        for (id in initialEntities.keys) {
            entityPool.remove(id)
            assertNull(entityPool[id])
        }
    }

    @Test fun removeNonExistingShouldReturnFalse() {
        val maxId = initialEntities.keys.maxBy { it.value }?.value ?: 0
        assertFalse(entityPool.remove(Id(maxId + 1)))
    }

    @Test fun getAfterCreateShouldReturnEqualEntity() {
        val maxId = initialEntities.keys.maxBy { it.value }?.value ?: 0
        val entity = entityPool.create(setOf(ComponentMock(maxId + 1)))
        assertEquals(entity, entityPool[entity.id])
    }

    @Test fun getAfterClearShouldReturnNull() {
        entityPool.clear()
        for (id in initialEntities.keys) {
            assertNull(entityPool[id])
        }
    }

    @Test fun filterGroupShouldContainFilteredEntities() {
        val subgroup = entityPool.addGroup { it.id.value % 2 == 1 }
        assertEquals(
                expected = entityPool.entities.filter {
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
