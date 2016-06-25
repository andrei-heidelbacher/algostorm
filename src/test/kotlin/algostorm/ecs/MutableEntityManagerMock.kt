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
 * A concrete implementation of a mutable entity manager that should be used for
 * testing purposes.
 */
class MutableEntityManagerMock() : MutableEntityManager {
    private val entityMap = hashMapOf<Int, MutableEntityMock>()
    private var nextId = 0

    constructor(entities: Map<Int, Map<String, Any>>) : this() {
        entityMap.putAll(entities.mapValues {
            MutableEntityMock(it.key, it.value)
        })
    }

    override val entities: Sequence<MutableEntityMock>
        get() = entityMap.values.asSequence()

    override fun create(properties: Map<String, Any>): MutableEntityMock {
        check(nextId >= 0)
        val id = nextId
        val entity = MutableEntityMock(id, properties)
        nextId++
        entityMap[id] = entity
        return entity
    }

    override fun delete(entityId: Int): Boolean =
            entityMap.remove(entityId) != null

    override fun get(entityId: Int): MutableEntityMock? = entityMap[entityId]

    /**
     * Checks that the manager entities are equal to the given [entities].
     *
     * @param expectedEntities the expected state of the manager
     * @throws IllegalStateException if the given entities do not correspond to
     * the manager entities
     */
    fun verify(expectedEntities: Map<Int, Map<String, Any>>) {
        val actualEntities = entityMap.mapValues { it.value.properties }
        check(actualEntities == expectedEntities) {
            "Entity manager doesn't have the indicated entities!\n" +
                    "Expected: $expectedEntities\n" +
                    "Actual: $actualEntities"
        }
    }
}
