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
import com.aheidelbacher.algostorm.ecs.MutableEntity
import com.aheidelbacher.algostorm.ecs.MutableEntityManager

import kotlin.test.assertEquals
import kotlin.test.assertNull

@Ignore
abstract class MutableEntityManagerTest : EntityManagerTest() {
    private lateinit var entities: Set<MutableEntity>
    private lateinit var entityManager: MutableEntityManager

    abstract override fun createInitialEntities(): Set<MutableEntity>

    abstract fun createEmptyMutableEntityManager(): MutableEntityManager

    override fun createEntityManager(entities: Set<Entity>): EntityManager =
            createEmptyMutableEntityManager().apply {
                entities.forEach { e ->
                    val entity = EntityMock(e.id)
                    e.components.forEach { c -> entity.set(c) }
                    add(entity)
                }
            }

    @Before
    fun initializeMutableEntityManager() {
        entities = createInitialEntities()
        entityManager = createEmptyMutableEntityManager()
        entities.forEach { entityManager.add(it) }
    }

    @Test
    fun removeExistingShouldReturnEqualEntity() {
        for (entity in entities) {
            assertEquals(entity, entityManager.remove(entity.id))
        }
    }

    @Test
    fun getAfterRemoveShouldReturnNull() {
        for (entity in entities) {
            entityManager.remove(entity.id)
            assertNull(entityManager[entity.id])
        }
    }

    @Test
    fun removeNonExistingShouldReturnNull() {
        val maxId = entities.maxBy(Entity::id)?.id ?: 0
        assertNull(entityManager.remove(maxId + 1))
    }

    @Test
    fun getAfterAddShouldReturnEqualEntity() {
        val maxId = entities.maxBy(Entity::id)?.id ?: 0
        val entity = EntityMock(maxId + 1)
        entityManager.add(entity)
        assertEquals(entity, entityManager[entity.id])
    }

    @Test
    fun getAfterClearShouldReturnNull() {
        entityManager.clear()
        for (entity in entities) {
            assertNull(entityManager[entity.id])
        }
    }
}
