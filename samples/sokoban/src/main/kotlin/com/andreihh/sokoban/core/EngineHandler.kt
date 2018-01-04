/*
 * Copyright 2018 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.andreihh.sokoban.core

import com.andreihh.algostorm.core.drivers.graphics2d.Color
import com.andreihh.algostorm.core.ecs.EntityRef.Id
import com.andreihh.algostorm.core.ecs.System
import com.andreihh.algostorm.core.ecs.System.Companion.ENTITY_POOL
import com.andreihh.algostorm.core.engine.Handler
import com.andreihh.algostorm.core.event.EventBus
import com.andreihh.algostorm.systems.EventSystem.Companion.EVENT_BUS
import com.andreihh.algostorm.systems.MapObject
import com.andreihh.algostorm.systems.MapObject.Builder.Companion.mapObject
import com.andreihh.algostorm.systems.Update
import com.andreihh.algostorm.systems.graphics2d.Animation
import com.andreihh.algostorm.systems.graphics2d.AnimationSystem
import com.andreihh.algostorm.systems.graphics2d.Camera
import com.andreihh.algostorm.systems.graphics2d.CameraSystem
import com.andreihh.algostorm.systems.graphics2d.CameraSystem.Follow
import com.andreihh.algostorm.systems.graphics2d.CameraSystem.UpdateCamera
import com.andreihh.algostorm.systems.graphics2d.GraphicsSystem.Companion.CAMERA
import com.andreihh.algostorm.systems.graphics2d.GraphicsSystem.Companion.GRAPHICS_DRIVER
import com.andreihh.algostorm.systems.graphics2d.GraphicsSystem.Companion.TILE_HEIGHT
import com.andreihh.algostorm.systems.graphics2d.GraphicsSystem.Companion.TILE_SET_COLLECTION
import com.andreihh.algostorm.systems.graphics2d.GraphicsSystem.Companion.TILE_WIDTH
import com.andreihh.algostorm.systems.graphics2d.RenderingSystem
import com.andreihh.algostorm.systems.graphics2d.RenderingSystem.Companion.BACKGROUND
import com.andreihh.algostorm.systems.graphics2d.RenderingSystem.Render
import com.andreihh.algostorm.systems.graphics2d.Sprite
import com.andreihh.algostorm.systems.graphics2d.TileSet.Companion.flipHorizontally
import com.andreihh.algostorm.systems.graphics2d.TileSet.Companion.flipVertically
import com.andreihh.algostorm.systems.graphics2d.TileSet.Companion.flipDiagonally
import com.andreihh.algostorm.systems.graphics2d.TileSetCollection
import com.andreihh.algostorm.systems.input.InputSystem.Companion.INPUT_DRIVER
import com.andreihh.algostorm.systems.input.InputSystem.HandleInput
import com.andreihh.algostorm.systems.physics2d.Body
import com.andreihh.algostorm.systems.physics2d.PathFindingSystem
import com.andreihh.algostorm.systems.physics2d.PhysicsSystem
import com.andreihh.algostorm.systems.physics2d.Position

class EngineHandler : Handler() {
    private val eventBus = EventBus()
    private lateinit var map: MapObject
    private val camera = Camera()
    private val systems = listOf(
        RenderingSystem(),
        CameraSystem(),
        PhysicsSystem(),
        PathFindingSystem(),
        AnimationSystem(),
        InputInterpretingSystem()
    )

    override val millisPerUpdate: Int = 30

    override fun onInit(args: Map<String, Any?>) {
        Thread.sleep(1000)
        map = mapObject {
            width = 8
            height = 8
            tileWidth = 64
            tileHeight = 64
            backgroundColor = Color("#FF000000")
            tileSet {
                name = "sokoban"
                tileWidth = 64
                tileHeight = 64
                image {
                    source = "res:///sokoban_tileset.png"
                    width = 832
                    height = 512
                }
                animation(name = "player:idle") {
                    frame(tileId = 52, duration = 250)
                    frame(tileId = 53, duration = 250)
                    frame(tileId = 52, duration = 250)
                    frame(tileId = 54, duration = 250)
                }
            }

            fun floor(x: Int, y: Int) = setOf(
                    Position(x, y),
                    Sprite(
                            width = tileWidth,
                            height = tileHeight,
                            z = 0,
                            priority = 0,
                            gid = 90
                    )
            )

            fun wall(x: Int, y: Int) = setOf(
                    Position(x, y),
                    Sprite(
                            width = tileWidth,
                            height = tileHeight,
                            z = 0,
                            priority = 1,
                            gid = 98
                    ),
                    Body.STATIC
            )

            fun player(x: Int, y: Int) = setOf(
                    Position(x, y),
                    Animation("player", "idle", 0, true),
                    Sprite(
                            width = tileWidth,
                            height = tileHeight,
                            z = 0,
                            priority = 1,
                            gid = 53.flipHorizontally().flipVertically().flipDiagonally()
                    ),
                    Body.KINEMATIC
            )

            for (x in 0 until width) {
                for (y in 0 until height) {
                    entity(floor(x, y))
                }
            }
            for (x in 0 until width) {
                for (y in listOf(0, height - 1)) {
                    entity(wall(x, y))
                }
            }
            for (x in listOf(0, width - 1)) {
                for (y in 1 until height - 1) {
                    entity(wall(x, y))
                }
            }
            for (x in 1 until width - 1 step 4) {
                for (y in 1 until height - 1 step 4) {
                    entity(wall(x, y))
                }
            }
            entity(Id(1), player(3, 3))
        }
        map.tileSets.forEach { graphicsDriver.loadBitmap(it.image.source) }
        val context = mapOf(
                ENTITY_POOL to map.entities,
                EVENT_BUS to eventBus,
                TILE_WIDTH to map.tileWidth,
                TILE_HEIGHT to map.tileHeight,
                BACKGROUND to map.backgroundColor,
                TILE_SET_COLLECTION to TileSetCollection(map.tileSets),
                CAMERA to camera,
                GRAPHICS_DRIVER to graphicsDriver,
                INPUT_DRIVER to inputDriver
        )
        systems.forEach { it.initialize(context) }
        eventBus.post(Follow(Id(1)))
    }

    override fun onError(cause: Exception) {
        cause.printStackTrace()
    }

    override fun onStart() {
        systems.forEach(System::start)
    }

    private fun onRender() {
        if (graphicsDriver.isCanvasReady) {
            graphicsDriver.lockCanvas()
            eventBus.post(Render(camera.x, camera.y))
            eventBus.publishPosts()
            graphicsDriver.save()
            graphicsDriver.translate(50f, 0f)
            graphicsDriver.translate(100f, 0f)
            graphicsDriver.scale(-1f, 1f)
            graphicsDriver.rotate(45f)
            //graphicsDriver.rotate(45f)
            graphicsDriver.drawRectangle(Color("#FFFF0000"), 100, 100)
            graphicsDriver.restore()
            graphicsDriver.unlockAndPostCanvas()
        }
    }

    override fun onUpdate() {
        onRender()
        eventBus.post(HandleInput)
        eventBus.post(Update(millisPerUpdate))
        eventBus.post(UpdateCamera)
        eventBus.publishPosts()
    }

    override fun onStop() {
        systems.forEach(System::stop)
    }

    override fun onRelease() {
        map.entities.clear()
    }
}

