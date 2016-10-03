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

package com.aheidelbacher.algostorm.engine.state

import com.aheidelbacher.algostorm.engine.state.Property.BooleanProperty
import com.aheidelbacher.algostorm.engine.state.Property.ColorProperty
import com.aheidelbacher.algostorm.engine.state.Property.FileProperty
import com.aheidelbacher.algostorm.engine.state.Property.FloatProperty
import com.aheidelbacher.algostorm.engine.state.Property.IntProperty
import com.aheidelbacher.algostorm.engine.state.Property.StringProperty

/** A read-only view of a bag of properties. */
interface Properties {
    /** The existing properties. */
    val properties: Map<String, Property>

    /**
     * Checks whether the given property exists.
     *
     * @param name the name of the checked property
     * @return `true` if the given property [name] is contained in [properties],
     * `false` otherwise
     */
    operator fun contains(name: String): Boolean = name in properties

    /**
     * Returns the value of the requested property.
     *
     * @param name the name of the requested property
     * @return the value of the property or `null` if the property doesn't exist
     * @throws ClassCastException if the property exists, but is not of type
     * [IntProperty]
     */
    fun getInt(name: String): Int? = (properties[name] as IntProperty?)?.value

    /**
     * Returns the value of the requested property.
     *
     * @param name the name of the requested property
     * @return the value of the property or `null` if the property doesn't exist
     * @throws ClassCastException if the property exists, but is not of type
     * [FloatProperty]
     */
    fun getFloat(name: String): Float? =
            (properties[name] as FloatProperty?)?.value

    /**
     * Returns the value of the requested property.
     *
     * @param name the name of the requested property
     * @return the value of the property or `null` if the property doesn't exist
     * @throws ClassCastException if the property exists, but is not of type
     * [BooleanProperty]
     */
    fun getBoolean(name: String): Boolean? =
            (properties[name] as BooleanProperty?)?.value

    /**
     * Returns the value of the requested property.
     *
     * @param name the name of the requested property
     * @return the value of the property or `null` if the property doesn't exist
     * @throws ClassCastException if the property exists, but is not of type
     * [StringProperty]
     */
    fun getString(name: String): String? =
            (properties[name] as StringProperty?)?.value

    /**
     * Returns the value of the requested property.
     *
     * @param name the name of the requested property
     * @return the value of the property or `null` if the property doesn't exist
     * @throws ClassCastException if the property exists, but is not of type
     * [FileProperty]
     */
    fun getFile(name: String): File? =
            (properties[name] as FileProperty?)?.value

    /**
     * Returns the value of the requested property.
     *
     * @param name the name of the requested property
     * @return the value of the property or `null` if the property doesn't exist
     * @throws ClassCastException if the property exists, but is not of type
     * [ColorProperty]
     */
    fun getColor(name: String): Color? =
            (properties[name] as ColorProperty?)?.value
}
