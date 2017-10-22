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

import java.util.ServiceLoader
import kotlin.reflect.KClass

interface ComponentLibrary {
    companion object {
        private val typeToName =
                ServiceLoader.load(ComponentLibrary::class.java)
                        .flatMap { it.components.entries }
                        .map { (k, v) -> k to v }
                        .toMap()

        private val nameToType = typeToName.map { (k, v) -> v to k }.toMap()

        operator fun get(name: String): KClass<out Component>? = nameToType[name]

        operator fun get(type: KClass<out Component>): String? = typeToName[type]

        operator fun contains(name: String): Boolean = name in nameToType

        operator fun contains(type: KClass<out Component>): Boolean =
                type in typeToName
    }

    val components: Map<KClass<out Component>, String>
}
