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

package algostorm.ecs

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

/**
 * An abstract test class for a [MutableEntityManager].
 *
 * In order to test common functionality to all entity managers, you may
 * implement this class and provide a concrete entity manager instance to test.
 *
 * @property entityManager the entity manager instance that should be tested
 * @property entityCount the number of entities used to initialize the entity
 * manager
 */
@Ignore
abstract class MutableEntityManagerTest(
        protected val entityManager: MutableEntityManager,
        protected val entityCount: Int
) {
    companion object {
        const val PROPERTY_NAME_MOCK: String = "property"
    }

    @Before
    fun setUp() {
        for (id in 0..entityCount - 1) {
            entityManager.create(mapOf(PROPERTY_NAME_MOCK to id))
        }
    }

    @Test
    fun entitiesShouldReturnAllExistingEntities() {
        assertEquals(
                (0..entityCount - 1).toSet(),
                entityManager.entities.map { it.id }.toSet()
        )
    }

    @Test
    fun clearShouldLeaveEntitiesEmpty() {
        entityManager.clear()
        assertEquals(0, entityManager.entities.count())
    }

    @Test
    fun getAfterClearShouldReturnNull() {
        entityManager.clear()
        for (id in -1..entityCount) {
            assertEquals(null, entityManager[id])
        }
    }

    @Test
    fun getNonExistingShouldReturnNull() {
        assertEquals(null, entityManager[entityCount])
    }

    @Test
    fun getExistingShouldReturnNonNull() {
        for (id in 0..entityCount - 1) {
            assertEquals(id, entityManager[id]?.id)
        }
    }

    @Test
    fun getExistingShouldHaveSameComponents() {
        for (id in 0..entityCount - 1) {
            assertEquals(entityManager[id]?.get(PROPERTY_NAME_MOCK), id)
        }
    }

    @Test
    fun deleteExistingShouldReturnTrue() {
        for (id in 0..entityCount - 1) {
            assertEquals(true, entityManager.delete(id))
        }
    }

    @Test
    fun getAfterDeleteShouldReturnNull() {
        for (id in 0..entityCount - 1) {
            entityManager.delete(id)
            assertEquals(null, entityManager[id])
        }
    }

    @Test
    fun deleteNonExistingShouldReturnFalse() {
        assertEquals(false, entityManager.delete(entityCount))
    }

    @Test
    fun containsExistingShouldReturnTrue() {
        for (id in 0..entityCount - 1) {
            assertEquals(true, id in entityManager)
        }
    }

    @Test
    fun containsNonExistingShouldReturnFalse() {
        assertEquals(false, entityCount in entityManager)
    }

    @Test
    fun filterEntitiesShouldFilterNonConformingEntities() {
        for (id in entityCount..2 * entityCount - 1) {
            entityManager.create(emptyMap())
        }
        assertEquals(
                (0..entityCount - 1).toSet(),
                entityManager.filterEntities(PROPERTY_NAME_MOCK).map {
                    it.id
                }.toSet()
        )
    }
}
