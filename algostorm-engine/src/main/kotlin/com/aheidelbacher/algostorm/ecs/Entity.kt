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

import kotlin.reflect.KClass

/**
 * A read-only view of an abstract object within the game.
 *
 * Its behaviour is entirely determined by the components it contains. An entity
 * can contain at most one component of a specific type.
 *
 * @property id the unique positive identifier of this entity
 * @throws IllegalArgumentException if [id] is not valid
 */
abstract class Entity(val id: Int) {
    companion object {
        /**
         * Validates the given id and returns it.
         *
         * @param id the id which should be validated
         * @return the given id
         * @throws IllegalArgumentException if [id] is not positive
         */
        fun validateId(id: Int): Int {
            require(id > 0) { "Id $id must be positive!" }
            return id
        }
    }

    init {
        validateId(id)
    }

    /** An immutable view of this entity's components. */
    abstract val components: Collection<Component>

    /**
     * Checks whether this entity contains a component of the specified type.
     *
     * @param type the class object of the component; must represent a final
     * type
     * @return `true` if this entity contains the specified component type,
     * `false` otherwise
     */
    abstract operator fun contains(type: KClass<out Component>): Boolean

    /**
     * Retrieves the component with the specified type.
     *
     * @param T the type of the component; must be final
     * @param type the class object of the component
     * @return the requested component, or `null` if this entity doesn't contain
     * the specified component type
     */
    abstract operator fun <T : Component> get(type: KClass<T>): T?

    /** Two entities are equal if and only if their ids are equal. */
    final override fun equals(other: Any?): Boolean =
            other is Entity && id == other.id

    final override fun hashCode(): Int = id

    final override fun toString(): String =
            "Entity(id=$id, components=$components)"
}
