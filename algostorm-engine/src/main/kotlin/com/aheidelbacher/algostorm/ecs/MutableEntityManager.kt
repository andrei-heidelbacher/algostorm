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

/** A mutable view of a collection of unique mutable entities. */
interface MutableEntityManager : EntityManager {
    override val entities: Iterable<MutableEntity>

    override operator fun get(id: Int): MutableEntity?

    /**
     * Creates an entity with the given components, adds it to this manager and
     * returns it.
     *
     * If creating and adding the entity to this manager fails, the entity
     * manager remains unchanged.
     *
     * @param components the initial components of the entity
     * @return the created entity
     * @throws IllegalStateException if there are too many entities in this
     * manager
     */
    fun create(components: Collection<Component>): MutableEntity

    /**
     * Removes the entity with the given id from this manager and returns it.
     *
     * @param id the id of the entity that should be removed
     * @return the removed entity if it exists in this manager when this method
     * is called, `null` otherwise
     * @throws IllegalArgumentException if [id] is not a valid id
     */
    fun remove(id: Int): MutableEntity?

    /** Removes all entities from this manager. */
    fun clear(): Unit
}
