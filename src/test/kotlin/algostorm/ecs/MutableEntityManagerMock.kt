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

class MutableEntityManagerMock() : MutableEntityManager {
    private val entityMap = hashMapOf<Int, MutableEntityMock>()
    private var nextId = 0

    constructor(entities: Map<Int, Iterable<Component>>) : this() {
        entities.forEach { create(it.key, it.value) }
    }

    override val entities: Sequence<MutableEntityMock>
        get() = entityMap.values.asSequence()

    override fun create(components: Iterable<Component>): MutableEntityMock {
        check(entityMap.size < Int.MAX_VALUE)
        while (nextId in entityMap) {
            ++nextId
        }
        val id = nextId
        ++nextId
        return create(id, components)
    }

    override fun create(
            entityId: Int,
            components: Iterable<Component>
    ): MutableEntityMock {
        require(entityId !in entityMap)
        val entity = MutableEntityMock(entityId)
        components.forEach { entity.set(it) }
        return entity
    }

    override fun delete(entityId: Int): Boolean =
            entityMap.remove(entityId) != null

    override fun get(entityId: Int): MutableEntityMock? = entityMap[entityId]

    fun verify(entities: Map<Int, Iterable<Component>>) {
        val actualEntities = entityMap.mapValues { it.value.components.toSet() }
        val expectedEntities = entities.mapValues { it.value.toSet() }
        check(actualEntities == expectedEntities) {
            "Entity manager doesn't have the indicated entities!\n" +
                    "Expected: $expectedEntities\n" +
                    "Actual: $actualEntities"
        }
    }
}
