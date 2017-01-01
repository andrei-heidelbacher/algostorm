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

import org.junit.Ignore
import org.junit.Test

import com.aheidelbacher.algostorm.ecs.MutableEntity
import com.aheidelbacher.algostorm.ecs.MutableEntityManager

import kotlin.test.assertNull

@Ignore
abstract class MutableEntityManagerTest : EntityManagerTest() {
    override abstract val entities: Set<MutableEntity>
    override abstract val entityManager: MutableEntityManager

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
        val maxId = entities.maxBy(MutableEntity::id)?.id ?: 0
        assertNull(entityManager.remove(maxId + 1))
    }

    @Test
    fun getAfterCreateShouldReturnEqualEntity() {
        val maxId = entities.maxBy(MutableEntity::id)?.id ?: 0
        val entity = entityManager.create(listOf(ComponentMock(maxId + 1)))
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
