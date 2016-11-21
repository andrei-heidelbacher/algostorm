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

package com.aheidelbacher.algostorm.ecs

import com.aheidelbacher.algostorm.ecs.Entity.Companion.validateId

import kotlin.reflect.KClass

internal class MutableEntityManagerImpl(
        entities: Set<MutableEntity>
) : MutableEntityManager {
    /**
     * @throws IllegalArgumentException
     */
    internal class MutableEntityImpl(
            id: Int,
            components: Collection<Component>
    ) : MutableEntity(id) {
        init {
            val uniqueComponents = components.distinctBy { it.javaClass }
            require(uniqueComponents.size == components.size) {
                "There are multiple components of the same type in $components!"
            }
        }

        @Transient
        private val componentMap = components.associateByTo(
                hashMapOf<KClass<out Component>, Component>()
        ) { it.javaClass.kotlin }

        override val components: Iterable<Component> = componentMap.values

        override fun contains(type: KClass<out Component>): Boolean =
                type in componentMap

        override fun <T : Component> get(type: KClass<T>): T? =
                type.java.cast(componentMap[type])

        override fun <T : Component> remove(type: KClass<T>): T? =
                type.java.cast(componentMap.remove(type))

        override fun set(value: Component) {
            componentMap[value.javaClass.kotlin] = value
        }
    }

    @Transient
    private val entityMap = entities.associateByTo(hashMapOf(), Entity::id)

    @Transient
    private var nextId = entityMap.keys.max() ?: 1

    override val entities: Iterable<MutableEntity> = entityMap.values

    override fun create(components: Collection<Component>): MutableEntity {
        check(nextId < Int.MAX_VALUE) { "Too many entities in $this!" }
        val entity = MutableEntityImpl(nextId++, components)
        entityMap[entity.id] = entity
        return entity
    }

    override fun contains(id: Int): Boolean = validateId(id) in entityMap

    override fun get(id: Int): MutableEntity? = entityMap[validateId(id)]

    override fun remove(id: Int): MutableEntity? =
            entityMap.remove(validateId(id))

    override fun clear() {
        entityMap.clear()
    }
}
