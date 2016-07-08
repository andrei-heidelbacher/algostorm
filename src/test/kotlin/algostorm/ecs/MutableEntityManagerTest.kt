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
        protected val firstId: Int,
        protected val entityCount: Int
) {
    companion object {
        const val PROPERTY_NAME_MOCK: String = "property"
    }

    private val idRange = firstId..entityCount + firstId - 1

    @Before
    fun setUp() {
        for (id in idRange) {
            entityManager.create(mapOf(PROPERTY_NAME_MOCK to id))
        }
    }

    @Test
    fun entitiesShouldReturnAllExistingEntities() {
        assertEquals(
                (idRange).toSet(),
                entityManager.entities.map { it.id }.toSet()
        )
    }

    @Test
    fun getNonExistingShouldReturnNull() {
        assertEquals(null, entityManager[idRange.endInclusive + 1])
    }

    @Test
    fun getExistingShouldReturnNonNull() {
        for (id in idRange) {
            assertEquals(id, entityManager[id]?.id)
        }
    }

    @Test
    fun getExistingShouldHaveSameProperties() {
        for (id in idRange) {
            assertEquals(entityManager[id]?.get(PROPERTY_NAME_MOCK), id)
        }
    }

    @Test
    fun deleteExistingShouldReturnTrue() {
        for (id in idRange) {
            assertEquals(true, entityManager.delete(id))
        }
    }

    @Test
    fun getAfterDeleteShouldReturnNull() {
        for (id in idRange) {
            entityManager.delete(id)
            assertEquals(null, entityManager[id])
        }
    }

    @Test
    fun deleteNonExistingShouldReturnFalse() {
        assertEquals(false, entityManager.delete(idRange.endInclusive + 1))
    }

    @Test
    fun containsExistingShouldReturnTrue() {
        for (id in idRange) {
            assertEquals(true, id in entityManager)
        }
    }

    @Test
    fun containsNonExistingShouldReturnFalse() {
        assertEquals(false, idRange.endInclusive + 1 in entityManager)
    }
}
