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
 * A mutable entity implementation that should be used for testing purposes.
 */
class MutableEntityMock(id: Int) : MutableEntity(id) {
    val properties: MutableMap<String, Any> = hashMapOf()

    constructor(id: Int, properties: Map<String, Any>) : this(id) {
        this.properties.putAll(properties)
    }

    override operator fun get(property: String): Any? = properties[property]

    override fun remove(property: String) {
        properties.remove(property)
    }

    override operator fun <T : Any> set(property: String, value: T) {
        properties[property] = value
    }

    /**
     * Checks if the entity properties are equal to the given properties.
     *
     * @param expectedProperties the expected properties of this entity
     * @throws IllegalStateException if this entity has different properties
     */
    fun verify(expectedProperties: Map<String, Any>) {
        val actualProperties = properties
        check(actualProperties == expectedProperties) {
            "Entity $id doesn't have the indicated properties!\n" +
                    "Expected: $expectedProperties\n" +
                    "Actual: $actualProperties"
        }
    }
}
