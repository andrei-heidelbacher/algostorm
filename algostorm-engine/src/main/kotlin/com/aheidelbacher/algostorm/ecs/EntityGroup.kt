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

/** A read-only view group of filtered read-only entities. */
interface EntityGroup {
    companion object {
        /**
         * Asserts that this entity group is valid.
         *
         * @throws IllegalStateException if this entity group is not valid
         */
        fun EntityGroup.checkIsValid() {
            check(isValid) { "$this invalid entity group!" }
        }
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
     * @throws IllegalArgumentException if `id` is not a valid id
     */
    operator fun contains(id: Int): Boolean = get(id) != null

    /**
     * Returns the entity with the given `id`.
     *
     * @param id the id of the requested entity
     * @return the requested entity, or `null` if it doesn't exist in this group
     * @throws IllegalArgumentException if `id` is not a valid id
     */
    operator fun get(id: Int): EntityRef?

    /**
     * Creates a subgroup with the given `name`, which contains those entities
     * from this group that satisfy the given `filter`.
     *
     * @param name the name of this subgroup
     * @param filter the filter which must return `true` for an entity to be
     * included in the new group
     * @return the new subgroup
     * @throws IllegalArgumentException if a subgroup with the same `name`
     * already exists
     * @throws IllegalStateException if this group is invalid
     */
    fun addGroup(name: String, filter: (EntityRef) -> Boolean): EntityGroup

    /**
     * Removes the subgroup with the given `name`.
     *
     * The removed group will be invalidated and will no longer contain any
     * entities.
     *
     * @param name the name of the subgroup which should be removed
     * @return `true` if the subgroup was removed, `false` if it doesn't exist
     * in this group
     */
    fun removeGroup(name: String): Boolean
}
