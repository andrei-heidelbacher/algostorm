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
import com.andreihh.algostorm.core.ecs.Prefab.Companion.toPrefab

/** A read-only view group of filtered read-only entities. */
interface EntityGroup {
    companion object {
        /** Returns the current state of the entities in this group. */
        fun EntityGroup.getSnapshot(): Map<Id, Prefab> =
                entities.associate { it.id to it.toPrefab() }
    }

    /** A read-only view of the entities in this group. */
    val entities: Iterable<EntityRef>

    /**
     * Whether this group is valid or not.
     *
     * An invalid group was disconnected from its parent group and will always
     * be empty.
     */
    val isValid: Boolean

    /**
     * Checks whether this group contains an entity with the given `id`.
     *
     * @param id the id of the requested entity
     * @return `true` if the requested entity exists in this group, `false`
     * otherwise
     */
    operator fun contains(id: Id): Boolean = get(id) != null

    /**
     * Returns the entity with the given `id`.
     *
     * @param id the id of the requested entity
     * @return the requested entity, or `null` if it doesn't exist in this group
     */
    operator fun get(id: Id): EntityRef?

    /**
     * Creates a subgroup which contains those entities from this group that
     * satisfy the given `filter`.
     *
     * @param filter the filter which must return `true` for an entity to be
     * included in the new group
     * @return the new subgroup
     * @throws IllegalStateException if this group is invalid
     */
    fun addGroup(filter: (EntityRef) -> Boolean): EntityGroup

    /**
     * Removes the given subgroup.
     *
     * The removed group will be invalidated and will no longer contain any
     * entities.
     *
     * @param group the subgroup which should be removed
     * @return `true` if the subgroup was removed, `false` if it doesn't exist
     * in this group
     */
    fun removeGroup(group: EntityGroup): Boolean
}
