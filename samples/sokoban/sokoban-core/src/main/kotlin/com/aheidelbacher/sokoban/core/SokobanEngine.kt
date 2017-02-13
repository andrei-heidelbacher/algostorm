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

import com.aheidelbacher.algostorm.core.ecs.EntityRef.Id
import com.aheidelbacher.algostorm.core.ecs.Prefab.Companion.prefabOf
import com.aheidelbacher.algostorm.core.engine.Engine
import com.aheidelbacher.algostorm.core.engine.audio.AudioDriver
import com.aheidelbacher.algostorm.core.engine.driver.Resource
import com.aheidelbacher.algostorm.core.engine.driver.Resource.Companion.SCHEMA
import com.aheidelbacher.algostorm.core.engine.graphics2d.Color
import com.aheidelbacher.algostorm.core.engine.graphics2d.GraphicsDriver
import com.aheidelbacher.algostorm.core.engine.input.InputDriver
import com.aheidelbacher.algostorm.core.engine.input.InputListener
import com.aheidelbacher.algostorm.core.engine.input.PollingInputListener
import com.aheidelbacher.algostorm.core.engine.serialization.Deserializer.Companion.readValue
import com.aheidelbacher.algostorm.core.event.EventBus
import com.aheidelbacher.algostorm.core.event.Subscriber
import com.aheidelbacher.algostorm.systems.MapObject
import com.aheidelbacher.algostorm.systems.MapObject.Builder.Companion.mapObject
import com.aheidelbacher.algostorm.drivers.json.JsonDriver
import com.aheidelbacher.algostorm.drivers.kts.KotlinScriptDriver
import com.aheidelbacher.algostorm.systems.Update
import com.aheidelbacher.algostorm.systems.graphics2d.Camera
import com.aheidelbacher.algostorm.systems.graphics2d.CameraSystem
import com.aheidelbacher.algostorm.systems.graphics2d.CameraSystem.Follow
import com.aheidelbacher.algostorm.systems.graphics2d.CameraSystem.Scroll
import com.aheidelbacher.algostorm.systems.graphics2d.CameraSystem.UpdateCamera
import com.aheidelbacher.algostorm.systems.graphics2d.RenderingSystem
import com.aheidelbacher.algostorm.systems.graphics2d.RenderingSystem.Render
import com.aheidelbacher.algostorm.systems.graphics2d.Sprite
import com.aheidelbacher.algostorm.core.engine.graphics2d.TileSet
import com.aheidelbacher.algostorm.core.engine.graphics2d.TileSetCollection
import com.aheidelbacher.algostorm.systems.graphics2d.Animation
import com.aheidelbacher.algostorm.systems.graphics2d.AnimationSystem
import com.aheidelbacher.algostorm.systems.physics2d.Body
import com.aheidelbacher.algostorm.systems.physics2d.PathFindingSystem
import com.aheidelbacher.algostorm.systems.physics2d.PathFindingSystem.FindPath
import com.aheidelbacher.algostorm.systems.physics2d.PhysicsSystem
import com.aheidelbacher.algostorm.systems.physics2d.PhysicsSystem.TransformIntent
import com.aheidelbacher.algostorm.systems.physics2d.Position

import java.io.InputStream
import java.io.OutputStream

class SokobanEngine(
        audioDriver: AudioDriver,
        graphicsDriver: GraphicsDriver,
        inputDriver: InputDriver
) : Engine(
        audioDriver = audioDriver,
        graphicsDriver = graphicsDriver,
        inputDriver = inputDriver,
        scriptDriver = KotlinScriptDriver(),
        serializationDriver = JsonDriver()
) {
    private val eventBus = EventBus()
    private lateinit var map: MapObject
    private val camera = Camera(0, 0)
    private lateinit var systems: List<Subscriber>
    private val inputListener = PollingInputListener()

    override val millisPerUpdate: Int = 30

    override fun onInit(inputStream: InputStream?) {
        inputDriver.addListener(inputListener)
        map = inputStream?.let {
            serializationDriver.readValue<MapObject>(it)
        } ?: mapObject {
            width = 8
            height = 8
            tileWidth = 64
            tileHeight = 64
            tileSet(Resource("$SCHEMA/sokoban_tileset.json"))

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
            resource.inputStream().use { src ->
                val tileSet = serializationDriver.readValue<TileSet>(src)
                graphicsDriver.loadBitmap(tileSet.image.resource)
                tileSet
            }
        }.let(::TileSetCollection)
        systems = listOf(
                RenderingSystem(
                        map.tileWidth,
                        map.tileHeight,
                        Color("#FF000000"),
                        tileSetCollection,
                        graphicsDriver,
                        map.entityPool.group
                ),
                CameraSystem(
                        map.tileWidth,
                        map.tileHeight,
                        camera,
                        map.entityPool.group,
                        Id(1)
                ),
                PhysicsSystem(map.entityPool.group),
                PathFindingSystem(map.entityPool.group),
                AnimationSystem(map.entityPool.group, tileSetCollection)
        )
    }

    override fun onStart() {
        systems.forEach { eventBus.subscribe(it) }
    }

    override fun onRender() {
        if (graphicsDriver.isCanvasReady) {
            graphicsDriver.lockCanvas()
            eventBus.post(Render(camera.x, camera.y))
            eventBus.publishPosts()
            graphicsDriver.unlockAndPostCanvas()
        }
    }

    override fun onHandleInput() {
        inputListener.pollMostRecent(object : InputListener {
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
        })
    }

    override fun onUpdate() {
        eventBus.post(Update(millisPerUpdate))
        eventBus.post(UpdateCamera)
        eventBus.publishPosts()
    }

    override fun onSerializeState(outputStream: OutputStream) {
        serializationDriver.writeValue(outputStream, map)
    }

    override fun onStop() {
        systems.forEach { eventBus.unsubscribe(it) }
    }

    override fun onShutdown() {
        map.entityPool.clear()
    }
}

