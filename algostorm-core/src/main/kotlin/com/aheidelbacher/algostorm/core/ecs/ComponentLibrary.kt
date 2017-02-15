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

import java.util.concurrent.ConcurrentHashMap

import kotlin.reflect.KClass

object ComponentLibrary {
    private val components = ConcurrentHashMap<String, KClass<out Component>>()

    /**
     * @throws IllegalArgumentException if the given `type` doesn't have a
     * simple name (for example, if it is an anonymous class)
     * @throws IllegalStateException if the given `type` has a conflicting
     * simple name with an already registered type
     */
    fun registerComponentType(type: KClass<out Component>) {
        val name = requireNotNull(type.simpleName)
        check(name !in components || components[name] == type) {
            "$type name conflicts with ${components[name]}!"
        }
        components[name] = type
    }

    operator fun contains(type: KClass<out Component>): Boolean =
            components[requireNotNull(type.simpleName)] == type

    operator fun contains(type: String): Boolean = get(type) != null

    operator fun get(type: String): KClass<out Component>? = components[type]
}
