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

package algostorm.graphics2d

import algostorm.event.Subscribe
import algostorm.event.Subscriber
import algostorm.state.Layer
import algostorm.state.Map
import algostorm.state.TileSet.Tile.Companion.isFlippedDiagonally
import algostorm.state.TileSet.Tile.Companion.isFlippedHorizontally
import algostorm.state.TileSet.Tile.Companion.isFlippedVertically
import algostorm.state.TileSet.Viewport
import algostorm.time.Tick
import kotlin.comparisons.compareBy

/**
 * A system which handles the rendering of all objects in the game.
 */
abstract class AbstractRenderingSystem(protected val map: Map) : Subscriber {
    /**
     * This method should draw the viewport projected on the indicated bitmap.
     * The drawing coordinates are given relative to the screen.
     *
     * It will be called from the private engine thread and should be blocking
     * and thread-safe.
     *
     * @param viewport the viewport which should be rendered
     * @param flipHorizontally whether the image should be flipped horizontally
     * before rendering
     * @param flipVertically whether the image should be flipped vertically
     * before rendering
     * @param flipDiagonally whether the image should be flipped diagonally
     * before rendering
     * @param opacity the opacity of the image. Should be between `0` and `1`.
     * @param x the x-axis coordinate of the lower-left corner of the rendered
     * image in pixels
     * @param y the y-axis coordinate of the lower-left corner of the rendered
     * image in pixels
     * @param rotation the rotation of the image around the lower-left corner in
     * radians
     */
    protected abstract fun drawBitmap(
            viewport: Viewport,
            flipHorizontally: Boolean,
            flipVertically: Boolean,
            flipDiagonally: Boolean,
            opacity: Float,
            x: Int,
            y: Int,
            rotation: Float
    ): Unit

    /**
     * Clears the screen.
     */
    protected abstract fun clear(): Unit

    /**
     * Renders all the changes from the last [clear] or [render] call to the
     * screen.
     */
    protected abstract fun render(): Unit

    /**
     * The x-axis coordinate of the lower-left corner of the camera in pixels.
     */
    protected abstract val cameraX: Int

    /**
     * The y-axis coordinate of the lower-left corner of the camera in pixels.
     */
    protected abstract val cameraY: Int

    /**
     * The x-axis width of the camera in pixels.
     */
    protected abstract val cameraWidth: Int

    /**
     * The y-axis height of the camera in pixels.
     */
    protected abstract val cameraHeight: Int

    private var currentTimeMillis = 0L

    private fun drawGid(
            gid: Int,
            opacity: Float,
            x: Int,
            y: Int,
            rotation: Float
    ) {
        val tileSet = map.getTileSet(gid)
                ?: error("Invalid gid $gid!")
        val localTileId = map.getTileId(gid)
                ?: error("Invalid gid $gid!")
        val width = tileSet.tileWidth
        val height = tileSet.tileHeight
        val animation = tileSet.tiles[localTileId]?.animation
        val tileId = if (animation == null) {
            localTileId
        } else {
            var elapsedTimeMillis = currentTimeMillis %
                    animation.sumBy { it.duration }
            animation.dropWhile {
                elapsedTimeMillis -= it.duration
                elapsedTimeMillis >= 0
            }.first().tileId
        }
        if (x <= cameraWidth && x + width - 1 >= 0 &&
                y <= cameraHeight && y + height - 1 >= 0) {
            drawBitmap(
                    viewport = tileSet.getViewport(tileId),
                    flipHorizontally = gid.isFlippedHorizontally,
                    flipVertically = gid.isFlippedVertically,
                    flipDiagonally = gid.isFlippedDiagonally,
                    opacity = opacity,
                    x = x - cameraX,
                    y = y - cameraY,
                    rotation = rotation
            )
        }
    }

    private fun drawImageLayer(imageLayer: Layer.ImageLayer) {
        drawBitmap(
                viewport = Viewport(
                        image = imageLayer.image,
                        x = cameraX - imageLayer.offsetX,
                        y = cameraY - imageLayer.offsetY,
                        width = cameraWidth,
                        height = cameraHeight
                ),
                flipHorizontally = false,
                flipVertically = false,
                flipDiagonally = false,
                opacity = imageLayer.opacity,
                x = 0,
                y = 0,
                rotation = 0F
        )
    }

    private fun drawObjectGroup(objectGroup: Layer.ObjectGroup) {
        objectGroup.objects.filter {
            it.isVisible && it.gid != 0
        }.sortedWith(compareBy({ -it.y }, { it.x })).forEach {
            drawGid(
                    gid = it.gid,
                    opacity = objectGroup.opacity,
                    x = it.x + objectGroup.offsetX - cameraX,
                    y = it.y + objectGroup.offsetY - cameraY,
                    rotation = it.rotation
            )
        }
    }

    private fun drawTileLayer(tileLayer: Layer.TileLayer) {
        for (y in map.height - 1 downTo 0) {
            for (x in 0 until map.width) {
                drawGid(
                        gid = tileLayer.data[x * map.height + y],
                        opacity = tileLayer.opacity,
                        x = x * map.tileWidth + tileLayer.offsetX - cameraX,
                        y = y * map.tileHeight + tileLayer.offsetY - cameraY,
                        rotation = 0F
                )
            }
        }
    }

    /**
     * When a [Tick] event is received, the [currentTimeMillis] is increased.
     */
    @Subscribe fun handleTick(event: Tick) {
        currentTimeMillis += event.elapsedMillis
    }

    /**
     * When a [Render] event is received, the [clear] method is called, followed
     * by [drawBitmap] for every tile, image and renderable object in the
     * game. After all the rendering is done, the [render] method is called to
     * render the changes to the screen.
     *
     * @param event the rendering request
     */
    @Subscribe fun handleRender(event: Render) {
        clear()
        map.layers.filter { it.isVisible }.forEach { layer ->
            when (layer) {
                is Layer.ImageLayer -> drawImageLayer(layer)
                is Layer.ObjectGroup -> drawObjectGroup(layer)
                is Layer.TileLayer -> drawTileLayer(layer)
            }
        }
        render()
    }
}
