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

package com.aheidelbacher.algostorm.engine.graphics2d

import org.junit.Test

import com.aheidelbacher.algostorm.engine.state.Layer
import com.aheidelbacher.algostorm.engine.state.Map
import com.aheidelbacher.algostorm.engine.state.Object
import com.aheidelbacher.algostorm.engine.state.TileSet
import com.aheidelbacher.algostorm.engine.state.TileSet.Tile.Companion.flipHorizontally
import com.aheidelbacher.algostorm.test.engine.graphics2d.CanvasMock

class RenderingSystemTest {
    val canvas = CanvasMock()
    val map = Map(
            width = 12,
            height = 12,
            tileWidth = 24,
            tileHeight = 24,
            orientation = Map.Orientation.ORTHOGONAL,
            tileSets = listOf(TileSet(
                    name = "test",
                    tileWidth = 24,
                    tileHeight = 24,
                    columns = 1,
                    tileCount = 1,
                    imageWidth = 24,
                    imageHeight = 24,
                    image = "test",
                    margin = 0,
                    spacing = 0
            )),
            layers = listOf(Layer.ObjectGroup(
                    name = "objects",
                    objects = mutableListOf(Object(
                            id = 1,
                            x = 32,
                            y = 48,
                            width = 32,
                            height = 32,
                            gid = 1L.flipHorizontally()
                    ))
            )),
            nextObjectId = 2
    )

    @Test
    fun testOnRender() {
        val renderingSystem = RenderingSystem(map, canvas)
        renderingSystem.onRender(RenderingSystem.Render(44, 60))
    }
}
