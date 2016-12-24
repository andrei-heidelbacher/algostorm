/*
 * Copyright (c) 2016  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.aheidelbacher.algostorm.systems.state.builders

import com.aheidelbacher.algostorm.systems.state.File
import com.aheidelbacher.algostorm.systems.state.Image
import com.aheidelbacher.algostorm.systems.state.TileSet
import com.aheidelbacher.algostorm.systems.state.TileSet.Tile

import kotlin.properties.Delegates

class TileSetBuilder {
    lateinit var name: String
    var tileWidth: Int by Delegates.notNull()
    var tileHeight: Int by Delegates.notNull()
    lateinit var image: Image
    var margin: Int = 0
    var spacing: Int = 0
    var tileOffsetX: Int = 0
    var tileOffsetY: Int = 0
    val tiles: MutableSet<Tile> = hashSetOf()

    fun image(source: String, width: Int, height: Int) {
        image = Image(File(source), width, height)
    }

    fun image(source: File, width: Int, height: Int) {
        image = Image(source, width, height)
    }

    operator fun Tile.unaryPlus() {
        tiles.add(this)
    }

    fun build(): TileSet = TileSet(
            name = name,
            tileWidth = tileWidth,
            tileHeight = tileHeight,
            image = image,
            columns = image.width / tileWidth,
            tileCount = image.width / tileWidth * image.height / tileWidth,
            margin = margin,
            spacing = spacing,
            tileOffsetX = tileOffsetX,
            tileOffsetY = tileOffsetY,
            tiles = tiles.toSet()
    )
}
