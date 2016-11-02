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

package com.aheidelbacher.algostorm.state

import com.fasterxml.jackson.annotation.JsonCreator

import kotlin.reflect.KClass

/**
 * An abstract object within the game.
 *
 * Its behaviour is entirely determined by the components it contains. An entity
 * can contain at most one component of a specific type.
 *
 * @property id the unique positive identifier of this entity
 * @throws IllegalArgumentException if [id] is not positive
 */
data class Entity(val id: Int) {
    /** Factory that handles associating unique ids to created entities. */
    interface Factory {
        /**
         * Creates an entity with a unique id among all entities created by this
         * factory.
         *
         * @param components the components with which the entity is initialized
         * @return the created entity
         * @throws IllegalStateException if this factory created too many
         * entities
         */
        fun create(components: Collection<Component>): Entity
    }

    @JsonCreator
    constructor(id: Int, components: Collection<Component>) : this(id) {
        val uniqueComponents = components.distinctBy { it.javaClass }
        require(components.size == uniqueComponents.size) {
            "$this can't contain multiple components of the same type!"
        }
        components.associateByTo(componentMap) { it.javaClass.kotlin }
    }

    constructor(id: Int, vararg components: Component) : this(
            id = id,
            components = components.asList()
    )

    init {
        require(id > 0) { "$this id must be positive!" }
    }

    @Transient private val componentMap =
            hashMapOf<KClass<out Component>, Component>()

    /** An immutable view of this entity's components. */
    val components: Collection<Component> = componentMap.values

    /**
     * Checks whether this entity contains a component of the specified type.
     *
     * The specified type must be final.
     *
     * @param T the type of the component
     * @param type the class object of the component
     * @return `true` if this entity contains the specified component type,
     * `false` otherwise
     */
    operator fun <T : Component> contains(type: KClass<T>): Boolean =
            type in componentMap

    /**
     * Retrieves the component with the specified type.
     *
     * The specified type must be final.
     *
     * @param T the type of the component
     * @param type the class object of the component
     * @return the requested component, or `null` if this entity doesn't contain
     * the specified component type
     */
    operator fun <T : Component> get(type: KClass<T>): T? =
            type.java.cast(componentMap[type])

    /**
     * Sets the value of the specified component type.
     *
     * The specified type must be final.
     *
     * @param T the type of the component
     * @param value the new value of the component
     */
    fun <T : Component> set(value: T) {
        componentMap[value.javaClass.kotlin] = value
    }

    /**
     * Removes the component with the specified type and returns it.
     *
     * @param T the type of the component
     * @param type the class object of the component
     * @return the removed component if it exists in this entity when this
     * method is called, `null` otherwise
     */
    fun <T : Component> remove(type: KClass<T>): T? =
            type.java.cast(componentMap.remove(type))
}
