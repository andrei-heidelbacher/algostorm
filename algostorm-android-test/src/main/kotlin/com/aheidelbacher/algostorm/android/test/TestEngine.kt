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

package com.aheidelbacher.algostorm.android.test

import com.aheidelbacher.algostorm.engine.Engine
import com.aheidelbacher.algostorm.engine.audio.AudioDriver
import com.aheidelbacher.algostorm.engine.graphics2d.Color
import com.aheidelbacher.algostorm.engine.graphics2d.GraphicsDriver
import com.aheidelbacher.algostorm.engine.graphics2d.Matrix
import com.aheidelbacher.algostorm.engine.input.InputDriver
import com.aheidelbacher.algostorm.event.EventBus
import com.aheidelbacher.algostorm.data.MapObject.Builder.Companion.mapObject
import com.aheidelbacher.algostorm.data.TileSet.Builder.Companion.tileSet
import com.aheidelbacher.algostorm.engine.driver.Resource
import com.aheidelbacher.algostorm.engine.driver.Resource.Companion.SCHEMA
import com.aheidelbacher.algostorm.engine.input.InputListener
import com.aheidelbacher.algostorm.engine.input.PollingInputListener
import com.aheidelbacher.algostorm.systems.Update
import com.aheidelbacher.algostorm.systems.graphics2d.Camera
import com.aheidelbacher.algostorm.systems.graphics2d.CameraSystem
import com.aheidelbacher.algostorm.systems.graphics2d.CameraSystem.Follow
import com.aheidelbacher.algostorm.systems.graphics2d.CameraSystem.Scroll
import com.aheidelbacher.algostorm.systems.graphics2d.CameraSystem.UpdateCamera
import com.aheidelbacher.algostorm.systems.graphics2d.RenderingSystem
import com.aheidelbacher.algostorm.systems.graphics2d.RenderingSystem.Render
import com.aheidelbacher.algostorm.systems.graphics2d.Sprite
import com.aheidelbacher.algostorm.systems.physics2d.Body
import com.aheidelbacher.algostorm.systems.physics2d.PhysicsSystem
import com.aheidelbacher.algostorm.systems.physics2d.PhysicsSystem.FindPath
import com.aheidelbacher.algostorm.systems.physics2d.PhysicsSystem.TransformIntent
import com.aheidelbacher.algostorm.systems.physics2d.Position

import java.io.OutputStream

class TestEngine(
        audioDriver: AudioDriver,
        graphicsDriver: GraphicsDriver,
        inputDriver: InputDriver
) : Engine(audioDriver, graphicsDriver, inputDriver) {
    private val eventBus = EventBus()
    private val map = mapObject {
        width = 32
        height = 32
        tileWidth = 32
        tileHeight = 32
        +tileSet {
            name = "world"
            tileWidth = 32
            tileHeight = 32
            image(Resource("$SCHEMA/tileset.png"), 2048, 1536)
        }
        for (x in 0 until width) {
            for (y in 0 until height) {
                entity(listOf(
                        Position(x, y),
                        Sprite(
                                width = tileWidth,
                                height = tileHeight,
                                z = 0,
                                priority = 0,
                                gid = 961
                        )
                ))
            }
        }
        for (x in 0 until width) {
            for (y in listOf(0, height - 1)) {
                entity(listOf(
                        Position(x, y),
                        Sprite(
                                width = tileWidth,
                                height = tileHeight,
                                z = 0,
                                priority = 1,
                                gid = 1089
                        ),
                        Body.STATIC
                ))
            }
        }
        for (x in listOf(0, width - 1)) {
            for (y in 1 until height - 1) {
                entity(listOf(
                        Position(x, y),
                        Sprite(
                                width = tileWidth,
                                height = tileHeight,
                                z = 0,
                                priority = 1,
                                gid = 1089
                        ),
                        Body.STATIC
                ))
            }
        }
        for (x in 1 until width - 1 step 4) {
            for (y in 1 until height - 1 step 4) {
                entity(listOf(
                        Position(x, y),
                        Sprite(
                                width = tileWidth,
                                height = tileHeight,
                                z = 0,
                                priority = 1,
                                gid = 1089
                        ),
                        Body.STATIC
                ))
            }
        }
        entity(1, listOf(
                Position(15, 15),
                Sprite(
                        width = tileWidth,
                        height = tileHeight,
                        z = 0,
                        priority = 1,
                        gid = 129
                ),
                Body.KINEMATIC
        ))
    }
    private val camera = Camera(0, 0)
    private val systems = listOf(
            RenderingSystem(map, graphicsDriver),
            CameraSystem(
                    map.tileWidth,
                    map.tileHeight,
                    camera,
                    map.entityPool.group,
                    1
            ),
            PhysicsSystem(map.entityPool.group)
    )
    private val inputListener = PollingInputListener()

    init {
        inputDriver.addListener(inputListener)
    }

    override val millisPerUpdate: Int
        get() = 25

    override fun onStart() {
        systems.forEach { eventBus.subscribe(it) }
    }

    override fun onRender() {
        if (graphicsDriver.isCanvasReady) {
            graphicsDriver.lockCanvas()
            eventBus.publish(UpdateCamera)
            eventBus.publish(Render(camera.x, camera.y))
            graphicsDriver.unlockAndPostCanvas()
        }
    }

    override fun onHandleInput() {
        inputListener.pollMostRecent(object : InputListener {
            override fun onScroll(dx: Int, dy: Int) {
                eventBus.post(Scroll(dx, dy))
            }

            override fun onTouch(x: Int, y: Int) {
                try {
                    val tx = (x + camera.x - graphicsDriver.width / 2) / map.tileWidth
                    val ty = (y + camera.y - graphicsDriver.height / 2) / map.tileHeight
                    eventBus.post(Follow(1))
                    val path = eventBus.request(FindPath(1, tx, ty))
                    path?.forEach { d ->
                        eventBus.post(TransformIntent(1, d.dx, d.dy))
                    }
                } catch (e : Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    override fun onUpdate() {
        eventBus.post(Update(millisPerUpdate))
        eventBus.publishPosts()
    }

    override fun onSerializeState(outputStream: OutputStream) {
        serializationDriver.writeValue(outputStream, map)
    }

    override fun onStop() {
        systems.forEach { eventBus.unsubscribe(it) }
    }

    override fun onShutdown() {
    }
}
