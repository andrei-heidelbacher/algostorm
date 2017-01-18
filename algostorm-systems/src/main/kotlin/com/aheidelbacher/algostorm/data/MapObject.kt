/*
 * Copyright 2017 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.aheidelbacher.algostorm.data

import com.fasterxml.jackson.annotation.JsonProperty

import com.aheidelbacher.algostorm.core.ecs.EntityPool
import com.aheidelbacher.algostorm.core.ecs.EntityPool.Companion.entityPoolOf
import com.aheidelbacher.algostorm.core.ecs.EntityRef.Id
import com.aheidelbacher.algostorm.core.ecs.Prefab
import com.aheidelbacher.algostorm.core.ecs.Prefab.Companion.toPrefab
import com.aheidelbacher.algostorm.core.engine.graphics2d.Color

import kotlin.properties.Delegates

class MapObject private constructor(
        val width: Int,
        val height: Int,
        val tileWidth: Int,
        val tileHeight: Int,
        private val tileSets: List<TileSet>,
        entities: Map<Id, Prefab>,
        val backgroundColor: Color?,
        val version: String
) {
    class Builder {
        companion object {
            fun mapObject(init: Builder.() -> Unit): MapObject =
                    Builder().apply(init).build()
        }

        var width: Int by Delegates.notNull<Int>()
        var height: Int by Delegates.notNull<Int>()
        var tileWidth: Int by Delegates.notNull<Int>()
        var tileHeight: Int by Delegates.notNull<Int>()
        private val tileSets: MutableList<TileSet> = arrayListOf()
        private val initialEntities = hashMapOf<Id, Prefab>()
        private val entities = arrayListOf<Prefab>()
        var backgroundColor: Color? = null
        var version: String = "1.0"

        operator fun TileSet.unaryPlus() {
            tileSets.add(this)
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
    @Transient val tileSetCollection: TileSetCollection =
            TileSetCollection(tileSets)

    private val entities: Map<Id, Prefab>
        @JsonProperty("entities") get() = entityPool.group.entities.associate {
            it.id to it.toPrefab()
        }
}
