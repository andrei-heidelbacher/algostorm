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

import com.aheidelbacher.algostorm.systems.state.TileSet.Tile
import com.aheidelbacher.algostorm.systems.state.TileSet.Tile.Frame

import kotlin.properties.Delegates

class TileBuilder {
    var id: Int by Delegates.notNull()
    val animation: MutableList<Frame> = arrayListOf()

    operator fun Frame.unaryPlus() {
        animation.add(this)
    }

    fun build(): Tile = Tile(
            id = id,
            animation = if (animation.isEmpty()) null else animation
    )
}