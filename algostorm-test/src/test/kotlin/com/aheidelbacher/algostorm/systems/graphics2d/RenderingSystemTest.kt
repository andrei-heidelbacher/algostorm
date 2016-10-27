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

package com.aheidelbacher.algostorm.systems.graphics2d

import org.junit.After
import org.junit.Before
import org.junit.Test

import com.aheidelbacher.algostorm.engine.graphics2d.Matrix
import com.aheidelbacher.algostorm.state.Color
import com.aheidelbacher.algostorm.state.File
import com.aheidelbacher.algostorm.state.Image
import com.aheidelbacher.algostorm.state.Layer
import com.aheidelbacher.algostorm.state.Layer.ObjectGroup
import com.aheidelbacher.algostorm.state.Layer.TileLayer
import com.aheidelbacher.algostorm.state.MapObject
import com.aheidelbacher.algostorm.state.Object
import com.aheidelbacher.algostorm.state.TileSet
import com.aheidelbacher.algostorm.state.TileSet.Tile.Companion.flipDiagonally
import com.aheidelbacher.algostorm.state.TileSet.Tile.Companion.flipHorizontally
import com.aheidelbacher.algostorm.state.TileSet.Tile.Companion.flipVertically
import com.aheidelbacher.algostorm.systems.physics2d.Rectangle
import com.aheidelbacher.algostorm.systems.graphics2d.RenderingSystem.Render
import com.aheidelbacher.algostorm.test.engine.graphics2d.GraphicsDriverMock

class RenderingSystemTest {
    val graphicsDriver = GraphicsDriverMock(320, 230)
    val width = 12
    val height = 12
    val tileWidth = 24
    val tileHeight = 24
    val image = Image(File("testImage.png"), tileWidth, tileHeight)
    val cameraX = 44
    val cameraY = 60
    val camera = Rectangle(
            x = cameraX - graphicsDriver.width / 2,
            y = cameraY - graphicsDriver.height / 2,
            width = graphicsDriver.width,
            height = graphicsDriver.height
    )

    fun makeMap(
            tileSets: List<TileSet> = listOf(TileSet(
                    name = "test",
                    tileWidth = tileWidth,
                    tileHeight = tileHeight,
                    columns = 1,
                    tileCount = 1,
                    image = image
            )),
            layers: List<Layer> = emptyList()
    ): MapObject = MapObject(
            width = width,
            height = height,
            tileWidth = tileWidth,
            tileHeight = tileHeight,
            backgroundColor = Color("#ffffffff"),
            tileSets = tileSets,
            layers = layers,
            nextObjectId = layers.filterIsInstance<ObjectGroup>()
                    .flatMap { it.objectSet }.maxBy { it.id }?.id?.plus(1) ?: 1
    )

    @Before
    fun lockCanvas() {
        graphicsDriver.lockCanvas()
    }

    @After
    fun unlockCanvas() {
        graphicsDriver.unlockAndPostCanvas()
    }

    @Test
    fun testRenderTileLayer() {
        val tileLayer = TileLayer(
                name = "floor",
                data = LongArray(width * height) { 1 }
        )
        val map = makeMap(layers = listOf(tileLayer))
        val renderingSystem = RenderingSystem(map, graphicsDriver)
        renderingSystem.onRender(Render(cameraX, cameraY))
        graphicsDriver.assertColor(-1)
        for (ty in 0 until height) {
            for (tx in 0 until width) {
                val y = ty * tileHeight
                val x = tx * tileWidth
                graphicsDriver.assertBitmap(
                        image = image.source.path,
                        x = 0,
                        y = 0,
                        width = tileWidth,
                        height = tileHeight,
                        matrix = Matrix.identity().postTranslate(
                                dx = x - camera.x.toFloat(),
                                dy = y - tileHeight + 1 - camera.y.toFloat()
                        )
                )
            }
        }
        graphicsDriver.assertEmptyDrawQueue()
    }

    @Test
    fun testRenderColoredObjects() {
        val objectGroup = ObjectGroup(
                name = "objects",
                color = Color("#000000ff"),
                objects = mutableListOf(Object(
                        id = 1,
                        x = 0,
                        y = tileHeight - 1,
                        width = tileWidth,
                        height = tileHeight
                ))
        )
        val map = makeMap(layers = listOf(objectGroup))
        val renderingSystem = RenderingSystem(map, graphicsDriver)
        renderingSystem.onRender(Render(cameraX, cameraY))
        graphicsDriver.assertColor(-1)
        graphicsDriver.assertRectangle(
                color = 255,
                width = tileWidth,
                height = tileHeight,
                matrix = Matrix.identity().postTranslate(
                        dx = -camera.x.toFloat(),
                        dy = -camera.y.toFloat()
                )
        )
        graphicsDriver.assertEmptyDrawQueue()
    }

    @Test
    fun testRenderFlippedHorizontallyObject() {
        val objectGroup = ObjectGroup(
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
        val renderingSystem = RenderingSystem(map, graphicsDriver)
        renderingSystem.onRender(Render(cameraX, cameraY))
        graphicsDriver.assertColor(-1)
        graphicsDriver.assertBitmap(
                image = image.source.path,
                x = 0,
                y = 0,
                width = tileWidth,
                height = tileHeight,
                matrix = Matrix.identity().postScale(-1F, 1F).postTranslate(
                        dx = tileWidth - camera.x.toFloat(),
                        dy = -camera.y.toFloat() - tileHeight + 1
                )
        )
        graphicsDriver.assertEmptyDrawQueue()
    }

    @Test
    fun testRenderFlippedVerticallyObject() {
        val objectGroup = ObjectGroup(
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
        val renderingSystem = RenderingSystem(map, graphicsDriver)
        renderingSystem.onRender(Render(cameraX, cameraY))
        graphicsDriver.assertColor(-1)
        graphicsDriver.assertBitmap(
                image = image.source.path,
                x = 0,
                y = 0,
                width = tileWidth,
                height = tileHeight,
                matrix = Matrix.identity().postScale(1F, -1F).postTranslate(
                        dx = -camera.x.toFloat(),
                        dy = -camera.y.toFloat() + 1
                )
        )
        graphicsDriver.assertEmptyDrawQueue()
    }

    @Test
    fun testRenderFlippedDiagonallyObject() {
        val objectGroup = ObjectGroup(
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
        val renderingSystem = RenderingSystem(map, graphicsDriver)
        renderingSystem.onRender(Render(cameraX, cameraY))
        graphicsDriver.assertColor(-1)
        graphicsDriver.assertBitmap(
                image = image.source.path,
                x = 0,
                y = 0,
                width = tileWidth,
                height = tileHeight,
                matrix = Matrix.identity()
                        .postRotate(90F)
                        .postScale(1F, -1F)
                        .postTranslate(
                                dx = tileWidth - camera.x.toFloat(),
                                dy = -camera.y.toFloat() + 1
                        )
        )
        graphicsDriver.assertEmptyDrawQueue()
    }
}
