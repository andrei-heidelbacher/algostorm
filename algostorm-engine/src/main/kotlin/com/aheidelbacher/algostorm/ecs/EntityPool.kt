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

/** A collection of unique entities. */
interface EntityPool {
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
