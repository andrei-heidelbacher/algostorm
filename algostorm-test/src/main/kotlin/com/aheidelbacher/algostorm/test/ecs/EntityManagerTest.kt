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

import com.aheidelbacher.algostorm.ecs.Entity
import com.aheidelbacher.algostorm.ecs.EntityManager

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Ignore
abstract class EntityManagerTest {
    companion object {
        fun assertEquals(expected: Entity?, actual: Entity?) {
            assertEquals(expected?.id, actual?.id)
            assertEquals(
                    expected = expected?.components?.toSet(),
                    actual = actual?.components?.toSet()
            )
        }
    }

    protected abstract val entities: Set<Entity>
    protected abstract val entityManager: EntityManager

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
    fun getExistingShouldReturnEqualEntity() {
        for (entity in entities) {
            assertEquals(entity, entityManager[entity.id])
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
