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

package com.aheidelbacher.algostorm.core.ecs

/**
 * An immutable template for creating and initializing entities.
 *
 * Prefabs are serializable.
 *
 * @property components the initial components which the instantiated entities
 * should contain
 * @throws IllegalArgumentException if there are given multiple components of
 * the same type
 */
data class Prefab private constructor(val components: Set<Component>) {
    companion object {
        private val empty = Prefab(emptySet())

        /** Returns an empty prefab with no components. */
        fun emptyPrefab(): Prefab = empty

        /**
         * Creates a prefab from the given `components`.
         *
         * @param components the components of the prefab
         * @return the prefab
         * @throws IllegalArgumentException if there are given multiple
         * components of the same type
         */
        fun prefabOf(vararg components: Component): Prefab =
                if (components.isEmpty()) emptyPrefab()
                else Prefab(components.toSet())

        /** Builds a prefab containing the current components of this entity. */
        fun EntityRef.toPrefab(): Prefab = Prefab(components)
    }

    init {
        val size = components.size
        require(components.distinctBy { it.javaClass }.size == size) {
            "Multiple components of the same type given in $components!"
        }
    }

    constructor(components: Collection<Component>) : this(components.toSet())

    constructor() : this(emptySet())
}
