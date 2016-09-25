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

package com.aheidelbacher.algostorm.engine.tiled

import com.aheidelbacher.algostorm.engine.serialization.Serializer

interface Properties {
    companion object {
        operator fun invoke(properties: Map<String, Property>): Properties =
                object : Properties {
                    override val properties: Map<String, Property>
                        get() = properties
                }

        inline fun <reified T : Any> Properties.get(name: String): T? =
                getString(name)?.byteInputStream()?.use {
                    Serializer.readValue(it)
                }
    }

    /**
     * The existing properties.
     */
    val properties: Map<String, Property>

    /**
     * Checks whether the given property exists.
     *
     * @param name the name of the checked property
     * @return `true` if the given property [name] is contained in [properties],
     * `false` otherwise
     */
    operator fun contains(name: String): Boolean = name in properties

    fun getInt(name: String): Int? = properties[name]?.value as Int?

    fun getFloat(name: String): Float? = properties[name]?.value as Float?

    fun getBoolean(name: String): Boolean? = properties[name]?.value as Boolean?

    fun getString(name: String): String? = properties[name]?.value as String?

    fun getFile(name: String): File? = properties[name]?.value as File?

    fun getColor(name: String): Color? = properties[name]?.value as Color?
}
