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

package com.aheidelbacher.algostorm.systems.graphics2d

import com.aheidelbacher.algostorm.systems.graphics2d.TileSet.Companion.clearFlags
import com.aheidelbacher.algostorm.systems.graphics2d.TileSet.Frame
import com.aheidelbacher.algostorm.systems.graphics2d.TileSet.Viewport

class TileSetCollection(tileSets: List<TileSet>) {
    @Transient private val viewports = arrayListOf<Viewport>()
    @Transient private val animations = hashMapOf<String, List<Frame>>()

    init {
        val tileSetNames = hashSetOf<String>()
        var firstGid = 1
        for (tileSet in tileSets) {
            require(tileSet.name !in tileSetNames) {
                "Multiple tile sets with the same name ${tileSet.name}!"
            }
            tileSetNames += tileSet.name
            for (tileId in 0 until tileSet.tileCount) {
                viewports += tileSet.getViewport(tileId)
            }
            for ((animation, frames) in tileSet.animations) {
                require(animation !in animations) {
                    "Multiple animations with the same name $animation!"
                }
                animations[animation] = frames.map {
                    it.copy(tileId = it.tileId + firstGid)
                }
            }
            firstGid += tileSet.tileCount
        }
    }

    fun getAnimation(animation: String): List<Frame>? = animations[animation]

    /**
     * @throws IndexOutOfBoundsException
     */
    fun getViewport(gid: Int): Viewport = viewports[gid.clearFlags() - 1]
}
