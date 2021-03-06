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

package com.andreihh.algostorm.systems.graphics2d

import org.junit.After
import org.junit.Before
import org.junit.Test

/*import com.aheidelbacher.algostorm.core.ecs.Prefab.Companion.prefabOf
import com.aheidelbacher.algostorm.core.drivers.Resource
import com.aheidelbacher.algostorm.core.drivers.Resource.Companion.SCHEMA
import com.aheidelbacher.algostorm.core.engine.graphics2d.Color
import com.aheidelbacher.algostorm.core.engine.graphics2d.Matrix
import com.andreihh.algostorm.core.event.EventBus
import com.andreihh.algostorm.systems.MapObject.Builder.Companion.mapObject
import com.andreihh.algostorm.systems.RenderingSystem.Render
import com.aheidelbacher.algostorm.core.engine.graphics2d.TileSet.Builder.Companion.tileSet
import com.aheidelbacher.algostorm.core.engine.graphics2d.TileSet.Tile.Companion.flipDiagonally
import com.aheidelbacher.algostorm.core.engine.graphics2d.TileSet.Tile.Companion.flipHorizontally
import com.aheidelbacher.algostorm.core.engine.graphics2d.TileSet.Tile.Companion.flipVertically
import com.andreihh.algostorm.systems.Position
import com.andreihh.algostorm.systems.Rectangle
import com.andreihh.algostorm.test.drivers.graphics2d.GraphicsDriverMock

class RenderingSystemTest {
    val graphicsDriver = GraphicsDriverMock(320, 230)
    val width = 12
    val height = 12
    val tileWidth = 24
    val tileHeight = 24
    val imageRes = Resource("$SCHEMA/image.png")
    val cameraX = 44
    val cameraY = 60
    val camera = Rectangle(
            x = cameraX - graphicsDriver.width / 2,
            y = cameraY - graphicsDriver.height / 2,
            width = graphicsDriver.width,
            height = graphicsDriver.height
    )
    val eventBus = EventBus()
    val map = mapObject {
        width = 12
        height = 12
        tileWidth = 24
        tileHeight = 24
        backgroundColor = Color("#ffffffff")
        +tileSet {
            name = "test"
            tileWidth = 24
            tileHeight = 24
            image(Resource("$SCHEMA/image.png"), tileWidth, tileHeight)
        }
    }
    val system = RenderingSystem(map, graphicsDriver)

    @Before fun lockCanvas() {
        graphicsDriver.lockCanvas()
        eventBus.subscribe(system)
    }

    @After fun unlockCanvas() {
        eventBus.unsubscribe(system)
        graphicsDriver.unlockAndPostCanvas()
    }

    @Test fun testRenderingOrder() {
        for (x in 0 until width) {
            for (y in 0 until height) {
                map.entities.create(prefabOf(
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
        system.onRender(Render(cameraX, cameraY))
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

    /*@Test fun testRenderColoredObjects() {
        map.entities.create(prefabOf(
                Sprite(
                        width = tileWidth,
                        height = tileHeight,
                        z = 0,
                        priority = 0,
                        color = Color("#000000ff")
                ),
                Position(x = 0, y = 0)
        ))
        system.onRender(Render(cameraX, cameraY))
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
    }*/

    @Test fun testRenderFlippedHorizontallyObject() {
        map.entities.create(prefabOf(
                Sprite(
                        width = tileWidth,
                        height = tileHeight,
                        z = 0,
                        priority = 0,
                        gid = 1.flipHorizontally()
                ),
                Position(x = 0, y = 0)
        ))
        system.onRender(Render(cameraX, cameraY))
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

    @Test fun testRenderFlippedVerticallyObject() {
        map.entities.create(prefabOf(
                Sprite(
                        width = tileWidth,
                        height = tileHeight,
                        z = 0,
                        priority = 0,
                        gid = 1.flipVertically()
                ),
                Position(x = 0, y = 0)
        ))
        system.onRender(Render(cameraX, cameraY))
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

    @Test fun testRenderFlippedDiagonallyObject() {
        map.entities.create(prefabOf(
                Sprite(
                        width = tileWidth,
                        height = tileHeight,
                        z = 0,
                        priority = 0,
                        gid = 1.flipDiagonally()
                ),
                Position(x = 0, y = 0)
        ))
        system.onRender(Render(cameraX, cameraY))
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
}*/
