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

/** A bag of mutable properties. */
interface MutableProperties : Properties {
    override val properties: MutableMap<String, Property>

    /**
     * Removes the property with the given name.
     *
     * @param name the name of the property that should be removed
     */
    fun remove(name: String) {
        properties.remove(name)
    }

    /**
     * Sets the value of the property with the given name.
     *
     * @param name the name of the property that should be set
     * @param value the value of the integer property
     */
    operator fun set(name: String, value: Int) {
        properties[name] = Property(value)
    }

    /**
     * Sets the value of the property with the given name.
     *
     * @param name the name of the property that should be set
     * @param value the value of the float property
     */
    operator fun set(name: String, value: Float) {
        properties[name] = Property(value)
    }

    /**
     * Sets the value of the property with the given name.
     *
     * @param name the name of the property that should be set
     * @param value the value of the boolean property
     */
    operator fun set(name: String, value: Boolean) {
        properties[name] = Property(value)
    }

    /**
     * Sets the value of the property with the given name.
     *
     * @param name the name of the property that should be set
     * @param value the value of the string property
     */
    operator fun set(name: String, value: String) {
        properties[name] = Property(value)
    }

    /**
     * Sets the value of the property with the given name.
     *
     * The string representation of the file property is saved.
     *
     * @param name the name of the property that should be set
     * @param value the value of the file property
     */
    operator fun set(name: String, value: File) {
        properties[name] = Property(value)
    }

    /**
     * Sets the value of the property with the given name.
     *
     * The string representation of the color property is saved.
     *
     * @param name the name of the property that should be set
     * @param value the value of the color property
     */
    operator fun set(name: String, value: Color) {
        properties[name] = Property(value)
    }
}
