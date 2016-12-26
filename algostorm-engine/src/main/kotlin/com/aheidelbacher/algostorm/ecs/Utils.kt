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

package com.aheidelbacher.algostorm.ecs

import com.aheidelbacher.algostorm.ecs.EntityGroup.Companion.checkIsValid
import com.aheidelbacher.algostorm.ecs.EntityRef.Companion.validateComponents
import com.aheidelbacher.algostorm.ecs.EntityRef.Companion.validateId

import kotlin.reflect.KClass

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

// -----------------------------------------------------------------

/**
 * Returns a default implementation of an entity pool.
 *
 * @param entities the initial entities which should be contained by the pool
 * @return the entity pool
 * @throws IllegalArgumentException if any given entity id is not valid or if
 * any given entity contains multiple components of the same type
 */
fun entityPoolOf(entities: Map<Int, Collection<Component>>): EntityPool =
        EntityPoolImpl(entities)

private class EntityRefImpl(
        private val entityPool: EntityPoolImpl,
        id: Int,
        components: Collection<Component> = emptyList()
) : MutableEntityRef(entityPool, id) {
    private val componentTable = components.associateByTo(
            hashMapOf<KClass<out Component>, Component>()
    ) { it.javaClass.kotlin }

    override var isValid: Boolean = true
        private set

    /** Invalidates this entity reference. */
    fun invalidate() {
        componentTable.clear()
        isValid = false
    }

    override val components: Collection<Component>
        get() = componentTable.values

    override fun <T : Component> get(type: KClass<T>): T? =
            type.java.cast(componentTable[type])

    override fun <T : Component> remove(type: KClass<T>): T? {
        checkIsValid()
        val component = type.java.cast(componentTable.remove(type))
        entityPool.onChanged(this)
        return component
    }

    override fun set(component: Component) {
        checkIsValid()
        componentTable[component.javaClass.kotlin] = component
        entityPool.onChanged(this)
    }
}

private class EntityGroupImpl(
        filter: (EntityRef) -> Boolean
) : MutableEntityGroup {
    private var filter: ((EntityRef) -> Boolean)? = filter
    private val entityTable = hashMapOf<Int, EntityRefImpl>()
    private val groups = hashMapOf<String, EntityGroupImpl>()

    override val isValid: Boolean
        get() = filter != null

    override val entities: Iterable<EntityRefImpl>
        get() = entityTable.values

    override fun get(id: Int): EntityRefImpl? = entityTable[validateId(id)]

    override fun contains(id: Int): Boolean = validateId(id) in entityTable

    override fun addGroup(
            name: String,
            filter: (EntityRef) -> Boolean
    ): EntityGroupImpl {
        checkIsValid()
        return EntityGroupImpl(filter).apply {
            require(name !in groups) { "$this already contains $name!" }
            groups[name] = this
            entityTable.forEach { this.onChanged(it.value) }
        }
    }

    override fun removeGroup(name: String): Boolean {
        groups[name]?.onCleared()
        return groups.remove(name) != null
    }

    fun onChanged(entity: EntityRefImpl) {
        if (filter?.invoke(entity) ?: error("$this was invalidated!")) {
            entityTable[entity.id] = entity
            groups.forEach { it.value.onChanged(entity) }
        } else {
            onRemoved(entity)
        }
    }

    fun onRemoved(entity: EntityRefImpl) {
        if (entityTable.remove(entity.id) != null) {
            groups.forEach { it.value.onRemoved(entity) }
        }
    }

    fun onCleared() {
        groups.forEach { it.value.onCleared() }
        groups.clear()
        entityTable.clear()
        filter = null
    }
}

private class EntityPoolImpl(
        entities: Map<Int, Collection<Component>>
) : EntityPool {
    private var nextId = 1 + (entities.keys.max() ?: 0)

    override val group = EntityGroupImpl { true }

    init {
        entities.forEach {
            validateId(it.key)
            validateComponents(it.value)
        }
        entities.forEach {
            group.onChanged(EntityRefImpl(this, it.key, it.value))
        }
    }

    override fun create(components: Collection<Component>): EntityRefImpl {
        validateComponents(components)
        check(nextId > 0) { "Too many entities created in $this!" }
        val entity = EntityRefImpl(this, nextId++, components)
        group.onChanged(entity)
        return entity
    }

    override fun delete(id: Int): Boolean = group[validateId(id)]?.apply {
        group.onRemoved(this)
        invalidate()
    } != null

    override fun clear() {
        group.entities.toList().forEach { group.onRemoved(it) }
    }

    fun onChanged(entity: EntityRefImpl) {
        group.onChanged(entity)
    }
}
