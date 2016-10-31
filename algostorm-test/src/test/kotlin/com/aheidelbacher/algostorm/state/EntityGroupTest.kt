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

package com.aheidelbacher.algostorm.state

import org.junit.Test

import com.aheidelbacher.algostorm.state.Builders.entity
import com.aheidelbacher.algostorm.state.Builders.entityGroup
import com.aheidelbacher.algostorm.state.Builders.mapObject
import com.aheidelbacher.algostorm.state.Layer.EntityGroup

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EntityGroupTest {
    companion object {
        const val ENTITY_COUNT: Int = 1000
    }

    data class ComponentMock(val id: Int) : Component

    val entityGroup = mapObject {
        width = 32
        height = 32
        tileWidth = 24
        tileHeight = 24
        +entityGroup {
            name = "entityGroup"
            for (i in 1..ENTITY_COUNT) {
                +entity {
                    +ComponentMock(i)
                }
            }
        }
    }.layers.filterIsInstance<EntityGroup>().single()

    val entityIds = entityGroup.entities.map(Entity::id)

    @Test
    fun entitiesShouldReturnAllExistingEntities() {
        assertEquals((1..ENTITY_COUNT).toList().toSet(), entityIds.toSet())
    }

    @Test
    fun getNonExistingShouldReturnNull() {
        assertNull(entityGroup[entityIds.max()!! + 1])
    }

    @Test
    fun getExistingShouldReturnNonNull() {
        for (id in entityIds) {
            assertEquals(id, entityGroup[id]?.id)
        }
    }

    @Test
    fun getExistingShouldHaveSameProperties() {
        for (id in entityIds) {
            val entity = entityGroup[id]
            assertEquals(id, entity?.get(ComponentMock::class)?.id)
            assertEquals(id, entity?.get(ComponentMock::class)?.id)
        }
    }

    @Test
    fun removeExistingShouldReturnTrue() {
        for (id in entityIds) {
            assertNotNull(entityGroup.remove(id))
        }
    }

    @Test
    fun getAfterRemoveShouldReturnNull() {
        for (id in entityIds) {
            entityGroup.remove(id)
            assertNull(entityGroup[id])
        }
    }

    @Test
    fun removeNonExistingShouldReturnFalse() {
        assertNull(entityGroup.remove(entityIds.max()!! + 1))
    }

    @Test
    fun containsExistingShouldReturnTrue() {
        for (id in entityIds) {
            assertTrue(id in entityGroup)
        }
    }

    @Test
    fun containsNonExistingShouldReturnFalse() {
        assertFalse((entityIds.max()!! + 1) in entityGroup)
    }

    @Test
    fun getAfterAddShouldReturnNonNull() {
        val id = entityIds.max()!! + 1
        val entity = entity(id) { +ComponentMock(id) }
        entityGroup.add(entity)
        assertEquals(entity, entityGroup[id])
    }

    @Test
    fun getAfterAddAllShouldReturnNonNull() {
        val firstId = entityIds.max()!! + 1
        val ids = firstId until firstId + ENTITY_COUNT
        val entities = ids.map { entity(it) { +ComponentMock(it) } }.toSet()
        entityGroup.addAll(entities)
        for (id in ids) {
            assertEquals(id, entityGroup[id]?.id)
        }
    }

    @Test
    fun getAfterClearShouldReturnNull() {
        entityGroup.clear()
        for (id in entityIds) {
            assertNull(entityGroup[id])
        }
    }
}
