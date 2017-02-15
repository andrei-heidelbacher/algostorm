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

package com.aheidelbacher.algostorm.core.engine.graphics2d

import com.aheidelbacher.algostorm.core.engine.driver.Resource
import com.aheidelbacher.algostorm.core.engine.graphics2d.TileSet.Companion.clearFlags
import com.aheidelbacher.algostorm.core.engine.graphics2d.TileSet.Frame
import com.aheidelbacher.algostorm.core.engine.graphics2d.TileSet.Viewport
import com.aheidelbacher.algostorm.core.engine.serialization.Deserializer.Companion.readValue
import com.aheidelbacher.algostorm.core.engine.serialization.JsonDriver

import java.io.IOException

data class TileSetCollection @Throws(IOException::class) constructor(
        val tileSets: List<Resource>
) {
    companion object {
        @Throws(IOException::class)
        fun load(resource: Resource): TileSetCollection =
                resource.inputStream().use { JsonDriver.readValue(it) }
    }

    @Transient private val viewports = arrayListOf<Viewport>()
    @Transient private val animations = hashMapOf<String, List<Frame>>()

    init {
        val tileSetNames = hashSetOf<String>()
        val viewports = arrayListOf<Viewport>()
        var firstGid = 1
        for (resource in tileSets) {
            val tileSet = TileSet.load(resource)
            require(tileSet.name !in tileSetNames) {
                "Multiple tile sets with the same name ${tileSet.name}!"
            }
            tileSetNames.add(tileSet.name)
            for (tileId in 0 until tileSet.tileCount) {
                viewports.add(tileSet.getViewport(tileId))
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
