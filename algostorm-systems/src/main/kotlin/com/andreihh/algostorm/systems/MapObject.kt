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
import com.andreihh.algostorm.core.ecs.Component
import com.andreihh.algostorm.core.ecs.EntityPool
import com.andreihh.algostorm.core.ecs.EntityRef
import com.andreihh.algostorm.core.ecs.EntityRef.Id
import com.andreihh.algostorm.systems.graphics2d.TileSet
import kotlin.properties.Delegates

class MapObject private constructor(
        val width: Int,
        val height: Int,
        val tileWidth: Int,
        val tileHeight: Int,
        val tileSets: List<TileSet>,
        val entityPool: EntityPool,
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
        private val tileSets = arrayListOf<TileSet>()
        private val initialEntities = hashMapOf<Id, Collection<Component>>()
        private val entities = arrayListOf<Collection<Component>>()
        var backgroundColor: Color? = null
        var version: String = "1.0"

        fun tileSet(init: TileSet.Builder.() -> Unit) {
            tileSets += TileSet.Builder().apply(init).build()
        }

        fun tileSet(tileSet: TileSet) {
            tileSets += tileSet
        }

        fun entity(id: Id, init: EntityRef.Builder.() -> Unit) {
            initialEntities[id] = EntityRef.Builder().apply(init).build()
        }

        fun entity(id: Id, components: Collection<Component>) {
            initialEntities[id] = components
        }

        fun entity(init: EntityRef.Builder.() -> Unit) {
            entities += EntityRef.Builder().apply(init).build()
        }

        fun entity(components: Collection<Component>) {
            entities += components
        }

        fun build(): MapObject = MapObject(
                width = width,
                height = height,
                tileWidth = tileWidth,
                tileHeight = tileHeight,
                tileSets = tileSets.toList(),
                entityPool = EntityPool.of(initialEntities),
                backgroundColor = backgroundColor,
                version = version
        ).apply {
            this@Builder.entities.forEach { entityPool.create(it) }
        }
    }
}
