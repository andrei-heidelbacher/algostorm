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

package com.aheidelbacher.algostorm.systems.graphics2d

import org.junit.After
import org.junit.Before
import org.junit.Test

import com.aheidelbacher.algostorm.engine.driver.Resource
import com.aheidelbacher.algostorm.engine.graphics2d.Color
import com.aheidelbacher.algostorm.engine.graphics2d.Matrix
import com.aheidelbacher.algostorm.systems.state.File
import com.aheidelbacher.algostorm.systems.state.Image
import com.aheidelbacher.algostorm.systems.state.TileSet.Tile.Companion.flipDiagonally
import com.aheidelbacher.algostorm.systems.state.TileSet.Tile.Companion.flipHorizontally
import com.aheidelbacher.algostorm.systems.state.TileSet.Tile.Companion.flipVertically
import com.aheidelbacher.algostorm.systems.state.builders.MapObjectBuilder
import com.aheidelbacher.algostorm.systems.state.builders.entity
import com.aheidelbacher.algostorm.systems.state.builders.entityGroup
import com.aheidelbacher.algostorm.systems.state.builders.tileLayer
//import com.aheidelbacher.algostorm.systems.state.builders.tileSet
import com.aheidelbacher.algostorm.data.TileSet.Builder.Companion.tileSet
import com.aheidelbacher.algostorm.engine.driver.Resource.Companion.SCHEMA
import com.aheidelbacher.algostorm.systems.graphics2d.RenderingSystem.Render
import com.aheidelbacher.algostorm.systems.physics2d.Position
import com.aheidelbacher.algostorm.systems.physics2d.geometry2d.Rectangle
import com.aheidelbacher.algostorm.systems.state.MapObject
import com.aheidelbacher.algostorm.test.engine.graphics2d.GraphicsDriverMock

class RenderingSystemTest {
    val graphicsDriver = GraphicsDriverMock(320, 230)
    val width = 12
    val height = 12
    val tileWidth = 24
    val tileHeight = 24
    val image = Image(File("testImage.png"), tileWidth, tileHeight)
    val imageRes = Resource("res:///" + image.source.path)
    val cameraX = 44
    val cameraY = 60
    val camera = Rectangle(
            x = cameraX - graphicsDriver.width / 2,
            y = cameraY - graphicsDriver.height / 2,
            width = graphicsDriver.width,
            height = graphicsDriver.height
    )

    val mapBuilder = com.aheidelbacher.algostorm.data.MapObject.Builder().apply {
        width = 12
        height = 12
        tileWidth = 24
        tileHeight = 24
        backgroundColor = Color("#ffffffff")
        +tileSet {
            name = "test"
            tileWidth = 24
            tileHeight = 24
            image(Resource("$SCHEMA/testImage.png"), tileWidth, tileHeight)
        }
    }
    /*val mapBuilder = MapObjectBuilder().apply {
        width = 12
        height = 12
        tileWidth = 24
        tileHeight = 24
        backgroundColor = com.aheidelbacher.algostorm.systems.state.Color("#ffffffff")
        +tileSet {
            name = "test"
            tileWidth = 24
            tileHeight = 24
            image("testImage.png", tileWidth, tileHeight)
        }
    }*/

    @Before
    fun lockCanvas() {
        graphicsDriver.lockCanvas()
    }

    @After
    fun unlockCanvas() {
        graphicsDriver.unlockAndPostCanvas()
    }

    @Test
    fun testRenderingOrder() {
        val map = mapBuilder.apply {
            for (x in 0 until width) {
                for (y in 0 until height) {
                    entity(listOf(
                            Position(x, y),
                            Sprite(
                                    width = tileWidth,
                                    height = tileHeight,
                                    z = 0,
                                    priority = 0,
                                    gid = 1
                            )
                    ))
                }
            }
        }.build()
        val renderingSystem = RenderingSystem(map, graphicsDriver)
        renderingSystem.onRender(Render(cameraX, cameraY))
        graphicsDriver.assertColor(Color(-1))
        for (ty in 0 until height) {
            for (tx in 0 until width) {
                val y = ty * tileHeight
                val x = tx * tileWidth
                graphicsDriver.assertBitmap(
                        image = imageRes,
                        x = 0,
                        y = 0,
                        width = tileWidth,
                        height = tileHeight,
                        matrix = Matrix.identity().postTranslate(
                                dx = 1F * x - camera.x,
                                dy = 1F * y - camera.y
                        )
                )
            }
        }
        graphicsDriver.assertEmptyDrawQueue()
    }
    /*@Test
    fun testRenderTileLayer() {
        val map = mapBuilder.apply {
            +tileLayer {
                name = "floor"
                data = IntArray(width * height) { 1 }
            }
        }.build()
        val renderingSystem = RenderingSystem(map, graphicsDriver)
        renderingSystem.onRender(Render(cameraX, cameraY))
        graphicsDriver.assertColor(Color(-1))
        for (ty in 0 until height) {
            for (tx in 0 until width) {
                val y = ty * tileHeight
                val x = tx * tileWidth
                graphicsDriver.assertBitmap(
                        image = imageRes,
                        x = 0,
                        y = 0,
                        width = tileWidth,
                        height = tileHeight,
                        matrix = Matrix.identity().postTranslate(
                                dx = 1F * x - camera.x,
                                dy = 1F * y - camera.y
                        )
                )
            }
        }
        graphicsDriver.assertEmptyDrawQueue()
    }*/

