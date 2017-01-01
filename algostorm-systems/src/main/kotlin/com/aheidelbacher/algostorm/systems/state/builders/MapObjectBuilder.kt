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

package com.aheidelbacher.algostorm.systems.state.builders

import com.aheidelbacher.algostorm.systems.state.Color
import com.aheidelbacher.algostorm.systems.state.Entity.Factory
import com.aheidelbacher.algostorm.systems.state.Layer
import com.aheidelbacher.algostorm.systems.state.MapObject
import com.aheidelbacher.algostorm.systems.state.MapObject.Orientation
import com.aheidelbacher.algostorm.systems.state.MapObject.RenderOrder
import com.aheidelbacher.algostorm.systems.state.TileSet

import kotlin.properties.Delegates

class MapObjectBuilder : Factory by Factory() {
    var width: Int by Delegates.notNull()
    var height: Int by Delegates.notNull()
    var tileWidth: Int by Delegates.notNull()
    var tileHeight: Int by Delegates.notNull()
    var orientation: Orientation = Orientation.ORTHOGONAL
    var renderOrder: RenderOrder = RenderOrder.RIGHT_DOWN
    val tileSets: MutableList<TileSet> = arrayListOf()
    val layers: MutableList<Layer> = arrayListOf()
    var backgroundColor: Color? = null
    var version: String = "1.0"

    operator fun TileSet.unaryPlus() {
        tileSets.add(this)
    }

    operator fun Layer.unaryPlus() {
        layers.add(this)
    }

    fun build(): MapObject = MapObject(
            width = width,
            height = height,
            tileWidth = tileWidth,
            tileHeight = tileHeight,
            orientation = orientation,
            renderOrder = renderOrder,
            tileSets = tileSets.toList(),
            layers = layers.toList(),
            backgroundColor = backgroundColor,
            version = version
    )
}
