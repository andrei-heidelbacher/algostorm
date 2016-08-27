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
import com.aheidelbacher.algostorm.engine.state.Layer.ObjectGroup.DrawOrder
import com.aheidelbacher.algostorm.engine.state.Map
import com.aheidelbacher.algostorm.engine.state.Map.RenderOrder
import com.aheidelbacher.algostorm.engine.state.Object
import com.aheidelbacher.algostorm.engine.state.TileSet.Tile.Companion.isFlippedDiagonally
import com.aheidelbacher.algostorm.engine.state.TileSet.Tile.Companion.isFlippedHorizontally
import com.aheidelbacher.algostorm.engine.state.TileSet.Tile.Companion.isFlippedVertically
import com.aheidelbacher.algostorm.engine.state.TileSet.Viewport
import com.aheidelbacher.algostorm.event.Event
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber

import java.io.FileNotFoundException
import java.util.Comparator

/**
 * A system which handles the rendering of all objects in the game to the screen
 * canvas.
 *
 * Every method call to the [canvas] is made from the private engine thread.
 *
 * @property map the map which should be rendered
 * @property canvas the canvas to which the system draws
 * @throws FileNotFoundException if any of the map tile set images doesn't exist
 */
class RenderingSystem @Throws(FileNotFoundException::class) constructor(
        private val map: Map,
        private val canvas: Canvas
) : Subscriber {
    companion object {
        @JvmStatic fun isVisible(
                camera: Rectangle,
                gid: Long,
                x: Int,
                y: Int,
                width: Int,
                height: Int
        ): Boolean = gid != 0L && camera.intersects(x, y, width, height)

        @JvmStatic fun isVisible(
                camera: Rectangle,
                obj: Object,
                color: Int?
        ): Boolean = obj.visible && (obj.gid != 0L || color != null) &&
                camera.intersects(obj.x, obj.y, obj.width, obj.height)

        fun Map.getViewport(gid: Long, currentTimeMillis: Long): Viewport {
            val tileSet = getTileSet(gid)
            val localTileId = getTileId(gid)
            val animation = tileSet.getTile(localTileId).animation
            val tileId = if (animation == null) {
                localTileId
            } else {
                var elapsedTimeMillis = currentTimeMillis % animation.sumBy {
                    it.duration
                }
                var i = 0
                do {
                    elapsedTimeMillis -= animation[i].duration
                    ++i
                } while (elapsedTimeMillis >= 0)
                animation[i - 1].tileId
            }
            return tileSet.getViewport(tileId)
        }
    }

    /**
     * An event which requests the rendering of the entire game state to the
     * screen.
     *
     * @property cameraX the x-axis coordinate of the center of the camera in
     * pixels
     * @property cameraY the y-axis coordinate of the center of the camera in
     * pixels
     */
    data class Render(val cameraX: Int, val cameraY: Int) : Event

    private val xRange = when (map.renderOrder) {
        RenderOrder.RIGHT_DOWN, RenderOrder.RIGHT_UP -> 0 until map.width
        RenderOrder.LEFT_DOWN, RenderOrder.LEFT_UP -> map.width - 1 downTo 0
    }
    private val yRange = when (map.renderOrder) {
        RenderOrder.RIGHT_DOWN, RenderOrder.LEFT_DOWN -> 0 until map.height
        RenderOrder.RIGHT_UP, RenderOrder.LEFT_UP -> map.height - 1 downTo 0
    }
    private val comparator = Comparator<Object> { o1, o2 ->
        if (o1.y != o2.y) o1.y - o2.y
        else o1.id - o2.id
    }
    private val matrix = Matrix.identity()

    init {
        map.tileSets.forEach { canvas.loadBitmap(it.image) }
    }

    private var currentTimeMillis = 0L

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
        val viewport = map.getViewport(gid, currentTimeMillis)
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
        }.postRotate(rotation).postTranslate(
                dx = tileSet.tileOffset.x.toFloat() + x,
                dy = tileSet.tileOffset.y.toFloat() + y
        )
        canvas.drawBitmap(
                image = viewport.image,
                x = viewport.x,
                y = viewport.y,
                width = viewport.width,
                height = viewport.height,
                matrix = matrix,
                opacity = opacity
        )
    }

    private fun drawRectangle(
            color: Int,
            opacity: Float,
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            rotation: Float
    ) {
        matrix.reset()
        matrix.postRotate(rotation).postTranslate(x.toFloat(), y.toFloat())
        canvas.drawRectangle(color, width, height, matrix, opacity)
    }

    private fun drawImageLayer(camera: Rectangle, layer: Layer.ImageLayer) {
        matrix.reset()
        canvas.drawBitmap(
                image = layer.image,
                x = camera.x - layer.offsetX,
                y = camera.y - layer.offsetY,
                width = camera.width,
                height = camera.height,
                matrix = matrix,
                opacity = layer.opacity
        )
    }

    private fun drawObjectGroup(camera: Rectangle, layer: Layer.ObjectGroup) {
        val color = layer.color?.let { Color.fromHtmlARGB8888(it) }
        layer.objects.let { objects ->
            when (layer.drawOrder) {
                DrawOrder.TOP_DOWN ->
                    objects.filter { obj ->
                        isVisible(camera, obj, color)
                    }.sortedWith(comparator)
                DrawOrder.INDEX -> objects
            }
        }.forEach { obj ->
            if (isVisible(camera, obj, color)) {
                if (obj.gid != 0L) {
                    drawGid(
                            gid = obj.gid,
                            opacity = layer.opacity,
                            x = obj.x + layer.offsetX - camera.x,
                            y = obj.y + layer.offsetY - camera.y,
                            width = obj.width,
                            height = obj.height,
                            rotation = obj.rotation
                    )
                } else if (color != null) {
                    drawRectangle(
                            color = color,
                            opacity = layer.opacity,
                            x = obj.x + layer.offsetX - camera.x,
                            y = obj.y + layer.offsetY - camera.y,
                            width = obj.width,
                            height = obj.height,
                            rotation = obj.rotation
                    )
                }
            }
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
                            height = tileSet.tileHeight
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
            map.backgroundColor?.let {
                canvas.drawColor(Color.fromHtmlARGB8888(it))
            }
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
