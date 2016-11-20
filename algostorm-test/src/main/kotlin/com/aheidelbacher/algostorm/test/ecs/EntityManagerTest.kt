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

package com.aheidelbacher.algostorm.test.ecs

import org.junit.Before
import org.junit.Ignore
import org.junit.Test

import com.aheidelbacher.algostorm.ecs.Entity
import com.aheidelbacher.algostorm.ecs.EntityManager

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Ignore
abstract class EntityManagerTest {
    private lateinit var entities: Set<Entity>
    private lateinit var entityManager: EntityManager

    abstract fun createInitialEntities(): Set<Entity>

    abstract fun createEntityManager(entities: Set<Entity>): EntityManager

    @Before
    fun initializeEntityManager() {
        entities = createInitialEntities()
        entityManager = createEntityManager(entities)
    }

    @Test
    fun entitiesShouldReturnAllExistingEntities() {
        assertEquals(entities, entityManager.entities.toSet())
    }

    @Test
    fun getNonExistingShouldReturnNull() {
        val maxId = entities.maxBy(Entity::id)?.id ?: 0
        assertNull(entityManager[maxId + 1])
    }

    @Test
    fun getExistingShouldReturnNonNull() {
        for (entity in entities) {
            assertEquals(entity, entityManager[entity.id])
        }
    }

    @Test
    fun getExistingShouldHaveSameComponents() {
        for (entity in entities) {
            assertEquals(
                    expected = entity.components.toSet(),
                    actual = entityManager[entity.id]?.components?.toSet()
            )
        }
    }

    @Test
    fun containsExistingShouldReturnTrue() {
        for (entity in entities) {
            assertTrue(entity.id in entityManager)
        }
    }

    @Test
    fun containsNonExistingShouldReturnFalse() {
        val maxId = entities.maxBy(Entity::id)?.id ?: 0
        assertFalse((maxId + 1) in entityManager)
    }
}
