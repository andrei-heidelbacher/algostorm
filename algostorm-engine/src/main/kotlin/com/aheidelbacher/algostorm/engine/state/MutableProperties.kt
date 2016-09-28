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

/**
 * A bag of mutable properties.
 */
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

    operator fun set(name: String, value: Int) {
        properties[name] = propertyOf(value)
    }

    operator fun set(name: String, value: Float) {
        properties[name] = propertyOf(value)
    }

    operator fun set(name: String, value: Boolean) {
        properties[name] = propertyOf(value)
    }

    operator fun set(name: String, value: String) {
        properties[name] = propertyOf(value)
    }

    operator fun set(name: String, value: File) {
        properties[name] = propertyOf(value)
    }

    operator fun set(name: String, value: Color) {
        properties[name] = propertyOf(value)
    }
}
