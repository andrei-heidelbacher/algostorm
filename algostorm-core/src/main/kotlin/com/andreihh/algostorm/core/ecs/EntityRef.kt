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

import kotlin.reflect.KClass

/**
 * A view of an abstract object within the game.
 *
 * Its behaviour is entirely determined by the [components] it contains. An
 * entity can contain at most one component of a specific type.
 *
 * @property id the unique positive identifier of this entity
 */
class EntityRef(val id: Id) {
    /**
     * An entity id.
     *
     * @property value the value of this id
     * @throws IllegalArgumentException if [value] is not positive
     */
    data class Id(val value: Int) {
        init {
            require(value > 0) { "'$value' must be positive!" }
        }

        /** Returns the [value] of this id. */
        override fun toString(): String = "$value"
    }

    private val componentMap = hashMapOf<KClass<out Component>, Component>()

    /** An immutable view of this entity's components. */
    val components: Collection<Component> get() = componentMap.values

    /**
     * Returns whether this entity contains a component of the given final
     * [type].
     */
    operator fun contains(type: KClass<out Component>): Boolean =
        get(type) != null

    /**
     * Returns the component with the specified final [type], or `null` if this
     * entity doesn't contain the given component type.
     */
    @Suppress("unchecked_cast")
    operator fun <T : Component> get(type: KClass<T>): T? =
        componentMap[type] as T?

    /** Sets the value of the specified [component] type. */
    fun set(component: Component) {
        componentMap[component::class] = component
    }

    /**
     * Removes the component with the specified `type` and returns it.
     *
     * @param T the type of the component; must be final
     * @param type the class object of the component
     * @return the removed component if it exists in this entity when this
     * method is called, `null` otherwise
     */
    @Suppress("unchecked_cast")
    fun <T : Component> remove(type: KClass<T>): T? =
        componentMap.remove(type) as T?

    override fun hashCode(): Int = id.value

    /** Two entity references are equal if they have the same [id]. */
    override fun equals(other: Any?): Boolean =
        other is EntityRef && id == other.id

    companion object {
        fun entityTemplate(init: Builder.() -> Unit): Collection<Component> =
            Builder().apply(init).build()
    }

    class Builder {
        private val components = arrayListOf<Component>()

        operator fun Component.unaryPlus() {
            components += this
        }

        fun build(): Collection<Component> = components.toList()
    }
}
