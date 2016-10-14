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

package com.aheidelbacher.algostorm.systems.graphics2d

import com.aheidelbacher.algostorm.engine.graphics2d.Canvas
import com.aheidelbacher.algostorm.event.Event
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber
import com.aheidelbacher.algostorm.state.Color
import com.aheidelbacher.algostorm.state.Layer
import com.aheidelbacher.algostorm.state.MapObject
import com.aheidelbacher.algostorm.state.Object
import com.aheidelbacher.algostorm.state.TileSet.Tile.Companion.isFlippedDiagonally
import com.aheidelbacher.algostorm.state.TileSet.Tile.Companion.isFlippedHorizontally
import com.aheidelbacher.algostorm.state.TileSet.Tile.Companion.isFlippedVertically
import com.aheidelbacher.algostorm.state.TileSet.Tile.Frame
import com.aheidelbacher.algostorm.state.TileSet.Viewport
import com.aheidelbacher.algostorm.state.Layer.ObjectGroup
import com.aheidelbacher.algostorm.state.Layer.ObjectGroup.DrawOrder.INDEX
import com.aheidelbacher.algostorm.state.Layer.ObjectGroup.DrawOrder.TOP_DOWN
import com.aheidelbacher.algostorm.state.Layer.TileLayer
import com.aheidelbacher.algostorm.state.MapObject.RenderOrder.LEFT_DOWN
import com.aheidelbacher.algostorm.state.MapObject.RenderOrder.LEFT_UP
import com.aheidelbacher.algostorm.state.MapObject.RenderOrder.RIGHT_DOWN
import com.aheidelbacher.algostorm.state.MapObject.RenderOrder.RIGHT_UP
import com.aheidelbacher.algostorm.systems.Update
import com.aheidelbacher.algostorm.systems.geometry2d.Rectangle

import java.io.FileNotFoundException
import java.util.Comparator

/**
 * A system which handles the rendering of all objects in the game to the screen
 * canvas.
 *
 * Every method call to the [canvas] is made from the private engine thread.
 *
 * @property map the map object which should be rendered
 * @property canvas the canvas to which the system draws
 * @throws FileNotFoundException if any of the map tile set images doesn't exist
 */
class RenderingSystem @Throws(FileNotFoundException::class) constructor(
        private val map: MapObject,
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
        ): Boolean = gid != 0L &&
                camera.intersects(x, y - height + 1, width, height)

        @JvmStatic fun Object.isVisible(camera: Rectangle): Boolean =
                isVisible && camera.intersects(x, y - height + 1, width, height)

        @JvmStatic fun MapObject.getViewport(
                gid: Long,
                currentTimeMillis: Long
        ): Viewport {
            val tileSet = getTileSet(gid)
            val localTileId = getTileId(gid)
            val animation = tileSet.getTile(localTileId).animation
            val tileId = if (animation == null) {
                localTileId
            } else {
                var elapsedTimeMillis =
                        currentTimeMillis % animation.sumBy(Frame::duration)
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
        RIGHT_DOWN, RIGHT_UP -> 0 until map.width
        LEFT_DOWN, LEFT_UP -> map.width - 1 downTo 0
    }
    private val yRange = when (map.renderOrder) {
        RIGHT_DOWN, LEFT_DOWN -> 0 until map.height
        RIGHT_UP, LEFT_UP -> map.height - 1 downTo 0
    }
    private val comparator = Comparator<Object> { o1, o2 ->
        if (o1.y != o2.y) o1.y - o2.y
        else o1.id - o2.id
    }
    private val matrix = com.aheidelbacher.algostorm.engine.graphics2d.Matrix.identity()

    init {
        map.tileSets.forEach { canvas.loadBitmap(it.image.source.path) }
    }

    private var currentTimeMillis = 0L

    private fun drawGid(gid: Long, x: Int, y: Int, width: Int, height: Int) {
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
                    .postTranslate(width.toFloat(), height.toFloat())
        }.let {
            if (!gid.isFlippedHorizontally) it
            else it.postScale(-1F, 1F).postTranslate(width.toFloat(), 0F)
        }.let {
            if (!gid.isFlippedVertically) it
            else it.postScale(1F, -1F).postTranslate(0F, height.toFloat())
        }.postTranslate(
                dx = tileSet.tileOffsetX.toFloat() + x,
                dy = tileSet.tileOffsetY.toFloat() + y - height + 1
        )
        canvas.drawBitmap(
                image = viewport.image.source.path,
                x = viewport.x,
                y = viewport.y,
                width = viewport.width,
                height = viewport.height,
                matrix = matrix
        )
    }

    private fun Object.draw(color: Color?, offsetX: Int, offsetY: Int) {
        if (gid != 0L) {
            drawGid(gid, x + offsetX, y + offsetY, width, height)
        } else if (color != null) {
            matrix.reset()
            matrix.postTranslate(
                    dx = offsetX + x.toFloat(),
                    dy = offsetY + y.toFloat() - height + 1
            )
            canvas.drawRectangle(color.color, width, height, matrix)
        }
    }

    private fun TileLayer.draw(camera: Rectangle) {
        for (ty in yRange) {
            for (tx in xRange) {
                val x = tx * map.tileWidth
                val y = ty * map.tileHeight
                val gid = data[ty * map.width + tx]
                if (gid != 0L) {
                    val tileSet = map.getTileSet(gid)
                    if (isVisible(
                            camera = camera,
                            gid = gid,
                            x = x + offsetX,
                            y = y + offsetY,
                            width = tileSet.tileWidth,
                            height = tileSet.tileHeight
                    )) {
                        drawGid(
                                gid = gid,
                                x = x + offsetX - camera.x,
                                y = y + offsetY - camera.y,
                                width = tileSet.tileWidth,
                                height = tileSet.tileHeight
                        )
                    }
                }
            }
        }
    }

    private fun ObjectGroup.draw(camera: Rectangle) {
        when (drawOrder) {
            TOP_DOWN -> objectSet.filter {
                it.isVisible(camera)
            }.sortedWith(comparator)
            INDEX -> objectSet
        }.forEach {
            if (it.isVisible(camera)) {
                it.draw(color, offsetX - camera.x, offsetY - camera.y)
            }
        }
    }

    private fun Layer.draw(camera: Rectangle) {
        if (isVisible) {
            when (this) {
                is TileLayer -> draw(camera)
                is ObjectGroup -> draw(camera)
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
     * When a [Render] event is received, the canvas is cleared or filled with
     * the map background color and every renderable tile and object in the game
     * is drawn.
     *
     * @param event the rendering request
     */
    @Subscribe fun onRender(event: Render) {
        val cameraWidth = canvas.width
        val cameraHeight = canvas.height
        if (cameraWidth > 0 && cameraHeight > 0) {
            val cameraX = event.cameraX - cameraWidth / 2
            val cameraY = event.cameraY - cameraHeight / 2
            val camera = Rectangle(cameraX, cameraY, cameraWidth, cameraHeight)
            map.backgroundColor?.color?.let {
                canvas.drawColor(it)
            } ?: canvas.clear()
            map.layers.forEach { it.draw(camera) }
        }
    }
}
