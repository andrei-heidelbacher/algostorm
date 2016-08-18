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

import com.aheidelbacher.algostorm.engine.Update
import com.aheidelbacher.algostorm.engine.geometry2d.Rectangle
import com.aheidelbacher.algostorm.engine.state.Layer
import com.aheidelbacher.algostorm.engine.state.Map
import com.aheidelbacher.algostorm.engine.state.Map.RenderOrder
import com.aheidelbacher.algostorm.engine.state.Object
import com.aheidelbacher.algostorm.engine.state.TileSet.Tile.Companion.isFlippedDiagonally
import com.aheidelbacher.algostorm.engine.state.TileSet.Tile.Companion.isFlippedHorizontally
import com.aheidelbacher.algostorm.engine.state.TileSet.Tile.Companion.isFlippedVertically
import com.aheidelbacher.algostorm.engine.state.TileSet.Viewport
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber

import java.util.Comparator

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
        fun isVisible(
                camera: Rectangle,
                gid: Long,
                x: Int,
                y: Int,
                width: Int,
                height: Int,
                rotation: Float
        ): Boolean = gid != 0L && camera.intersects(x, y, width, height)

        fun isVisible(camera: Rectangle, obj: Object): Boolean =
                obj.visible && obj.gid != 0L && camera.intersects(
                        x = obj.x,
                        y = obj.y,
                        width = obj.width,
                        height = obj.height
                )
    }

    private val xRange = when (map.renderOrder) {
        RenderOrder.RIGHT_DOWN, RenderOrder.RIGHT_UP -> 0 until map.width
        RenderOrder.LEFT_DOWN, RenderOrder.LEFT_UP -> map.width - 1 downTo 0
    }
    private val yRange = when (map.renderOrder) {
        RenderOrder.RIGHT_DOWN, RenderOrder.LEFT_DOWN -> 0 until map.height
        RenderOrder.RIGHT_UP, RenderOrder.LEFT_UP -> map.height - 1 downTo 0
    }
    private val comparator: Comparator<Object> = when (map.renderOrder) {
        RenderOrder.RIGHT_DOWN -> Comparator<Object> { o1, o2 ->
            if (o1.y != o2.y) o1.y - o2.y
            else o1.x - o2.x
        }
        RenderOrder.RIGHT_UP -> Comparator<Object> { o1, o2 ->
            if (o1.y != o2.y) o2.y - o1.y
            else o1.x - o2.x
        }
        RenderOrder.LEFT_DOWN -> Comparator<Object> { o1, o2 ->
            if (o1.y != o2.y) o1.y - o2.y
            else o2.x - o1.x
        }
        RenderOrder.LEFT_UP -> Comparator<Object> { o1, o2 ->
            if (o1.y != o2.y) o2.y - o1.y
            else o2.x - o1.x
        }
    }
    private val matrix = Matrix.identity()

    init {
        map.tileSets.forEach { canvas.loadBitmap(it.image) }
    }

    private var currentTimeMillis: Long = 0

    private fun drawGid(
            gid: Long,
            opacity: Float,
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            rotation: Float
    ) {
        val tileSet = map.getTileSet(gid)
        val localTileId = map.getTileId(gid)
        val animation = tileSet.getTile(localTileId).animation
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
        matrix.reset()
        matrix.postScale(
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
        }.postRotate(rotation, 0F, height.toFloat()).postTranslate(
                dx = tileSet.tileOffset.x.toFloat() + x,
                dy = tileSet.tileOffset.y.toFloat() + y
        )
        canvas.drawBitmap(
                viewport = viewport,
                matrix = matrix,
                opacity = opacity
        )
    }

    private fun drawImageLayer(camera: Rectangle, layer: Layer.ImageLayer) {
        matrix.reset()
        canvas.drawBitmap(
                viewport = Viewport(
                        image = layer.image,
                        x = camera.x - layer.offsetX,
                        y = camera.y - layer.offsetY,
                        width = camera.width,
                        height = camera.height
                ),
                matrix = matrix,
                opacity = layer.opacity
        )
    }

    private fun drawObjectGroup(camera: Rectangle, layer: Layer.ObjectGroup) {
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

    private fun drawTileLayer(camera: Rectangle, layer: Layer.TileLayer) {
        for (ty in yRange) {
            for (tx in xRange) {
                val x = tx * map.tileWidth
                val y = ty * map.tileHeight
                val gid = layer.data[ty * map.width + tx]
                if (gid != 0L) {
                    val tileSet = map.getTileSet(gid)
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
     * When an [Update] event is received, the [currentTimeMillis] is increased.
     *
     * @param event the [Update] event
     */
    @Subscribe fun onUpdate(event: Update) {
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
    @Subscribe fun onRender(event: Render) {
        canvas.lock()
        val cameraWidth = canvas.width
        val cameraHeight = canvas.height
        if (cameraWidth > 0 && cameraHeight > 0) {
            val cameraX = event.cameraX - cameraWidth / 2
            val cameraY = event.cameraY - cameraHeight / 2
            val camera = Rectangle(cameraX, cameraY, cameraWidth, cameraHeight)
            canvas.clear()
            map.layers.filter { it.visible }.forEach { layer ->
                when (layer) {
                    is Layer.ImageLayer -> drawImageLayer(camera, layer)
                    is Layer.ObjectGroup -> drawObjectGroup(camera, layer)
                    is Layer.TileLayer -> drawTileLayer(camera, layer)
                }
            }
        }
        canvas.unlockAndPost()
    }
}
