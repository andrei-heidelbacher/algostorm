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

package com.aheidelbacher.algostorm.core.ecs

import java.io.FileNotFoundException
import java.util.Properties
import java.util.ServiceConfigurationError

import kotlin.reflect.KClass

object ComponentLibrary {
    private val serviceFile = "/META-INF/components.properties"
    private val nameToType = try {
        val stream = javaClass.getResourceAsStream(serviceFile)
                ?: throw FileNotFoundException("'$serviceFile' not found!")
        val properties = Properties()
        properties.load(stream)
        val components = hashMapOf<String, KClass<out Component>>()
        for (name in properties.propertyNames()) {
            val typeName = properties.getProperty(name as String)
            val type = Class.forName(typeName)
            require(Component::class.java.isAssignableFrom(type)) {
                "'$typeName' is not a component!"
            }
            @Suppress("unchecked_cast")
            components[name] = type.kotlin as KClass<out Component>
        }
        components
    } catch (e: Exception) {
        throw ServiceConfigurationError(e.message)
    }

    private val typeToName = nameToType.map { (k, v) -> v to k }.toMap()

    operator fun get(name: String): KClass<out Component>? = nameToType[name]

    operator fun get(type: KClass<out Component>): String? = typeToName[type]

    operator fun contains(name: String): Boolean = name in nameToType

    operator fun contains(type: KClass<out Component>): Boolean =
            type in typeToName
}
