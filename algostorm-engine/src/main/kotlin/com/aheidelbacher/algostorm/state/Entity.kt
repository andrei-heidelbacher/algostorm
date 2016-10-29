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

import kotlin.reflect.KClass

data class Entity(val id: Int) {
    constructor(id: Int, components: Iterable<Component>) : this(id) {
        components.associateByTo(componentMap) { it.javaClass.kotlin }
    }

    constructor(id: Int, vararg components: Component) : this(id) {
        components.associateByTo(componentMap) { it.javaClass.kotlin }
    }

    @Transient private val componentMap =
            hashMapOf<KClass<out Component>, Component>()

    val components: Iterable<Component> = componentMap.values

    operator fun <T : Component> contains(type: KClass<T>): Boolean =
            type in componentMap

    operator fun <T : Component> get(type: KClass<T>): T? =
            type.java.cast(componentMap[type])

    operator fun <T : Component> set(type: KClass<T>, value: T) {
        componentMap[type] = value
    }
}
