/*
 * Copyright (c) 2017  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.andreihh.algostorm.core.ecs

import com.andreihh.algostorm.core.ecs.EntityRef.Id
import kotlin.reflect.KClass

/** A collection of unique entities. */
interface EntityPool : MutableEntityGroup {
    companion object {
        /**
         * Returns a default implementation of an entity pool.
         *
         * @param entities the initial entities contained by the pool
         * @return the entity pool
         */
        fun entityPoolOf(entities: Map<Id, Prefab>): EntityPool =
                EntityPoolImpl(entities)

        fun entityPoolOf(vararg entities: Pair<Id, Prefab>): EntityPool =
                entityPoolOf(mapOf(*entities))

        private class EntityPoolImpl(entities: Map<Id, Prefab>) : EntityPool {
            private inner class EntityRefImpl(
                    id: Id,
                    components: Collection<Component>
            ) : MutableEntityRef(this@EntityPoolImpl, id) {
                private val componentMap =
                        hashMapOf<KClass<out Component>, Component>()
                        //components.associateByTo(hashMapOf()) { it::class }

                init {
                    for (component in components) {
                        require(component::class !in componentMap) {
                            "Duplicated component type '${component::class}'!"
                        }
                        componentMap[component::class] = component
                    }
                }

                override val components: Collection<Component>
                    get() = componentMap.values

                @Suppress("unchecked_cast")
                override fun <T : Component> get(type: KClass<T>): T? =
                        componentMap[type] as T?

                @Suppress("unchecked_cast")
                override fun <T : Component> remove(type: KClass<T>): T? {
                    val component = componentMap.remove(type) as T?
                    rootGroup.onChanged(this)
                    return component
                }

                override fun set(component: Component) {
                    componentMap[component::class] = component
                    rootGroup.onChanged(this)
                }
            }

            private class EntityGroupImpl(
                    filter: (EntityRef) -> Boolean
            ) : MutableEntityGroup {
                private var filter: ((EntityRef) -> Boolean)? = filter
                private val entityMap = hashMapOf<Id, EntityRefImpl>()
                private val groups = hashSetOf<EntityGroupImpl>()

                override val isValid: Boolean
                    get() = filter != null

                override val entities: Iterable<EntityRefImpl>
                    get() = entityMap.values

                override fun get(id: Id): EntityRefImpl? = entityMap[id]

                override fun addGroup(
                        filter: (EntityRef) -> Boolean
                ): EntityGroupImpl {
                    check(isValid) {
                        "Can't add group when '$this' is invalid!"
                    }
                    val subgroup = EntityGroupImpl(filter)
                    groups += subgroup
                    entityMap.values.forEach(subgroup::onChanged)
                    return subgroup
                }

                override fun removeGroup(group: EntityGroup): Boolean =
                        if (group is EntityGroupImpl && group in groups) {
                            group.onCleared()
                            groups -= group
                            true
                        } else false

                internal fun onChanged(entity: EntityRefImpl) {
                    if (checkNotNull(filter).invoke(entity)) {
                        entityMap[entity.id] = entity
                        groups.forEach { it.onChanged(entity) }
                    } else {
                        onRemoved(entity)
                    }
                }

                internal fun onRemoved(entity: EntityRefImpl) {
                    if (entityMap.remove(entity.id) != null) {
                        groups.forEach { it.onRemoved(entity) }
                    }
                }

                internal fun onCleared() {
                    groups.forEach(EntityGroupImpl::onCleared)
                    groups.clear()
                    entityMap.clear()
                    filter = null
                }
            }

            private var nextId = 1
            private val rootGroup = EntityGroupImpl { true }

            init {
                for ((id, prefab) in entities) {
                    nextId = maxOf(nextId, id.value + 1)
                    rootGroup.onChanged(EntityRefImpl(id, prefab.components))
                }
            }

            override val entities: Iterable<MutableEntityRef>
                get() = rootGroup.entities

            override val isValid: Boolean
                get() = rootGroup.isValid

            override fun get(id: Id): MutableEntityRef? = rootGroup[id]

            override fun addGroup(
                    filter: (EntityRef) -> Boolean
            ): MutableEntityGroup = rootGroup.addGroup(filter)

            override fun removeGroup(group: EntityGroup): Boolean =
                    rootGroup.removeGroup(group)

            override fun create(prefab: Prefab): MutableEntityRef {
                check(nextId > 0) { "Too many entities created in '$this'!" }
                val entity = EntityRefImpl(Id(nextId++), prefab.components)
                rootGroup.onChanged(entity)
                return entity
            }

            override fun remove(id: Id): Boolean =
                    rootGroup[id]?.apply(rootGroup::onRemoved) != null

            override fun clear() {
                rootGroup.entities.toList().forEach(rootGroup::onRemoved)
            }
        }
    }

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
    fun clear()
}
