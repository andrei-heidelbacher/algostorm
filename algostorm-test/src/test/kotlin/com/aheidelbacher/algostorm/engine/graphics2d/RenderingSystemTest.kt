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
import com.aheidelbacher.algostorm.engine.tiled.Layer
import com.aheidelbacher.algostorm.engine.tiled.Map
import com.aheidelbacher.algostorm.engine.tiled.Object
import com.aheidelbacher.algostorm.engine.tiled.Color
import com.aheidelbacher.algostorm.engine.tiled.File
import com.aheidelbacher.algostorm.engine.tiled.TileSet
import com.aheidelbacher.algostorm.engine.tiled.TileSet.Tile.Companion.flipDiagonally
import com.aheidelbacher.algostorm.engine.tiled.TileSet.Tile.Companion.flipHorizontally
import com.aheidelbacher.algostorm.engine.tiled.TileSet.Tile.Companion.flipVertically
import com.aheidelbacher.algostorm.test.engine.graphics2d.CanvasMock

class RenderingSystemTest {
    val canvas = CanvasMock()
    val width = 12
    val height = 12
    val tileWidth = 24
    val tileHeight = 24
    val image = File("testImage.png")
    val cameraX = 44
    val cameraY = 60
    val camera = run {
        canvas.lock()
        val rect = Rectangle(
                x = cameraX - canvas.width / 2,
                y = cameraY - canvas.height / 2,
                width = canvas.width,
                height = canvas.height
        )
        canvas.unlockAndPost()
        rect
    }

    fun makeMap(
            tileSets: List<TileSet> = listOf(TileSet(
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
            layers: List<Layer> = emptyList()
    ): Map = Map(
            width = width,
            height = height,
            tileWidth = tileWidth,
            tileHeight = tileHeight,
            orientation = Map.Orientation.ORTHOGONAL,
            backgroundColor = Color("#ffffffff"),
            tileSets = tileSets,
            layers = layers,
            nextObjectId = layers.filterIsInstance<Layer.ObjectGroup>()
                    .flatMap { it.objects }.maxBy { it.id }?.id ?: 1
    )

    @Test
    fun testRenderTileLayer() {
        val tileLayer = Layer.TileLayer(
                name = "floor",
                data = LongArray(width * height) { 1 }
        )
        val map = makeMap(layers = listOf(tileLayer))
        val renderingSystem = RenderingSystem(map, canvas)
        renderingSystem.onRender(RenderingSystem.Render(cameraX, cameraY))
        canvas.verifyClear()
        canvas.verifyColor(-1)
        for (ty in 0 until height) {
            for (tx in 0 until width) {
                val y = ty * tileHeight
                val x = tx * tileWidth
                if (isVisible(camera, 1L, x, y, tileWidth, tileHeight)) {
                    canvas.verifyBitmap(
                            image = image.path,
                            x = 0,
                            y = 0,
                            width = tileWidth,
                            height = tileHeight,
                            matrix = Matrix.identity().postTranslate(
                                    dx = x - camera.x.toFloat(),
                                    dy = y - tileHeight + 1 - camera.y.toFloat()
                            ),
                            opacity = 1F
                    )
                }
            }
        }
        canvas.verifyEmptyDrawQueue()
    }

    @Test
    fun testRenderColoredObjects() {
        val objectGroup = Layer.ObjectGroup(
                name = "objects",
                color = Color("#000000ff"),
                objects = mutableListOf(Object(
                        id = 1,
                        x = 0,
                        y = 0,
                        width = tileWidth,
                        height = tileHeight
                ))
        )
        val map = makeMap(layers = listOf(objectGroup))
        val renderingSystem = RenderingSystem(map, canvas)
        renderingSystem.onRender(RenderingSystem.Render(cameraX, cameraY))
        canvas.verifyClear()
        canvas.verifyColor(-1)
        canvas.verifyRectangle(
                color = 255,
                width = tileWidth,
                height = tileHeight,
                matrix = Matrix.identity().postTranslate(
                        dx = -camera.x.toFloat(),
                        dy = -camera.y.toFloat()
                ),
                opacity = 1F
        )
        canvas.verifyEmptyDrawQueue()
    }

    @Test
    fun testRenderFlippedHorizontallyObject() {
        val objectGroup = Layer.ObjectGroup(
                name = "objects",
                objects = mutableListOf(Object(
                        id = 1,
                        x = 0,
                        y = 0,
                        width = tileWidth,
                        height = tileHeight,
                        gid = 1L.flipHorizontally()
                ))
        )
        val map = makeMap(layers = listOf(objectGroup))
        val renderingSystem = RenderingSystem(map, canvas)
        renderingSystem.onRender(RenderingSystem.Render(cameraX, cameraY))
        canvas.verifyClear()
        canvas.verifyColor(-1)
        canvas.verifyBitmap(
                image = image.path,
                x = 0,
                y = 0,
                width = tileWidth,
                height = tileHeight,
                matrix = Matrix.identity().postScale(-1F, 1F).postTranslate(
                        dx = tileWidth - camera.x.toFloat(),
                        dy = -camera.y.toFloat() - tileHeight + 1
                ),
                opacity = 1F
        )
        canvas.verifyEmptyDrawQueue()
    }

    @Test
    fun testRenderFlippedVerticallyObject() {
        val objectGroup = Layer.ObjectGroup(
                name = "objects",
                objects = mutableListOf(Object(
                        id = 1,
                        x = 0,
                        y = 0,
                        width = tileWidth,
                        height = tileHeight,
                        gid = 1L.flipVertically()
                ))
        )
        val map = makeMap(layers = listOf(objectGroup))
        val renderingSystem = RenderingSystem(map, canvas)
        renderingSystem.onRender(RenderingSystem.Render(cameraX, cameraY))
        canvas.verifyClear()
        canvas.verifyColor(-1)
        canvas.verifyBitmap(
                image = image.path,
                x = 0,
                y = 0,
                width = tileWidth,
                height = tileHeight,
                matrix = Matrix.identity().postScale(1F, -1F).postTranslate(
                        dx = -camera.x.toFloat(),
                        dy = -camera.y.toFloat() + 1
                ),
                opacity = 1F
        )
        canvas.verifyEmptyDrawQueue()
    }

    @Test
    fun testRenderFlippedDiagonallyObject() {
        val objectGroup = Layer.ObjectGroup(
                name = "objects",
                objects = mutableListOf(Object(
                        id = 1,
                        x = 0,
                        y = 0,
                        width = tileWidth,
                        height = tileHeight,
                        gid = 1L.flipDiagonally()
                ))
        )
        val map = makeMap(layers = listOf(objectGroup))
        val renderingSystem = RenderingSystem(map, canvas)
        renderingSystem.onRender(RenderingSystem.Render(cameraX, cameraY))
        canvas.verifyClear()
        canvas.verifyColor(-1)
        canvas.verifyBitmap(
                image = image.path,
                x = 0,
                y = 0,
                width = tileWidth,
                height = tileHeight,
                matrix = Matrix.identity()
                        .postRotate(90F)
                        .postScale(1F, -1F).postTranslate(
                        dx = tileWidth - camera.x.toFloat(),
                        dy = -camera.y.toFloat() + 1
                ),
                opacity = 1F
        )
        canvas.verifyEmptyDrawQueue()
    }
}
