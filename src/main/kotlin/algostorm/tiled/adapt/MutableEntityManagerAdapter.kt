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

package algostorm.tiled.adapt

import algostorm.ecs.MutableEntity
import algostorm.ecs.MutableEntityManager
import algostorm.tiled.json.Layer
import algostorm.tiled.json.TiledMap
import algostorm.tiled.json.Object

/**
 * A concrete implementation of a mutable entity manager which wraps a Tiled
 * map. Changes made to the entity database and entity records will be reflected
 * in the associated Tiled map.
 *
 * @param tiledMap the underlying Tiled map
 * @throws IllegalArgumentException if the given [tiledMap] doesn't contain a
 * [Layer.ObjectGroup] with the name [ENTITY_LAYER_NAME]
 */
class MutableEntityManagerAdapter(
        private val tiledMap: TiledMap
) : MutableEntityManager {
    companion object {
        /**
         * The name of the [Layer.ObjectGroup] which contains the game entities.
         */
        const val ENTITY_LAYER_NAME = "entities"
    }

    /**
     * A wrapper over a Tiled object.
     *
     * @param tiledObject the underlying Tiled object
     */
    private class MutableEntityAdapter(
            val tiledObject: Object
    ) : MutableEntity(tiledObject.id) {
        override operator fun get(name: String): Any? =
                tiledObject.properties[name]

        override fun <T : Any> set(name: String, value: T) {
            tiledObject.properties[name] = value
        }

        override fun remove(name: String) {
            tiledObject.properties.remove(name)
        }
    }

    private val entityMap: MutableMap<Int, MutableEntityAdapter> = hashMapOf()
    private val objectGroup = requireNotNull(tiledMap.layers.find {
        it.name == ENTITY_LAYER_NAME
    } as? Layer.ObjectGroup) { "Tiled map doesn't contain entity layer!" }

    override val entities: Sequence<MutableEntity>
        get() = entityMap.values.asSequence()

    override fun get(entityId: Int): MutableEntity? = entityMap[entityId]

    override fun create(properties: Map<String, Any>): MutableEntity {
        check(tiledMap.nextObjectId >= 0) { "Too many entities!" }
        val id = tiledMap.nextObjectId
        val tiledObject = Object(id)
        val entity = MutableEntityAdapter(tiledObject)
        tiledMap.nextObjectId++
        tiledObject.properties.putAll(properties)
        objectGroup.objects.add(tiledObject)
        entityMap[id] = entity
        return entity
    }

    override fun delete(entityId: Int): Boolean {
        return entityMap[entityId]?.let { entity ->
            entityMap.remove(entityId)
            objectGroup.objects.remove(entity.tiledObject)
            true
        } ?: false
    }
}
