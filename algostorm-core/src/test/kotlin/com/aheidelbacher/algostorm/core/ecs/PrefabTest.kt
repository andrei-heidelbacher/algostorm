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

package com.aheidelbacher.algostorm.core.ecs

import com.aheidelbacher.algostorm.core.ecs.Prefab.Companion.emptyPrefab
import com.aheidelbacher.algostorm.core.ecs.Prefab.Companion.prefabOf
import com.aheidelbacher.algostorm.test.ecs.ComponentMock
import org.junit.Test

import kotlin.test.assertEquals

class PrefabTest {
    @Test fun testEmptyPrefabHasNoComponents() {
        assertEquals(emptySet(), emptyPrefab().components)
    }

    @Test fun testEmptyPrefabOfHasNoComponents() {
        assertEquals(emptySet(), prefabOf().components)
    }

    @Test fun testEmptyPrefabConstructorHasNoComponents() {
        assertEquals(emptySet(), Prefab().components)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testPrefabOfRepeatedComponentTypesThrows() {
        prefabOf(ComponentMock(1), ComponentMock(2))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testPrefabConstructorWithRepeatedComponentTypesThrows() {
        Prefab(listOf(ComponentMock(1), ComponentMock(2)))
    }

    @Test fun prefabOfComponentHasEqualComponent() {
        val component = ComponentMock(284)
        assertEquals(setOf(component), prefabOf(component).components)
    }
}