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

import com.aheidelbacher.algostorm.core.ecs.Component
import com.aheidelbacher.algostorm.core.ecs.ComponentLibrary
import com.aheidelbacher.algostorm.core.ecs.EntityRef
import com.aheidelbacher.algostorm.core.ecs.Prefab
import com.aheidelbacher.algostorm.core.ecs.Prefab.Companion.prefabOf

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Ignore
abstract class EntityRefTest {
    /*init {
        ComponentLibrary.registerComponentType(Component::class)
        ComponentLibrary.registerComponentType(TestComponent::class)
    }*/

    data class TestComponent(val id: Int) : Component

    protected abstract fun createEntity(prefab: Prefab): EntityRef

    @Test fun testComponentsAreEqualToPrefabComponents() {
        val id = 153
        val prefab = prefabOf(ComponentMock(id), TestComponent(id))
        val entity = createEntity(prefab)
        assertEquals(prefab.components, entity.components.toSet())
    }

    @Test fun testGetComponentReturnsEqualComponent() {
        val id = 9
        val prefab = prefabOf(ComponentMock(id), TestComponent(id))
        val entity = createEntity(prefab)
        assertEquals(ComponentMock(id), entity[ComponentMock::class])
    }

    @Test fun testGetAbsentComponentReturnsNull() {
        val id = 9
        val prefab = prefabOf(ComponentMock(id))
        val entity = createEntity(prefab)
        assertNull(entity[TestComponent::class])
    }
    @Test fun testContainsComponentReturnsTrue() {
        val id = 9
        val prefab = prefabOf(ComponentMock(id), TestComponent(id))
        val entity = createEntity(prefab)
        assertTrue(ComponentMock::class in entity)
    }

    @Test fun testContainsAbsentComponentReturnsFalse() {
        val id = 9
        val prefab = prefabOf(ComponentMock(id))
        val entity = createEntity(prefab)
        assertFalse(TestComponent::class in entity)
    }
}
