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
    private var currentTimeMillis = 0L

    private fun drawGid(
            canvasX: Int,
            canvasY: Int,
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
        if (x <= canvas.width && x + width - 1 >= 0 &&
                y <= canvas.height && y + height - 1 >= 0) {
            canvas.drawBitmap(
                    viewport = tileSet.getViewport(tileId),
                    flipHorizontally = gid.isFlippedHorizontally,
                    flipVertically = gid.isFlippedVertically,
                    flipDiagonally = gid.isFlippedDiagonally,
                    opacity = opacity,
                    x = x - canvasX,
                    y = y - canvasY,
                    width = width,
                    height = height,
                    rotation = rotation
            )
        }
    }

    private fun drawImageLayer(
            canvasX: Int,
            canvasY: Int,
            imageLayer: Layer.ImageLayer
    ) {
        canvas.drawBitmap(
                viewport = Viewport(
                        image = imageLayer.image,
                        x = canvasX - imageLayer.offsetX,
                        y = canvasY - imageLayer.offsetY,
                        width = canvas.width,
                        height = canvas.height
                ),
                flipHorizontally = false,
                flipVertically = false,
                flipDiagonally = false,
                opacity = imageLayer.opacity,
                x = 0,
                y = 0,
                width = canvas.width,
                height = canvas.height,
                rotation = 0F
        )
    }

    private fun drawObjectGroup(
            canvasX: Int,
            canvasY: Int,
            objectGroup: Layer.ObjectGroup
    ) {
        val comparator = when (map.renderOrder) {
            RenderOrder.RIGHT_DOWN -> compareBy<Object>({ it.y }, { it.x })
            RenderOrder.RIGHT_UP -> compareBy<Object>({ -it.y }, { it.x })
            RenderOrder.LEFT_DOWN -> compareBy<Object>({ it.y }, { -it.x })
            RenderOrder.LEFT_UP -> compareBy<Object>({ -it.y }, { -it.x })
        }
        objectGroup.objects.filter {
            it.isVisible && it.gid != 0
        }.sortedWith(comparator).forEach {
            drawGid(
                    canvasX = canvasX,
                    canvasY = canvasY,
                    gid = it.gid,
                    opacity = objectGroup.opacity,
                    x = it.x + objectGroup.offsetX - canvasX,
                    y = it.y + objectGroup.offsetY - canvasY,
                    width = it.width,
                    height = it.height,
                    rotation = it.rotation
            )
        }
    }

    private fun drawTileLayer(
            canvasX: Int,
            canvasY: Int,
            tileLayer: Layer.TileLayer
    ) {
        val (yRange, xRange) = when (map.renderOrder) {
            RenderOrder.RIGHT_DOWN ->
                Pair(0 until map.height, 0 until map.width)
            RenderOrder.RIGHT_UP ->
                Pair(map.height - 1 downTo 0, 0 until map.width)
            RenderOrder.LEFT_DOWN ->
                Pair(0 until map.height, map.width - 1 downTo 0)
            RenderOrder.LEFT_UP ->
                Pair(map.height - 1 downTo 0, map.width - 1 downTo 0)
        }
        for (y in yRange) {
            for (x in xRange) {
                val gid = tileLayer.data[y * map.width + x]
                if (gid == 0) {
                    continue
                }
                val tileSet = map.getTileSet(gid) ?: error("Invalid gid $gid!")
                drawGid(
                        canvasX = canvasX,
                        canvasY = canvasY,
                        gid = gid,
                        opacity = tileLayer.opacity,
                        x = x * map.tileWidth + tileLayer.offsetX - canvasX,
                        y = y * map.tileHeight + tileLayer.offsetY - canvasY,
                        width = tileSet.tileWidth,
                        height = tileSet.tileHeight,
                        rotation = 0F
                )
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
     * [Canvas.lock], followed by [Canvas.width], [Canvas.height] and
     * [Canvas.clear], followed by [Canvas.drawBitmap] for every tile, image and
     * renderable object in the game, followed by [Canvas.unlockAndPost].
     *
     * @param event the rendering request
     */
    @Subscribe fun handleRender(event: Render) {
        canvas.lock()
        val canvasX = event.cameraX - canvas.width / 2
        val canvasY = event.cameraY - canvas.height / 2
        canvas.clear()
        map.layers.filter { it.isVisible }.forEach { layer ->
            when (layer) {
                is Layer.ImageLayer -> drawImageLayer(canvasX, canvasY, layer)
                is Layer.ObjectGroup -> drawObjectGroup(canvasX, canvasY, layer)
                is Layer.TileLayer -> drawTileLayer(canvasX, canvasY, layer)
            }
        }
        canvas.unlockAndPost()
    }
}
