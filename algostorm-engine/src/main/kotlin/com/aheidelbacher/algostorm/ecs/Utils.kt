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

import kotlin.reflect.KClass

/**
 * Validates the given entity id and returns it.
 *
 * @param id the entity id which should be validated
 * @return the given entity id
 * @throws IllegalArgumentException if [id] is not positive
 */
fun validateId(id: Int): Int {
    require(id > 0) { "Id $id must be positive!" }
    return id
}

/**
 * Returns a default implementation of an entity manager.
 *
 * @param entities the initial entities which should be contained by the manager
 * @return the entity manager
 * @throws IllegalArgumentException if any given entity id is not valid or if
 * any given entity contains multiple components of the same type
 */
fun entityManagerOf(entities: Map<Int, Collection<Component>>): EntityManager =
        mutableEntityManagerOf(entities)

/**
 * Returns a default implementation of a mutable entity manager.
 *
 * @param entities the initial entities which should be contained by the manager
 * @return the mutable entity manager
 * @throws IllegalArgumentException if any given entity id is not valid or if
 * any given entity contains multiple components of the same type
 */
fun mutableEntityManagerOf(
        entities: Map<Int, Collection<Component>>
): MutableEntityManager = MutableEntityManagerImpl(entities)

private class MutableEntityManagerImpl(
        entities: Map<Int, Collection<Component>>
) : MutableEntityManager {
    private class MutableEntityImpl(
            id: Int,
            components: Collection<Component>
    ) : MutableEntity(id) {
        private val componentMap = components.associateByTo(
                hashMapOf<KClass<out Component>, Component>()
        ) { it.javaClass.kotlin }

        override val components: Iterable<Component>
            get() = componentMap.values

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

    private val entityMap = entities
            .map { MutableEntityImpl(it.key, it.value) }
            .associateByTo(hashMapOf(), MutableEntityImpl::id)
    private var nextId = 1 + (entityMap.keys.max() ?: 0)

    override val entities: Iterable<MutableEntity>
        get() = entityMap.values

    override fun create(components: Collection<Component>): MutableEntity {
        require(
                components.distinctBy { it.javaClass }.size == components.size
        ) { "Multiple components of the same type given in $components!" }
        check(nextId > 0) { "Too many entities created in $this!" }
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
