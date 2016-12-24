/*
 * Copyright (c) 2016  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.aheidelbacher.algostorm.systems.state

import com.fasterxml.jackson.annotation.JsonCreator

import com.aheidelbacher.algostorm.ecs.Component

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
class Entity(id: Int) : com.aheidelbacher.algostorm.ecs.MutableEntity(id) {
    /** Factory that handles associating unique ids to created entities. */
    interface Factory {
        companion object {
            /**
             * Returns a default implementation of a factory.
             *
             * @return a factory which assigns ids in ascending order starting
             * from `1`
             */
            operator fun invoke(): Factory = object : Factory {
                private var nextEntityId = 1

                override fun create(components: Collection<Component>): Entity {
                    check(nextEntityId < Int.MAX_VALUE) {
                        "$this created too many entities!"
                    }
                    return Entity(nextEntityId++, components)
                }
            }
        }

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

    @Transient private val componentMap =
            hashMapOf<KClass<out Component>, Component>()

    override val components: Collection<Component> = componentMap.values

    override operator fun contains(type: KClass<out Component>): Boolean =
            type in componentMap

    override operator fun <T : Component> get(type: KClass<T>): T? =
            type.java.cast(componentMap[type])

    override fun set(value: Component) {
        componentMap[value.javaClass.kotlin] = value
    }

    override fun <T : Component> remove(type: KClass<T>): T? =
            type.java.cast(componentMap.remove(type))
}
