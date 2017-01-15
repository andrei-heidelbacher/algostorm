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

import kotlin.reflect.KClass

/**
 * A read-only view of an abstract object within the game.
 *
 * Its behaviour is entirely determined by the [components] it contains. An
 * entity can contain at most one component of a specific type.
 *
 * Entity references are not serializable.
 *
 * @property entityPool the entity pool which created this entity
 * @property id the unique positive identifier of this entity
 * @throws IllegalArgumentException if `id` is not valid
 */
abstract class EntityRef protected constructor(
        private val entityPool: EntityPool,
        val id: Id
) {
    /**
     * An entity id.
     *
     * @throws IllegalArgumentException if `value` is not positive
     */
    data class Id(val value: Int) {
        init {
            require(value > 0) { "$this must be positive!" }
        }
    }

    /** An immutable view of this entity's components. */
    abstract val components: Collection<Component>

    /**
     * Checks whether this entity contains a component of the specified `type`.
     *
     * @param type the class object of the component; must represent a final
     * type
     * @return `true` if this entity contains the specified component `type`,
     * `false` otherwise
     */
    operator fun contains(type: KClass<out Component>): Boolean =
            get(type) != null

    /**
     * Retrieves the component with the specified `type`.
     *
     * @param T the type of the component; must be final
     * @param type the class object of the component
     * @return the requested component, or `null` if this entity doesn't contain
     * the specified component `type`
     */
    abstract operator fun <T : Component> get(type: KClass<T>): T?

    final override fun hashCode(): Int = id.hashCode()

    /**
     * Two entity references are equal if and only if they were both created by
     * the same entity pool and their ids are equal.
     */
    final override fun equals(other: Any?): Boolean = other is EntityRef
            && id == other.id && entityPool === other.entityPool

    final override fun toString(): String =
            "EntityRef(${entityPool.hashCode()}, $id, $components)"
}
