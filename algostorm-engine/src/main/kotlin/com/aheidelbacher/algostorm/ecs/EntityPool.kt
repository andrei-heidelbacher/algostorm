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

package com.aheidelbacher.algostorm.ecs

import com.aheidelbacher.algostorm.ecs.EntityRef.Id

import kotlin.reflect.KClass

/** A collection of unique entities. */
interface EntityPool {
    companion object {
        /**
         * Returns a default implementation of an entity pool.
         *
         * @param entities the initial entities contained by the pool
         * @return the entity pool
         */
        fun entityPoolOf(entities: Map<Id, Prefab>): EntityPool =
                EntityPoolImpl(entities)

        private class EntityRefImpl(
                private val entityPool: EntityPoolImpl,
                id: Id,
                prefab: Prefab
        ) : MutableEntityRef(entityPool, id) {
            private val componentTable = prefab.components.associateByTo(
                    hashMapOf<KClass<out Component>, Component>()
            ) { it.javaClass.kotlin }

            override val components: Collection<Component>
                get() = componentTable.values

            override fun <T : Component> get(type: KClass<T>): T? =
                    type.java.cast(componentTable[type])

            override fun <T : Component> remove(type: KClass<T>): T? {
                val component = type.java.cast(componentTable.remove(type))
                entityPool.onChanged(this)
                return component
            }

            override fun set(component: Component) {
                componentTable[component.javaClass.kotlin] = component
                entityPool.onChanged(this)
            }
        }

        private class EntityGroupImpl(
                filter: (EntityRef) -> Boolean
        ) : MutableEntityGroup {
            private var filter: ((EntityRef) -> Boolean)? = filter
            private val entityTable = hashMapOf<Id, EntityRefImpl>()
            private val groups = hashSetOf<EntityGroupImpl>()

            override val isValid: Boolean
                get() = filter != null

            override val entities: Iterable<EntityRefImpl>
                get() = entityTable.values

            override fun get(id: Id): EntityRefImpl? = entityTable[id]

            override fun contains(id: Id): Boolean = id in entityTable

            override fun addGroup(
                    filter: (EntityRef) -> Boolean
            ): EntityGroupImpl {
                check(isValid) { "Can't add group when $this is invalid!" }
                val subgroup = EntityGroupImpl(filter)
                groups.add(subgroup)
                entityTable.forEach { subgroup.onChanged(it.value) }
                return subgroup
            }

            override fun removeGroup(group: EntityGroup): Boolean {
                if (group is EntityGroupImpl && group in groups) {
                    group.onCleared()
                    groups.remove(group)
                    return true
                }
                return false
            }

            fun onChanged(entity: EntityRefImpl) {
                if (filter?.invoke(entity) ?: error("$this was invalidated!")) {
                    entityTable[entity.id] = entity
                    groups.forEach { it.onChanged(entity) }
                } else {
                    onRemoved(entity)
                }
            }

            fun onRemoved(entity: EntityRefImpl) {
                if (entityTable.remove(entity.id) != null) {
                    groups.forEach { it.onRemoved(entity) }
                }
            }

            fun onCleared() {
                groups.forEach { it.onCleared() }
                groups.clear()
                entityTable.clear()
                filter = null
            }
        }

        private class EntityPoolImpl(
                entities: Map<Id, Prefab>
        ) : EntityPool {
            private var nextId =
                    1 + (entities.keys.maxBy { it.value }?.value ?: 0)

            override val group = EntityGroupImpl { true }

            init {
                entities.forEach {
                    group.onChanged(EntityRefImpl(this, it.key, it.value))
                }
            }

            override fun create(prefab: Prefab): EntityRefImpl {
                check(nextId > 0) { "Too many entities created in $this!" }
                val entity = EntityRefImpl(this, Id(nextId++), prefab)
                group.onChanged(entity)
                return entity
            }

            override fun remove(id: Id): Boolean = group[id]?.apply {
                group.onRemoved(this)
            } != null

            override fun clear() {
                group.entities.toList().forEach { group.onRemoved(it) }
            }

            fun onChanged(entity: EntityRefImpl) {
                group.onChanged(entity)
            }
        }
    }

    /** The entity group containing all the entities in this pool. */
    val group: MutableEntityGroup

    /**
     * Creates an entity from the given `prefab`, adds it to this pool and
     * returns it.
     *
     * If creating and adding the entity to this pool fails, the entity pool
     * remains unchanged.
     *
     * @param prefab the prefab containing the initial components of the entity
     * @return the created entity
     * @throws IllegalStateException if there are too many entities in this pool
     */
    fun create(prefab: Prefab): MutableEntityRef

    /**
     * Removes the entity with the given `id` from this pool.
     *
     * @param id the id of the entity that should be removed
     * @return `true` if the entity was removed, `false` if it didn't exist in
     * this pool
     */
    fun remove(id: Id): Boolean

    /** Removes all the entities from this pool. */
    fun clear(): Unit
}
