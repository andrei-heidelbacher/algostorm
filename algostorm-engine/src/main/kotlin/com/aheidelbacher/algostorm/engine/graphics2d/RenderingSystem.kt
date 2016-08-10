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

import com.aheidelbacher.algostorm.engine.state.Layer
import com.aheidelbacher.algostorm.engine.state.Map
import com.aheidelbacher.algostorm.engine.state.Map.RenderOrder
import com.aheidelbacher.algostorm.engine.state.Object
import com.aheidelbacher.algostorm.engine.state.TileSet.Tile.Companion.isFlippedDiagonally
import com.aheidelbacher.algostorm.engine.state.TileSet.Tile.Companion.isFlippedHorizontally
import com.aheidelbacher.algostorm.engine.state.TileSet.Tile.Companion.isFlippedVertically
import com.aheidelbacher.algostorm.engine.state.TileSet.Viewport
import com.aheidelbacher.algostorm.engine.time.Tick
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber

import kotlin.comparisons.compareBy

/**
 * A system which handles the rendering of all objects in the game to the screen
 * canvas.
 *
 * Every method call to the [canvas] is made from the private engine thread.
 *
 * @property map the map which should be rendered
 * @property canvas the canvas to which the system draws
 */
class RenderingSystem(
        private val map: Map,
        private val canvas: Canvas
) : Subscriber {
    private companion object {
        /**
         * A camera representing the captured area by the screen.
         *
         * @property x the x-axis coordinate of the upper-left corner of the
         * camera in pixels
         * @property y the y-axis coordinate of the upper-left corner of the
         * camera in pixels
         * @property width the width of the camera in pixels
         * @property height the height of the camera in pixels
         */
        data class Camera(
                val x: Int,
                val y: Int,
                val width: Int,
                val height: Int
        )

        fun isVisible(
                camera: Camera,
                gid: Int,
                x: Int,
                y: Int,
                width: Int,
                height: Int,
                rotation: Float
        ): Boolean = gid != 0 &&
                x + width > camera.x && x < camera.x + camera.width &&
                y + height > camera.y && y < camera.y + camera.height

        fun isVisible(camera: Camera, obj: Object): Boolean =
                obj.isVisible && isVisible(
                        camera = camera,
                        gid = obj.gid,
                        x = obj.x,
                        y = obj.y,
                        width = obj.width,
                        height = obj.height,
                        rotation = obj.rotation
                )
    }

    init {
        map.tileSets.forEach { canvas.loadBitmap(it.image) }
    }

    private var currentTimeMillis: Long = 0L

    private fun drawGid(
            gid: Int,
            opacity: Float,
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            rotation: Float
    ) {
        val tileSet = map.getTileSet(gid) ?: error("Invalid gid $gid!")
        val localTileId = map.getTileId(gid) ?: error("Invalid gid $gid!")
        val animation = tileSet.tiles[localTileId]?.animation
        val tileId = if (animation == null) {
            localTileId
        } else {
            var elapsedTimeMillis = currentTimeMillis % animation.sumBy {
                it.duration
            }
            animation.dropWhile {
                elapsedTimeMillis -= it.duration
                elapsedTimeMillis >= 0
            }.first().tileId
        }
        val viewport = tileSet.getViewport(tileId)
        val matrix = Matrix.scale(
                sx = width.toFloat() / viewport.width.toFloat(),
                sy = height.toFloat() / viewport.height.toFloat()
        ).let {
            if (!gid.isFlippedDiagonally) it
            else it.postRotate(90F)
                    .postScale(1F, -1F)
                    .postTranslate(width.toFloat(), 0F)
        }.let {
            if (!gid.isFlippedHorizontally) it
            else it.postScale(-1F, 1F).postTranslate(width.toFloat(), 0F)
        }.let {
            if (!gid.isFlippedVertically) it
            else it.postScale(1F, -1F).postTranslate(0F, height.toFloat())
        }.postRotate(rotation).postTranslate(x.toFloat(), y.toFloat())
        canvas.drawBitmap(
                viewport = viewport,
                matrix = matrix,
                opacity = opacity
        )
    }

    private fun drawImageLayer(camera: Camera, imageLayer: Layer.ImageLayer) {
        canvas.drawBitmap(
                viewport = Viewport(
                        image = imageLayer.image,
                        x = camera.x - imageLayer.offsetX,
                        y = camera.y - imageLayer.offsetY,
                        width = camera.width,
                        height = camera.height
                ),
                matrix = Matrix.IDENTITY,
                opacity = imageLayer.opacity
        )
    }

    private fun drawObjectGroup(camera: Camera, layer: Layer.ObjectGroup) {
        val comparator = when (map.renderOrder) {
            RenderOrder.RIGHT_DOWN -> compareBy<Object>({ it.y }, { it.x })
            RenderOrder.RIGHT_UP -> compareBy<Object>({ -it.y }, { it.x })
            RenderOrder.LEFT_DOWN -> compareBy<Object>({ it.y }, { -it.x })
            RenderOrder.LEFT_UP -> compareBy<Object>({ -it.y }, { -it.x })
        }
        layer.objects.filter {
            isVisible(camera, it)
        }.sortedWith(comparator).forEach {
            drawGid(
                    gid = it.gid,
                    opacity = layer.opacity,
                    x = it.x + layer.offsetX - camera.x,
                    y = it.y + layer.offsetY - camera.y,
                    width = it.width,
                    height = it.height,
                    rotation = it.rotation
            )
        }
    }

    private fun drawTileLayer(camera: Camera, layer: Layer.TileLayer) {
        val tileWidth = map.tileWidth
        val tileHeight = map.tileHeight
        val (yRange, xRange) = when (map.renderOrder) {
            RenderOrder.RIGHT_DOWN -> Pair(
                    0 until map.height * tileHeight step tileHeight,
                    0 until map.width * tileWidth step tileWidth
            )
            RenderOrder.RIGHT_UP -> Pair(
                    (map.height - 1) * tileHeight downTo 0 step tileHeight,
                    0 until map.width * tileWidth step tileWidth
            )
            RenderOrder.LEFT_DOWN -> Pair(
                    0 until map.height * tileHeight step tileHeight,
                    (map.width - 1) * tileWidth downTo 0 step tileWidth
            )
            RenderOrder.LEFT_UP -> Pair(
                    (map.height - 1) * tileHeight downTo 0 step tileHeight,
                    (map.width - 1) * tileWidth downTo 0 step tileWidth
            )
        }
        for (y in yRange) {
            for (x in xRange) {
                val index = y / map.tileHeight * map.width + x / map.tileWidth
                val gid = layer.data[index]
                val tileSet = map.getTileSet(gid)
                if (gid != 0) {
                    tileSet ?: error("Invalid gid $gid!")
                    if (isVisible(
                            camera = camera,
                            gid = gid,
                            x = x + layer.offsetX,
                            y = y + layer.offsetY,
                            width = tileSet.tileWidth,
                            height = tileSet.tileHeight,
                            rotation = 0F
                    )) {
                        drawGid(
                                gid = gid,
                                opacity = layer.opacity,
                                x = x + layer.offsetX - camera.x,
                                y = y + layer.offsetY - camera.y,
                                width = tileSet.tileWidth,
                                height = tileSet.tileHeight,
                                rotation = 0F
                        )
                    }
                }
            }
        }
    }

    /**
     * When a [Tick] event is received, the [currentTimeMillis] is increased.
     *
     * @param event the [Tick] event
     */
    @Subscribe fun handleTick(event: Tick) {
        currentTimeMillis += event.elapsedMillis
    }

    /**
     * When a [Render] event is received, the following method calls occur:
     * [Canvas.lock], followed by [Canvas.clear], followed by
     * [Canvas.drawBitmap] for every tile, image and renderable object in the
     * game, followed by [Canvas.unlockAndPost].
     *
     * @param event the rendering request
     */
    @Subscribe fun handleRender(event: Render) {
        currentTimeMillis = System.currentTimeMillis()
        canvas.lock()
        val cameraWidth = canvas.width
        val cameraHeight = canvas.height
        val cameraX = event.cameraX - cameraWidth / 2
        val cameraY = event.cameraY - cameraHeight / 2
        val camera = Camera(cameraX, cameraY, cameraWidth, cameraHeight)
        canvas.clear()
        map.layers.filter { it.isVisible }.forEach { layer ->
            when (layer) {
                is Layer.ImageLayer -> drawImageLayer(camera, layer)
                is Layer.ObjectGroup -> drawObjectGroup(camera, layer)
                is Layer.TileLayer -> drawTileLayer(camera, layer)
            }
        }
        canvas.unlockAndPost()
    }
}
