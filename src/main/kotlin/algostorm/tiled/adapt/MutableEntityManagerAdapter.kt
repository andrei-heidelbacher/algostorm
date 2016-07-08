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
import algostorm.physics2d.Box
import algostorm.tiled.Tiled

/**
 * A concrete implementation of a mutable entity manager which wraps a Tiled
 * map. Changes made to the entity database and entity records will be reflected
 * in the associated Tiled map.
 *
 * @param tiledMap the underlying Tiled map
 * @throws IllegalArgumentException if the given [tiledMap] doesn't contain a
 * [Tiled.Layer.ObjectGroup] with the name [ENTITY_LAYER_NAME]
 */
class MutableEntityManagerAdapter(
        private val tiledMap: Tiled.Map
) : MutableEntityManager {
    companion object {
        /**
         * The name of the [Tiled.Layer.ObjectGroup] which contains the game
         * entities.
         */
        const val ENTITY_LAYER_NAME = "entities"
    }

    /**
     * A wrapper over a Tiled object.
     *
     * @param tiledObject the underlying Tiled object
     */
    class MutableEntityAdapter(
            val tiledObject: Tiled.Object
    ) : MutableEntity(tiledObject.id) {
        companion object {
            const val TYPE: String = "type"
            const val NAME: String = "name"
            const val VISIBLE: String = "visible"
            const val BOX: String = "box"
            const val ROTATION: String = "rotation"
            const val GID: String = "gid"
        }

        override operator fun get(property: String): Any? = when (property) {
            TYPE -> tiledObject.type
            NAME -> tiledObject.name
            VISIBLE -> tiledObject.isVisible
            BOX -> {
                if (tiledObject.width == 0 || tiledObject.height == 0) null
                else Box(
                        x = tiledObject.x,
                        y = tiledObject.y,
                        width = tiledObject.width,
                        height = tiledObject.height
                )
            }
            ROTATION -> tiledObject.rotation
            GID -> tiledObject.gid
            else -> tiledObject.properties[property]
        }

        override fun <T : Any> set(property: String, value: T) {
            when (property) {
                TYPE, NAME ->
                    throw IllegalArgumentException("$property is read-only!")
                VISIBLE -> tiledObject.isVisible =
                        requireNotNull(value as? Boolean) {
                            "$VISIBLE property must be of type Boolean!"
                        }
                BOX -> {
                    val (x, y, width, height) = requireNotNull(value as? Box) {
                        "$BOX property must be of type Box!"
                    }
                    tiledObject.x = x
                    tiledObject.y = y
                    tiledObject.width = width
                    tiledObject.height = height
                }
                ROTATION -> tiledObject.rotation =
                        requireNotNull(value as? Float) {
                            "$ROTATION property must be of type Float!"
                        }
                GID -> tiledObject.gid = requireNotNull(value as? Long) {
                    "$GID property must be of type Long!"
                }
                else -> tiledObject.properties[property] = value
            }
        }

        override fun remove(property: String) {
            when (property) {
                TYPE, NAME ->
                    throw IllegalArgumentException("$property is read-only!")
                VISIBLE -> tiledObject.isVisible = false
                BOX -> {
                    tiledObject.x = 0
                    tiledObject.y = 0
                    tiledObject.width = 0
                    tiledObject.height = 0
                }
                ROTATION -> tiledObject.rotation = 0F
                GID -> tiledObject.gid = 0
                else -> tiledObject.properties.remove(property)
            }
        }
    }

    private val objectGroup = requireNotNull(tiledMap.layers.find {
        it.name == ENTITY_LAYER_NAME
    } as? Tiled.Layer.ObjectGroup) { "Tiled map doesn't contain entity layer!" }
    private val entityMap = objectGroup.objects.associateTo(hashMapOf()) {
        it.id to MutableEntityAdapter(it)
    }

    override val entities: Sequence<MutableEntity>
        get() = entityMap.values.asSequence()

    override fun get(entityId: Int): MutableEntity? = entityMap[entityId]

    override fun create(properties: Map<String, Any>): MutableEntity {
        val id = tiledMap.getNextObjectId()
        val tiledObject = Tiled.Object(id)
        val entity = MutableEntityAdapter(tiledObject)
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
