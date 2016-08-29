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

import com.aheidelbacher.algostorm.engine.geometry2d.Rectangle
import com.aheidelbacher.algostorm.engine.graphics2d.RenderingSystem.Companion.isVisible
import com.aheidelbacher.algostorm.engine.state.Layer
import com.aheidelbacher.algostorm.engine.state.Map
import com.aheidelbacher.algostorm.engine.state.Object
import com.aheidelbacher.algostorm.engine.state.TileSet
import com.aheidelbacher.algostorm.engine.state.TileSet.Tile.Companion.flipHorizontally
import com.aheidelbacher.algostorm.test.engine.graphics2d.CanvasMock

class RenderingSystemTest {
    val canvas = CanvasMock()
    val width = 12
    val height = 12
    val tileWidth = 24
    val tileHeight = 24
    val image = "testImage.png"
    val map = Map(
            width = width,
            height = height,
            tileWidth = tileWidth,
            tileHeight = tileHeight,
            orientation = Map.Orientation.ORTHOGONAL,
            backgroundColor = "#00000000",
            tileSets = listOf(TileSet(
                    name = "test",
                    tileWidth = tileWidth,
                    tileHeight = tileHeight,
                    columns = 1,
                    tileCount = 1,
                    imageWidth = tileWidth,
                    imageHeight = tileHeight,
                    image = image,
                    margin = 0,
                    spacing = 0
            )),
            layers = listOf(
                    Layer.TileLayer(
                            name = "floor",
                            data = LongArray(width * height) { 1 }
                    ),
                    Layer.ObjectGroup(
                            name = "objects",
                            objects = mutableListOf(Object(
                                    id = 1,
                                    x = 32,
                                    y = 48,
                                    width = 32,
                                    height = 32,
                                    gid = 1L.flipHorizontally()
                            ))
                    )
            ),
            nextObjectId = 2
    )

    @Test
    fun testOnRender() {
        val cameraX = 44
        val cameraY = 60
        canvas.lock()
        val camera = Rectangle(
                x = cameraX - canvas.width / 2,
                y = cameraY - canvas.height / 2,
                width = canvas.width,
                height = canvas.height
        )
        canvas.unlockAndPost()
        val renderingSystem = RenderingSystem(map, canvas)
        renderingSystem.onRender(RenderingSystem.Render(cameraX, cameraY))
        canvas.verifyClear()
        canvas.verifyColor(0)
        for (ty in 0 until height) {
            for (tx in 0 until width) {
                val y = ty * tileHeight
                val x = tx * tileWidth
                if (isVisible(camera, 1L, x, y, tileWidth, tileHeight)) {
                    canvas.verifyBitmap(
                            image = image,
                            x = 0,
                            y = 0,
                            width = tileWidth,
                            height = tileHeight,
                            matrix = Matrix.identity().postTranslate(
                                    dx = x - cameraX.toFloat(),
                                    dy = y - cameraY.toFloat()
                            ),
                            opacity = 1F
                    )
                }
            }
        }
    }
}
