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

package algostorm.state.adapters

import algostorm.ecs.MutableEntity
import algostorm.ecs.MutableEntityManager
import algostorm.state.Layer
import algostorm.state.Object

/**
 * A concrete implementation of a mutable entity manager which wraps a State
 * stateMap. Changes made to the entity database and entity records will be reflected
 * in the associated State stateMap.
 *
 * @param stateMap the underlying State stateMap
 * @throws IllegalArgumentException if the given [stateMap] doesn't contain a
 * [State.Layer.ObjectGroup] with the name [ENTITY_LAYER_NAME]
 */
class MutableEntityManagerAdapter(
        private val stateMap: algostorm.state.Map
) : MutableEntityManager {
    companion object {
        /**
         * The name of the [State.Layer.ObjectGroup] which contains the game
         * entities.
         */
        const val ENTITY_LAYER_NAME = "entities"
    }

    /**
     * A wrapper over a State object.
     *
     * @param stateObject the underlying state object
     */
    class MutableEntityAdapter(
            val stateObject: Object
    ) : MutableEntity(stateObject.id) {
        override operator fun get(property: String): Any? =
                stateObject.properties[property]

        override fun <T : Any> set(property: String, value: T) {
            stateObject.properties[property] = value
        }

        override fun remove(property: String) {
            stateObject.properties.remove(property)
        }
    }

    private val objectGroup = requireNotNull(stateMap.layers.find {
        it.name == ENTITY_LAYER_NAME
    } as? Layer.ObjectGroup) { "State map doesn't contain entity layer!" }
    private val entityMap = objectGroup.objects.associateTo(hashMapOf()) {
        it.id to MutableEntityAdapter(it)
    }

    override val entities: Sequence<MutableEntity>
        get() = entityMap.values.asSequence()

    override fun get(entityId: Int): MutableEntity? = entityMap[entityId]

    override fun create(properties: Map<String, Any>): MutableEntity {
        val id = stateMap.getNextObjectId()
        val tiledObject = Object(id)
        val entity = MutableEntityAdapter(tiledObject)
        tiledObject.properties.putAll(properties)
        objectGroup.objects.add(tiledObject)
        entityMap[id] = entity
        return entity
    }

    override fun delete(entityId: Int): Boolean {
        return entityMap[entityId]?.let { entity ->
            entityMap.remove(entityId)
            objectGroup.objects.remove(entity.stateObject)
            true
        } ?: false
    }
}
