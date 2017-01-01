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

/** A read-only view of a collection of unique entities. */
interface EntityManager {
    /** A read-only view of the entities in this manager. */
    val entities: Iterable<Entity>

    /**
     * Checks whether this manager contains an entity with the given id.
     *
     * @param id the id of the requested entity
     * @return `true` if the entity with the given id exists in this manager,
     * `false` otherwise
     * @throws IllegalArgumentException if [id] is not a valid id
     */
    operator fun contains(id: Int): Boolean

    /**
     * Returns the entity with the given id.
     *
     * @param id the id of the requested entity
     * @return the requested entity, or `null` if it doesn't exist in this
     * manager
     * @throws IllegalArgumentException if [id] is not a valid id
     */
    operator fun get(id: Int): Entity?
}
