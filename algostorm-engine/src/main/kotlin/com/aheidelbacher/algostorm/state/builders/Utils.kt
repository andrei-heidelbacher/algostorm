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

package com.aheidelbacher.algostorm.state.builders

import com.aheidelbacher.algostorm.state.Entity
import com.aheidelbacher.algostorm.state.Entity.Factory
import com.aheidelbacher.algostorm.state.Layer.EntityGroup
import com.aheidelbacher.algostorm.state.Layer.TileLayer
import com.aheidelbacher.algostorm.state.MapObject
import com.aheidelbacher.algostorm.state.TileSet
import com.aheidelbacher.algostorm.state.TileSet.Tile

inline fun mapObject(init: MapObjectBuilder.() -> Unit): MapObject =
        MapObjectBuilder().apply(init).build()

inline fun tileSet(init: TileSetBuilder.() -> Unit): TileSet =
        TileSetBuilder().apply(init).build()

inline fun tile(init: TileBuilder.() -> Unit): Tile =
        TileBuilder().apply(init).build()

inline fun tileLayer(init: TileLayerBuilder.() -> Unit): TileLayer =
        TileLayerBuilder().apply(init).build()

inline fun entityGroup(init: EntityGroupBuilder.() -> Unit): EntityGroup =
        EntityGroupBuilder().apply(init).build()

inline fun Factory.entity(init: EntityBuilder.() -> Unit): Entity =
        create(EntityBuilder().apply(init).components)

inline fun entity(id: Int, init: EntityBuilder.() -> Unit): Entity =
        EntityBuilder().apply(init).build(id)
