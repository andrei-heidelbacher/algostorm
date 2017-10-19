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

package com.aheidelbacher.sokoban.core

import com.aheidelbacher.algostorm.core.drivers.client.audio.AudioDriver
import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.Color
import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.GraphicsDriver
import com.aheidelbacher.algostorm.core.drivers.client.input.Input
import com.aheidelbacher.algostorm.core.drivers.client.input.InputDriver
import com.aheidelbacher.algostorm.core.drivers.client.input.InputDriver.GestureInterpreter
import com.aheidelbacher.algostorm.core.drivers.io.FileSystem.Companion.loadResource
import com.aheidelbacher.algostorm.core.drivers.io.FileSystemDriver
import com.aheidelbacher.algostorm.core.drivers.io.Resource.Companion.resourceOf
import com.aheidelbacher.algostorm.core.ecs.EntityRef.Id
import com.aheidelbacher.algostorm.core.ecs.Prefab.Companion.prefabOf
import com.aheidelbacher.algostorm.core.ecs.System
import com.aheidelbacher.algostorm.core.ecs.System.Companion.ENTITY_POOL
import com.aheidelbacher.algostorm.core.engine.Engine
import com.aheidelbacher.algostorm.core.event.EventBus
import com.aheidelbacher.algostorm.systems.EventSystem.Companion.EVENT_BUS
import com.aheidelbacher.algostorm.systems.MapObject
import com.aheidelbacher.algostorm.systems.MapObject.Builder.Companion.mapObject
import com.aheidelbacher.algostorm.systems.Update
import com.aheidelbacher.algostorm.systems.graphics2d.Animation
import com.aheidelbacher.algostorm.systems.graphics2d.AnimationSystem
import com.aheidelbacher.algostorm.systems.graphics2d.Camera
import com.aheidelbacher.algostorm.systems.graphics2d.CameraSystem
import com.aheidelbacher.algostorm.systems.graphics2d.CameraSystem.Follow
import com.aheidelbacher.algostorm.systems.graphics2d.CameraSystem.Scroll
import com.aheidelbacher.algostorm.systems.graphics2d.CameraSystem.UpdateCamera
import com.aheidelbacher.algostorm.systems.graphics2d.GraphicsSystem.Companion.CAMERA
import com.aheidelbacher.algostorm.systems.graphics2d.GraphicsSystem.Companion.CANVAS
import com.aheidelbacher.algostorm.systems.graphics2d.GraphicsSystem.Companion.TILE_HEIGHT
import com.aheidelbacher.algostorm.systems.graphics2d.GraphicsSystem.Companion.TILE_SET_COLLECTION
import com.aheidelbacher.algostorm.systems.graphics2d.GraphicsSystem.Companion.TILE_WIDTH
import com.aheidelbacher.algostorm.systems.graphics2d.RenderingSystem
import com.aheidelbacher.algostorm.systems.graphics2d.RenderingSystem.Companion.BACKGROUND
import com.aheidelbacher.algostorm.systems.graphics2d.RenderingSystem.Render
import com.aheidelbacher.algostorm.systems.graphics2d.Sprite
import com.aheidelbacher.algostorm.systems.graphics2d.TileSet
import com.aheidelbacher.algostorm.systems.graphics2d.TileSetCollection
import com.aheidelbacher.algostorm.systems.input.InputSystem.Companion.INPUT_DRIVER
import com.aheidelbacher.algostorm.systems.input.InputSystem.HandleInput
import com.aheidelbacher.algostorm.systems.physics2d.Body
import com.aheidelbacher.algostorm.systems.physics2d.PathFindingSystem
import com.aheidelbacher.algostorm.systems.physics2d.PathFindingSystem.FindPath
import com.aheidelbacher.algostorm.systems.physics2d.PhysicsSystem
import com.aheidelbacher.algostorm.systems.physics2d.PhysicsSystem.TransformIntent
import com.aheidelbacher.algostorm.systems.physics2d.Position

class SokobanEngine(
        audioDriver: AudioDriver,
        graphicsDriver: GraphicsDriver,
        inputDriver: InputDriver,
        fileSystemDriver: FileSystemDriver
) : Engine(
        audioDriver = audioDriver,
        graphicsDriver = graphicsDriver,
        inputDriver = inputDriver,
        fileSystemDriver = fileSystemDriver
) {
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
            tileSet(resourceOf("sokoban_tileset.json"))

            fun floor(x: Int, y: Int) = prefabOf(
                    Position(x, y),
                    Sprite(
                            width = tileWidth,
                            height = tileHeight,
                            z = 0,
                            priority = 0,
                            gid = 90
                    )
            )

            fun wall(x: Int, y: Int) = prefabOf(
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

            fun player(x: Int, y: Int) = prefabOf(
                    Position(x, y),
                    Animation("player", "idle", 0, true),
                    Sprite(
                            width = tileWidth,
                            height = tileHeight,
                            z = 0,
                            priority = 1,
                            gid = 53
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
        val tileSetCollection = map.tileSets.map { resource ->
            fileSystemDriver.loadResource(resource)
        }.onEach { tileSet ->
            graphicsDriver.loadBitmap(tileSet.image.source)
        }.let(::TileSetCollection)
        val context = mapOf(
                ENTITY_POOL to map.entityPool,
                EVENT_BUS to eventBus,
                TILE_WIDTH to map.tileWidth,
                TILE_HEIGHT to map.tileHeight,
                BACKGROUND to Color("#FF000000"),
                TILE_SET_COLLECTION to tileSetCollection,
                CAMERA to camera,
                CANVAS to graphicsDriver,
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
        map.entityPool.clear()
    }
}

