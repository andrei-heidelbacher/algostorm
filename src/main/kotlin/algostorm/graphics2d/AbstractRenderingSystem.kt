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
import algostorm.state.Map.RenderOrder
import algostorm.state.TileSet.Tile.Companion.isFlippedDiagonally
import algostorm.state.TileSet.Tile.Companion.isFlippedHorizontally
import algostorm.state.TileSet.Tile.Companion.isFlippedVertically
import algostorm.state.TileSet.Viewport

/**
 * A system which handles the rendering of all objects in the game.
 */
abstract class AbstractRenderingSystem(protected val map: Map) : Subscriber {
    /**
     * This method should render the viewport projected on the indicated bitmap.
     *
     * It will be called from the private engine thread and should be blocking
     * and thread-safe.
     */
    protected abstract fun renderBitmap(
            viewport: Viewport,
            flipHorizontally: Boolean,
            flipVertically: Boolean,
            flipDiagonally: Boolean,
            opacity: Float,
            x: Int,
            y: Int,
            rotation: Float
    ): Unit

    protected abstract val cameraX: Int

    protected abstract val cameraY: Int

    protected abstract val cameraWidth: Int

    protected abstract val cameraHeight: Int

    private fun renderGid(
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
            var elapsedTimeMillis = System.currentTimeMillis() %
                    animation.sumBy { it.duration }
            animation.dropWhile {
                elapsedTimeMillis -= it.duration
                elapsedTimeMillis >= 0
            }.first().tileId
        }
        if (x <= cameraWidth && x + width - 1 >= 0 &&
                y <= cameraHeight && y + height - 1 >= 0) {
            renderBitmap(
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

    private fun renderImageLayer(imageLayer: Layer.ImageLayer) {
        renderBitmap(
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

    private fun renderObjectGroup(objectGroup: Layer.ObjectGroup) {
        objectGroup.objects.filter { it.isVisible && it.gid != 0 }.forEach {
            renderGid(
                    gid = it.gid,
                    opacity = objectGroup.opacity,
                    x = it.x + objectGroup.offsetX - cameraX,
                    y = it.y + objectGroup.offsetY - cameraY,
                    rotation = it.rotation
            )
        }
    }

    private fun renderTileLayer(tileLayer: Layer.TileLayer) {
        val yRange = when (map.renderOrder) {
            RenderOrder.LEFT_DOWN, RenderOrder.RIGHT_DOWN -> {
                map.height - 1 downTo 0
            }
            RenderOrder.LEFT_UP, RenderOrder.RIGHT_UP -> 0 until map.height
        }
        val xRange = when (map.renderOrder) {
            RenderOrder.LEFT_DOWN, RenderOrder.LEFT_UP -> map.width - 1 downTo 0
            RenderOrder.RIGHT_DOWN, RenderOrder.RIGHT_UP -> 0 until map.width
        }
        yRange.forEach { y ->
            xRange.forEach { x ->
                renderGid(
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
     * When a [Render] event is received, the [renderBitmap] method is called
     * for every tile, image and renderable object in the game.
     *
     * @param event the rendering request
     */
    @Subscribe fun handleRender(event: Render) {
        map.layers.filter { it.isVisible }.forEach { layer ->
            when (layer) {
                is Layer.ImageLayer -> renderImageLayer(layer)
                is Layer.ObjectGroup -> renderObjectGroup(layer)
                is Layer.TileLayer -> renderTileLayer(layer)
            }
        }
    }
}
