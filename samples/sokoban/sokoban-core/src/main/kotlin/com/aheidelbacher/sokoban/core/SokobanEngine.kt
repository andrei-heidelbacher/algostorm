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
import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.TileSet
import com.aheidelbacher.algostorm.core.drivers.client.input.InputDriver
import com.aheidelbacher.algostorm.core.drivers.io.FileSystemDriver
import com.aheidelbacher.algostorm.core.drivers.io.Resource.Companion.resourceOf
import com.aheidelbacher.algostorm.core.ecs.EntityRef.Id
import com.aheidelbacher.algostorm.core.ecs.Prefab.Companion.prefabOf
import com.aheidelbacher.algostorm.core.engine.Engine
import com.aheidelbacher.algostorm.core.event.EventBus
import com.aheidelbacher.algostorm.core.event.Service
import com.aheidelbacher.algostorm.core.drivers.serialization.JsonDriver
import com.aheidelbacher.algostorm.systems.MapObject
import com.aheidelbacher.algostorm.systems.MapObject.Builder.Companion.mapObject
import com.aheidelbacher.algostorm.systems.Update
import com.aheidelbacher.algostorm.systems.graphics2d.Animation
import com.aheidelbacher.algostorm.systems.graphics2d.AnimationService
import com.aheidelbacher.algostorm.systems.graphics2d.Camera
import com.aheidelbacher.algostorm.systems.graphics2d.CameraService
import com.aheidelbacher.algostorm.systems.graphics2d.CameraService.UpdateCamera
import com.aheidelbacher.algostorm.systems.graphics2d.RenderingService
import com.aheidelbacher.algostorm.systems.graphics2d.RenderingService.Render
import com.aheidelbacher.algostorm.systems.graphics2d.Sprite
import com.aheidelbacher.algostorm.systems.graphics2d.TileSetCollection
import com.aheidelbacher.algostorm.systems.physics2d.Body
import com.aheidelbacher.algostorm.systems.physics2d.PathFindingService
import com.aheidelbacher.algostorm.systems.physics2d.PhysicsService
import com.aheidelbacher.algostorm.systems.physics2d.Position

import java.io.InputStream
import java.io.OutputStream

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
    private val camera = Camera(0, 0)
    private lateinit var services: List<Service>

    override val millisPerUpdate: Int = 30

    override fun onInit(inputStream: InputStream?) {
        map = inputStream?.let {
            JsonDriver.deserialize<MapObject>(it)
        } ?: mapObject {
            width = 8
            height = 8
            tileWidth = 64
            tileHeight = 64
            tileSet(resourceOf("assets/sokoban_tileset.json"))

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
            javaClass.getResourceAsStream("/${resource.path}")
                    .use { src ->
                        val tileSet = JsonDriver.deserialize<TileSet>(src)
                        graphicsDriver.loadImage(tileSet.image.resource)
                        tileSet
                    }
        }.let(::TileSetCollection)
        services = listOf(
                RenderingService(
                        map.entityPool.group,
                        map.tileWidth,
                        map.tileHeight,
                        Color("#FF000000"),
                        tileSetCollection,
                        graphicsDriver
                ),
                CameraService(
                        map.entityPool.group,
                        map.tileWidth,
                        map.tileHeight,
                        camera,
                        Id(1)
                ),
                PhysicsService(map.entityPool.group),
                PathFindingService(map.entityPool.group),
                AnimationService(map.entityPool.group, tileSetCollection)
        )
    }

    override fun onError(cause: Exception) {
        cause.printStackTrace()
    }

    override fun onStart() {
        services.forEach { it.start(eventBus) }
    }

    private fun onRender() {
        if (graphicsDriver.isCanvasReady) {
            graphicsDriver.lockCanvas()
            eventBus.post(Render(camera.x, camera.y))
            eventBus.publishPosts()
            graphicsDriver.unlockAndPostCanvas()
        }
    }

    private fun onHandleInput() {
        /*inputListener.pollMostRecent(object {
            override fun onScroll(dx: Int, dy: Int) {
                eventBus.post(Scroll(dx, dy))
            }

            override fun onTouch(x: Int, y: Int) {
                val tx = (x + camera.x - graphicsDriver.width / 2) /
                        map.tileWidth
                val ty = (y + camera.y - graphicsDriver.height / 2) /
                        map.tileHeight
                val path = eventBus.request(FindPath(Id(1), tx, ty))
                path?.let { eventBus.post(Follow(Id(1))) }
                path?.forEach { d ->
                    eventBus.post(TransformIntent(Id(1), d.dx, d.dy))
                }
            }
        })*/
    }

    override fun onUpdate() {
        onRender()
        onHandleInput()
        eventBus.post(Update(millisPerUpdate))
        eventBus.post(UpdateCamera)
        eventBus.publishPosts()
    }

    override fun onSerializeState(outputStream: OutputStream) {
        JsonDriver.serialize(outputStream, map)
    }

    override fun onStop() {
        services.forEach { it.stop() }
    }

    override fun onShutdown() {
        map.entityPool.clear()
    }
}

