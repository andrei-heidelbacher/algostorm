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

package algostorm.ecs

/**
 * A mutable view of an [Entity] that allows setting or removing properties.
 */
abstract class MutableEntity(id: Int) : Entity(id) {
    /**
     * Sets the [value] of the specified property.
     *
     * If this entity already contains a property with the given name, the value
     * is overwritten.
     *
     * @param T the type of the property that is set
     * @param property the name of the property
     * @param value the value of the property
     */
    abstract operator fun <T : Any> set(property: String, value: T): Unit

    /**
     * Removes the specified property.
     *
     * @param property the name of the property that should be removed
     */
    abstract fun remove(property: String): Unit
}
