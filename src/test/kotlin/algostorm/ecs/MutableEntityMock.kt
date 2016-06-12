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

import kotlin.reflect.KClass

/**
 * A mutable entity implementation that should be used for testing purposes.
 */
class MutableEntityMock(id: Int) : MutableEntity(id) {
    private val componentMap = hashMapOf<KClass<out Component>, Component>()

    constructor(id: Int, components: Iterable<Component>) : this(id) {
        components.forEach { set(it) }
    }

    override val components: List<Component>
        get() = componentMap.values.toList()

    override fun <T : Component> get(type: KClass<T>): T? =
            type.java.cast(componentMap[type])

    override fun <T : Component> remove(type: KClass<T>) {
        componentMap.remove(type)
    }

    override fun <T : Component> set(type: KClass<T>, value: T) {
        componentMap[type] = value
    }

    /**
     * Checks if the entity components are equal to the given [components].
     *
     * @param components the expected components of this entity
     * @throws IllegalStateException if this entity has different components
     */
    fun verify(components: Iterable<Component>) {
        val actualComponents = this.components.toSet()
        val expectedComponents = components.toSet()
        check(actualComponents == expectedComponents) {
            "Entity $id doesn't have the indicated components!\n" +
                    "Expected: $expectedComponents\n" +
                    "Actual: $actualComponents"
        }
    }
}
