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

package com.aheidelbacher.algostorm.graphics2d

import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber
import com.aheidelbacher.algostorm.state.Layer
import com.aheidelbacher.algostorm.state.Map
import com.aheidelbacher.algostorm.state.Map.RenderOrder
import com.aheidelbacher.algostorm.state.Object
import com.aheidelbacher.algostorm.state.TileSet.Tile.Companion.isFlippedDiagonally
import com.aheidelbacher.algostorm.state.TileSet.Tile.Companion.isFlippedHorizontally
import com.aheidelbacher.algostorm.state.TileSet.Tile.Companion.isFlippedVertically
import com.aheidelbacher.algostorm.state.TileSet.Viewport
import com.aheidelbacher.algostorm.time.Tick

import kotlin.comparisons.compareBy

/**
 * A system which handles the rendering of all objects in the game to the screen
 * canvas.
 *
 * Every method call to the [canvas] is made from the private engine thread.
 */
class RenderingSystem(
        private val map: Map,
        private val canvas: Canvas
) : Subscriber {
    companion object {
        private fun isVisible(
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

        private fun isVisible(camera: Camera, obj: Object): Boolean =
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

    private var currentTimeMillis = 0L

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
        canvas.drawBitmap(
                viewport = tileSet.getViewport(tileId),
                flipHorizontally = gid.isFlippedHorizontally,
                flipVertically = gid.isFlippedVertically,
                flipDiagonally = gid.isFlippedDiagonally,
                opacity = opacity,
                x = x,
                y = y,
                width = width,
                height = height,
                rotation = rotation
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
                flipHorizontally = false,
                flipVertically = false,
                flipDiagonally = false,
                opacity = imageLayer.opacity,
                x = 0,
                y = 0,
                width = camera.width,
                height = camera.height,
                rotation = 0F
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
                val gid = layer.data[y * map.width + x]
                val tileSet = map.getTileSet(gid) ?: error("Invalid gid $gid!")
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
        canvas.lock()
        canvas.clear()
        map.layers.filter { it.isVisible }.forEach { layer ->
            when (layer) {
                is Layer.ImageLayer -> drawImageLayer(event.camera, layer)
                is Layer.ObjectGroup -> drawObjectGroup(event.camera, layer)
                is Layer.TileLayer -> drawTileLayer(event.camera, layer)
            }
        }
        canvas.unlockAndPost()
    }
}