    @Test
    fun testRenderColoredObjects() {
        val map = mapBuilder.apply {
            /*+entityGroup {
                name = "entities"
                +entity {
                    +Sprite(
                            width = tileWidth,
                            height = tileHeight,
                            z = 0,
                            priority = 0,
                            color = com.aheidelbacher.algostorm.systems.state.Color("#000000ff")
                    )
                    +Position(x = 0, y = 0)
                }
            }*/
            entity(listOf(
                    Sprite(
                            width = tileWidth,
                            height = tileHeight,
                            z = 0,
                            priority = 0,
                            color = Color("#000000ff")),
                    Position(x = 0, y = 0)
            ))
        }.build()
        val renderingSystem = RenderingSystem(map, graphicsDriver)
        renderingSystem.onRender(Render(cameraX, cameraY))
        graphicsDriver.assertColor(Color(-1))
        graphicsDriver.assertRectangle(
                color = Color(255),
                width = tileWidth,
                height = tileHeight,
                matrix = Matrix.identity().postTranslate(
                        dx = -1F * camera.x,
                        dy = -1F * camera.y
                )
        )
        graphicsDriver.assertEmptyDrawQueue()
    }

    @Test
    fun testRenderFlippedHorizontallyObject() {
        val map = mapBuilder.apply {
            entity(listOf(
                    Sprite(
                            width = tileWidth,
                            height = tileHeight,
                            z = 0,
                            priority = 0,
                            gid = 1.flipHorizontally(),
                            color = Color("#000000ff")),
                    Position(x = 0, y = 0)
            ))
            /*+entityGroup {
                name = "entities"
                +entity {
                    +Sprite(
                            width = tileWidth,
                            height = tileHeight,
                            z = 0,
                            priority = 0,
                            gid = 1.flipHorizontally()
                    )
                    +Position(x = 0, y = 0)
                }
            }*/
        }.build()
        val renderingSystem = RenderingSystem(map, graphicsDriver)
        renderingSystem.onRender(Render(cameraX, cameraY))
        graphicsDriver.assertColor(Color(-1))
        graphicsDriver.assertBitmap(
                image = imageRes,
                x = 0,
                y = 0,
                width = tileWidth,
                height = tileHeight,
                matrix = Matrix.identity().postScale(-1F, 1F).postTranslate(
                        dx = 1F * tileWidth - camera.x,
                        dy = -1F * camera.y
                )
        )
        graphicsDriver.assertEmptyDrawQueue()
    }

    @Test
    fun testRenderFlippedVerticallyObject() {
        val map = mapBuilder.apply {
            entity(listOf(
                    Sprite(
                            width = tileWidth,
                            height = tileHeight,
                            z = 0,
                            priority = 0,
                            gid = 1.flipVertically(),
                            color = Color("#000000ff")),
                    Position(x = 0, y = 0)
            ))
            /*+entityGroup {
                name = "entities"
                +entity {
                    +Sprite(
                            width = tileWidth,
                            height = tileHeight,
                            z = 0,
                            priority = 0,
                            gid = 1.flipVertically()
                    )
                    +Position(x = 0, y = 0)
                }
            }*/
        }.build()
        val renderingSystem = RenderingSystem(map, graphicsDriver)
        renderingSystem.onRender(Render(cameraX, cameraY))
        graphicsDriver.assertColor(Color(-1))
        graphicsDriver.assertBitmap(
                image = imageRes,
                x = 0,
                y = 0,
                width = tileWidth,
                height = tileHeight,
                matrix = Matrix.identity().postScale(1F, -1F).postTranslate(
                        dx = -1F * camera.x,
                        dy = -1F * camera.y + tileHeight
                )
        )
        graphicsDriver.assertEmptyDrawQueue()
    }

    @Test
    fun testRenderFlippedDiagonallyObject() {
        val map = mapBuilder.apply {
            entity(listOf(
                    Sprite(
                            width = tileWidth,
                            height = tileHeight,
                            z = 0,
                            priority = 0,
                            gid = 1.flipDiagonally(),
                            color = Color("#000000ff")),
                    Position(x = 0, y = 0)
            ))
            /*+entityGroup {
                name = "entities"
                +entity {
                    +Sprite(
                            width = tileWidth,
                            height = tileHeight,
                            z = 0,
                            priority = 0,
                            gid = 1.flipDiagonally()
                    )
                    +Position(x = 0, y = 0)
                }
            }*/
        }.build()
        val renderingSystem = RenderingSystem(map, graphicsDriver)
        renderingSystem.onRender(Render(cameraX, cameraY))
        graphicsDriver.assertColor(Color(-1))
        graphicsDriver.assertBitmap(
                image = imageRes,
                x = 0,
                y = 0,
                width = tileWidth,
                height = tileHeight,
                matrix = Matrix.identity()
                        .postRotate(90F)
                        .postScale(1F, -1F)
                        .postTranslate(
                                dx = 1F * tileWidth - camera.x,
                                dy = 1F * tileHeight - camera.y
                        )
        )
        graphicsDriver.assertEmptyDrawQueue()
    }
}
