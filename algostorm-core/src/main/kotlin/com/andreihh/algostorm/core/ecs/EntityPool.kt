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

/** A collection of unique entities. */
class EntityPool : EntityGroup {
    private var nextId = 1
    private val entityMap = hashMapOf<Id, EntityRef>()

    override fun iterator(): Iterator<EntityRef> = entityMap.values.iterator()

    override fun get(id: Id): EntityRef? = entityMap[id]

    override fun filter(predicate: (EntityRef) -> Boolean): EntityGroup =
        Group(parent = this, predicate = predicate)

    private fun create(id: Id, components: Collection<Component>): EntityRef {
        check(id !in entityMap) { "Id '$id' is already used!" }
        val entity = EntityRef(id)
        for (component in components) {
            require(component::class !in entity) {
                "Duplicated component type '${component::class}'!"
            }
            entity.set(component)
        }
        nextId = maxOf(nextId, id.value + 1)
        entityMap[id] = entity
        return entity
    }

    /**
     * Creates an entity from the given `components`, adds it to this pool and
     * returns it.
     *
     * If creating and adding the entity to this pool fails, the entity pool
     * remains unchanged.
     *
     * @param components the initial components of the entity
     * @return the created entity
     * @throws IllegalArgumentException if there are duplicated component types
     * @throws IllegalStateException if there are too many entities in this pool
     */
    fun create(components: Collection<Component>): EntityRef {
        check(nextId > 0) { "Too many entities created in '$this'!" }
        return create(Id(nextId++), components)
    }

    /**
     * Removes the entity with the given `id` from this pool.
     *
     * @param id the id of the entity that should be removed
     * @return `true` if the entity was removed, `false` if it didn't exist in
     * this pool
     */
    fun remove(id: Id): Boolean = entityMap.remove(id) != null

    /** Removes all the entities from this pool. */
    fun clear() {
        entityMap.clear()
    }

    companion object {
        /**
         * Returns an entity pool containing the given initial [entities].
         *
         * @throws IllegalArgumentException if there is an entity with
         * duplicated component types
         */
        fun of(entities: Map<Id, Collection<Component>>): EntityPool {
            val pool = EntityPool()
            for ((id, components) in entities) {
                pool.create(id, components)
            }
            return pool
        }
    }

    private class Group(
        private val parent: EntityGroup,
        private val predicate: (EntityRef) -> Boolean
    ) : EntityGroup {

        override fun iterator(): Iterator<EntityRef> =
            parent.asSequence().filter(predicate).iterator()

        override fun get(id: Id): EntityRef? = parent[id]?.takeIf(predicate)

        override fun filter(predicate: (EntityRef) -> Boolean): EntityGroup =
            Group(parent = this, predicate = predicate)
    }
}
