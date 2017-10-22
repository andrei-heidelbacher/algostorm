/*
 * Copyright (c) 2017  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andreihh.algostorm.systems

import com.andreihh.algostorm.core.drivers.graphics2d.Color
import com.andreihh.algostorm.core.drivers.io.Resource
import com.andreihh.algostorm.core.ecs.EntityPool
import com.andreihh.algostorm.core.ecs.EntityPool.Companion.entityPoolOf
import com.andreihh.algostorm.core.ecs.EntityRef.Id
import com.andreihh.algostorm.core.ecs.Prefab
import com.andreihh.algostorm.core.ecs.Prefab.Companion.toPrefab
import com.andreihh.algostorm.systems.graphics2d.TileSet
import kotlin.properties.Delegates

class MapObject private constructor(
        val width: Int,
        val height: Int,
        val tileWidth: Int,
        val tileHeight: Int,
        val tileSets: List<Resource<TileSet>>,
        private var entities: Map<Id, Prefab>,
        val backgroundColor: Color?,
        val version: String
) {
    class Builder {
        companion object {
            fun mapObject(init: Builder.() -> Unit): MapObject =
                    Builder().apply(init).build()
        }

        var width: Int by Delegates.notNull()
        var height: Int by Delegates.notNull()
        var tileWidth: Int by Delegates.notNull()
        var tileHeight: Int by Delegates.notNull()
        private val tileSets = arrayListOf<Resource<TileSet>>()
        private val initialEntities = hashMapOf<Id, Prefab>()
        private val entities = arrayListOf<Prefab>()
        var backgroundColor: Color? = null
        var version: String = "1.0"

        fun tileSet(resource: Resource<TileSet>) {
            tileSets.add(resource)
        }

        fun entity(id: Id, prefab: Prefab) {
            initialEntities[id] = prefab
        }

        fun entity(prefab: Prefab) {
            entities.add(prefab)
        }

        fun build(): MapObject = MapObject(
                width = width,
                height = height,
                tileWidth = tileWidth,
                tileHeight = tileHeight,
                tileSets = tileSets.toList(),
                entities = initialEntities,
                backgroundColor = backgroundColor,
                version = version
        ).apply {
            this@Builder.entities.forEach { entityPool.create(it) }
        }
    }

    @Transient val entityPool: EntityPool = entityPoolOf(entities)

    fun updateSnapshot() {
        entities = entityPool.entities.associate {
            it.id to it.toPrefab()
        }
    }

}
