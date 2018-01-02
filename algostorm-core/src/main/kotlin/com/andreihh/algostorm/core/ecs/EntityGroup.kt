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

/** A read-only view group of filtered entities. */
interface EntityGroup : Iterable<EntityRef> {
    /** Returns whether this group contains an entity with the given [id]. */
    operator fun contains(id: Id): Boolean = get(id) != null

    /**
     * Returns the entity with the given [id], or `null` if it doesn't exist in
     * this group.
     */
    operator fun get(id: Id): EntityRef?

    /**
     * Returns a subgroup which contains those entities from this group that
     * satisfy the given [predicate].
     */
    fun filter(predicate: (EntityRef) -> Boolean): EntityGroup
}
