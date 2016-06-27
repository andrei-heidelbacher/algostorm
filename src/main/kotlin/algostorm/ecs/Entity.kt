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

package algostorm.ecs

/**
 * A game object containing a set of named properties.
 *
 * Two entities are equal if and only if they have the same [id]. Raw entities
 * should not be serialized; instead, serialize the `id` and underlying
 * properties.
 *
 * This is a read-only view, but the underlying implementation might be mutable.
 *
 * All the entity properties should be immutable data-types or primitive types.
 *
 * @property id the unique identifier of the entity
 */
abstract class Entity(val id: Int) {
    /**
     * Returns the property with the given name.
     *
     * @param property the name of the property
     * @return the requested property, or `null` if this entity doesn't contain
     * the property
     */
    abstract fun get(property: String): Any?

    /**
     * Checks whether this entity contains the property with the given name.
     *
     * @param property the name of the property
     * @return `true` if this entity contains the given property, `false`
     * otherwise
     */
    operator fun contains(property: String): Boolean = get(property) != null

    final override fun equals(other: Any?): Boolean =
            other is Entity && id == other.id

    final override fun hashCode(): Int = id
}
