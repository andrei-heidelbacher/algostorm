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

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

import com.aheidelbacher.algostorm.state.Layer.EntityGroup
import com.aheidelbacher.algostorm.state.Layer.TileLayer

/**
 * An abstract layer in the game world.
 *
 * @property name the name of this layer
 * @property isVisible whether this layer should be rendered or not
 * @property offsetX the horizontal rendering offset in pixels
 * @property offsetY the vertical rendering offset in pixels (positive is down)
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        JsonSubTypes.Type(value = EntityGroup::class, name = "EntityGroup"),
        JsonSubTypes.Type(value = TileLayer::class, name = "TileLayer")
)
sealed class Layer(
        val name: String,
        var isVisible: Boolean,
        val offsetX: Int,
        val offsetY: Int
) {
    /**
     * Two layers are equal if and only if they have the same name.
     *
     * @param other the layer with which equality is checked
     * @return `true` if the two layers have the same name, `false` otherwise
     */
    final override fun equals(other: Any?): Boolean =
            other is Layer && name == other.name

    final override fun hashCode(): Int = name.hashCode()

    final override fun toString(): String = "${javaClass.simpleName}($name)"

    /**
     * A layer which consists of `width * height` tiles, where `width` and
     * `height` are the dimensions of the containing [MapObject].
     *
     * @property data the global ids of the tiles in this layer
     */
    class TileLayer internal constructor(
            name: String,
            isVisible: Boolean,
            offsetX: Int,
            offsetY: Int,
            private val data: IntArray
    ) : Layer(name, isVisible, offsetX, offsetY) {
        /** The number of tiles in this layer. */
        val size: Int
            get() = data.size

        /**
         * Returns the gid of the tile at the given index.
         *
         * @param index the index of the tile
         * @return the gid of the requested tile
         * @throws IndexOutOfBoundsException if [index] is negative or greater
         * than or equal to [size]
         */
        operator fun get(index: Int): Int = data[index]

        /**
         * Sets the gid of the tile at the given index to the given value.
         *
         * @param index the index of the tile
         * @param value the new gid of the tile
         * @throws IndexOutOfBoundsException if [index] is negative or greater
         * than or equal to [size]
         */
        operator fun set(index: Int, value: Int) {
            data[index] = value
        }
    }

    /**
     * A layer which contains a set of entities.
     *
     * @param entities the set of entities contained by this layer
     */
    class EntityGroup internal constructor(
            name: String,
            isVisible: Boolean,
            offsetX: Int,
            offsetY: Int,
            entities: Set<Entity>
    ) : Layer(name, isVisible, offsetX, offsetY) {
        @Transient private val entityMap =
                entities.associateByTo(hashMapOf(), Entity::id)

        /** A read-only view of the entities in this group. */
        val entities: Iterable<Entity> = entityMap.values

        /**
         * Checks whether this entity group contains an entity with the given
         * id.
         *
         * @param id the id of the requested entity
         * @return `true` if the entity with the given id exists in this entity
         * group, `false` otherwise
         */
        operator fun contains(id: Int): Boolean = id in entityMap

        /**
         * Returns the entity with the given id.
         *
         * @param id the id of the requested entity
         * @return the requested entity, or `null` if it doesn't exist
         */
        operator fun get(id: Int): Entity? = entityMap[id]

        /**
         * Adds the given entity to this entity group.
         *
         * If adding the entity to this group fails, the entity group remains
         * unchanged.
         *
         * @throws IllegalArgumentException if the id of the given entity is not
         * unique among the entities in this entity group
         */
        fun add(entity: Entity) {
            require(entity.id !in entityMap) {
                "$entity id is not unique within $this!"
            }
            entityMap[entity.id] = entity
        }

        /**
         * Adds the given entities to this entity group.
         *
         * If adding the entities to this group fails, the entity group remains
         * unchanged.
         *
         * @throws IllegalArgumentException if the id of any given entity is not
         * unique among the entities in this entity group
         */
        fun addAll(entities: Set<Entity>) {
            require(entities.none { it.id in entityMap }) {
                "$entities ids are not unique within $this!"
            }
            entities.associateByTo(entityMap, Entity::id)
        }

        fun addAll(vararg entities: Entity) {
            addAll(entities.toSet())
        }

        /**
         * Removes the entity with the given id from this entity group and
         * returns it.
         *
         * @param id the id of the entity that should be removed
         * @return the removed entity if it exists in this entity group when
         * this method is called, `null` otherwise
         */
        fun remove(id: Int): Entity? = entityMap.remove(id)

        /** Removes all entities from this entity group. */
        fun clear() {
            entityMap.clear()
        }
    }
}
