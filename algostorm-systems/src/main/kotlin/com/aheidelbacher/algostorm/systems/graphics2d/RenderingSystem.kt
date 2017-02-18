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

package com.aheidelbacher.algostorm.systems.graphics2d

import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.Canvas
import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.Color
import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.TileSet.Companion.isFlippedDiagonally
import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.TileSet.Companion.isFlippedHorizontally
import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.TileSet.Companion.isFlippedVertically
import com.aheidelbacher.algostorm.core.ecs.EntityGroup
import com.aheidelbacher.algostorm.core.ecs.EntityRef.Id
import com.aheidelbacher.algostorm.core.event.Event
import com.aheidelbacher.algostorm.core.event.Publisher
import com.aheidelbacher.algostorm.core.event.Subscribe
import com.aheidelbacher.algostorm.core.event.Subscriber
import com.aheidelbacher.algostorm.systems.physics2d.Position
import com.aheidelbacher.algostorm.systems.physics2d.position

/**
 * A system which handles the rendering of all tiles and entities in the game to
 * the screen canvas.
 *
 * Entities are rendered ascending by `z`, then ascending by `y`, then ascending
 * by `priority`, then ascending by `x`.
 *
 * @property map the map object which should be rendered
 * @property canvas the canvas to which the system draws
 */
class RenderingSystem(
        private val tileWidth: Int,
        private val tileHeight: Int,
        private val background: Color,
        private val tileSetCollection: TileSetCollection,
        private val canvas: Canvas,
        private val entityGroup: EntityGroup
) : Subscriber {
    private data class Node(
            val id: Id,
            var position: Position,
            var sprite: Sprite
    ) : Comparable<Node> {
        override fun compareTo(other: Node): Int =
                if (sprite.z != other.sprite.z)
                    sprite.z - other.sprite.z
                else if (position.y != other.position.y)
                    position.y - other.position.y
                else if (sprite.priority != other.sprite.priority)
                    sprite.priority - other.sprite.priority
                else
                    position.x - other.position.x
    }

    private lateinit var renderableGroup: EntityGroup
    private var sortedEntities = emptyArray<Node>()

    override fun onSubscribe(publisher: Publisher) {
        renderableGroup = entityGroup.addGroup {
            it.sprite != null && it.position != null
        }
    }

    override fun onUnsubscribe(publisher: Publisher) {
        entityGroup.removeGroup(renderableGroup)
    }

    private fun updateSortedOrder() {
        val size = renderableGroup.entities.count()
        val isChanged = size != sortedEntities.size || sortedEntities.any {
            it.id !in renderableGroup
        }
        if (isChanged) {
            val entities = arrayOfNulls<Node?>(size)
            renderableGroup.entities.forEachIndexed { i, entityRef ->
                entities[i] = Node(
                        id = entityRef.id,
                        position = checkNotNull(entityRef.position),
                        sprite = checkNotNull(entityRef.sprite)
                )
            }
            sortedEntities = entities.requireNoNulls()
        } else {
            sortedEntities.forEach {
                val entity = checkNotNull(renderableGroup[it.id])
                it.position = checkNotNull(entity.position)
                it.sprite = checkNotNull(entity.sprite)
            }
        }
        val isSorted = (0 until sortedEntities.size - 1).none {
            sortedEntities[it] > sortedEntities[it + 1]
        }
        if (!isSorted) {
            sortedEntities.sort()
        }
    }

    /**
     * An event which requests the rendering of the entire game state to the
     * screen.
     *
     * @property cameraX the horizontal coordinate of the center of the camera
     * in pixels
     * @property cameraY the vertical coordinate of the center of the camera in
     * pixels (positive is down)
     */
    data class Render(val cameraX: Int, val cameraY: Int) : Event

    private fun drawGid(gid: Int, x: Int, y: Int, width: Int, height: Int) {
        if (gid == 0) {
            return
        }
        val viewport = tileSetCollection.getViewport(gid)
        with(canvas) {
            save()
            if (gid.isFlippedDiagonally) {
                rotate(90F)
                scale(1F, -1F)
                translate(1F * width, 1F * height)
            }
            if (gid.isFlippedHorizontally) {
                scale(-1F, 1F)
                translate(1F * width, 0F)
            }
            if (gid.isFlippedVertically) {
                scale(1F, -1F)
                translate(0F, 1F * height)
            }
            drawImage(
                    resource = viewport.image.resource,
                    sx = viewport.x,
                    sy = viewport.y,
                    sw = viewport.width,
                    sh = viewport.height,
                    dx = x,
                    dy = y,
                    dw = width,
                    dh = height
            )
            restore()
        }
    }

    private fun Sprite.draw(offX: Int, offY: Int) {
        if (isVisible && gid != 0) {
            drawGid(gid, offX + offsetX, offY + offsetY, width, height)
        }
    }

    private fun Node.draw(offX: Int, offY: Int) {
        sprite.draw(
                offX = offX + position.x * tileWidth,
                offY = offY + position.y * tileHeight
        )
    }

    /**
     * When a [Render] event is received, the canvas is filled with the
     * background color and every renderable tile and entity in the game is
     * drawn.
     *
     * @param event the rendering event
     */
    @Subscribe fun onRender(event: Render) {
        val cameraWidth = canvas.width
        val cameraHeight = canvas.height
        val cameraX = event.cameraX - canvas.width / 2
        val cameraY = event.cameraY - canvas.height / 2
        if (cameraWidth > 0 && cameraHeight > 0) {
            canvas.drawColor(background)
            updateSortedOrder()
            sortedEntities.forEach { it.draw(-cameraX, -cameraY) }
        }
    }
}
