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

import com.aheidelbacher.algostorm.ecs.EntityGroup
import com.aheidelbacher.algostorm.ecs.EntityRef
import com.aheidelbacher.algostorm.engine.driver.Resource
import com.aheidelbacher.algostorm.engine.graphics2d.Canvas
import com.aheidelbacher.algostorm.engine.graphics2d.Color
import com.aheidelbacher.algostorm.engine.graphics2d.Matrix
import com.aheidelbacher.algostorm.event.Event
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber
import com.aheidelbacher.algostorm.data.MapObject
import com.aheidelbacher.algostorm.data.TileSetCollection
import com.aheidelbacher.algostorm.data.TileSet.Tile.Companion.isFlippedDiagonally
import com.aheidelbacher.algostorm.data.TileSet.Tile.Companion.isFlippedHorizontally
import com.aheidelbacher.algostorm.data.TileSet.Tile.Companion.isFlippedVertically
import com.aheidelbacher.algostorm.event.Publisher
import com.aheidelbacher.algostorm.systems.state.Layer
//import com.aheidelbacher.algostorm.systems.state.Layer.EntityGroup
import com.aheidelbacher.algostorm.systems.state.Layer.TileLayer
//import com.aheidelbacher.algostorm.systems.state.MapObject
import com.aheidelbacher.algostorm.systems.state.MapObject.RenderOrder.LEFT_DOWN
import com.aheidelbacher.algostorm.systems.state.MapObject.RenderOrder.LEFT_UP
import com.aheidelbacher.algostorm.systems.state.MapObject.RenderOrder.RIGHT_DOWN
import com.aheidelbacher.algostorm.systems.state.MapObject.RenderOrder.RIGHT_UP
//import com.aheidelbacher.algostorm.systems.state.TileSet.Tile.Companion.isFlippedDiagonally
//import com.aheidelbacher.algostorm.systems.state.TileSet.Tile.Companion.isFlippedHorizontally
//import com.aheidelbacher.algostorm.systems.state.TileSet.Tile.Companion.isFlippedVertically
import com.aheidelbacher.algostorm.systems.Update
import com.aheidelbacher.algostorm.systems.graphics2d.getViewport
import com.aheidelbacher.algostorm.systems.graphics2d.sprite
import com.aheidelbacher.algostorm.systems.physics2d.position

import java.util.Comparator

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
        private val map: MapObject,
        private val canvas: Canvas
) : Subscriber {
    companion object {
        const val RENDERABLE_GROUP_NAME: String = "renderable"
    }

    private val tileWidth: Int = map.tileWidth
    private val tileHeight: Int = map.tileHeight
    private val tileSetCollection: TileSetCollection = map.tileSetCollection
    private val entityGroup: EntityGroup = map.entityPool.group
    private lateinit var renderableGroup: EntityGroup
    private var sortedIds = Array(0) { 0 }
    private val comparator = Comparator<Int> { id1, id2 ->
        val e1 = renderableGroup[id1] ?: error("")
        val e2 = renderableGroup[id2] ?: error("")
        val p1 = e1.position ?: error("")
        val p2 = e2.position ?: error("")
        val s1 = e1.sprite ?: error("")
        val s2 = e2.sprite ?: error("")
        if (s1.z != s2.z) s1.z - s2.z
        else if (p1.y != p2.y) p1.y - p2.y
        else if (s1.priority != s2.priority) s1.priority - s2.priority
        else p1.x - p2.x
    }

    override fun onSubscribe(publisher: Publisher) {
        renderableGroup = entityGroup.addGroup(RENDERABLE_GROUP_NAME) {
            it.sprite != null && it.position != null
        }
    }

    override fun onUnsubscribe(publisher: Publisher) {
        entityGroup.removeGroup(RENDERABLE_GROUP_NAME)
    }

    private fun updateSortedOrder() {
        val size = renderableGroup.entities.count()
        val isChanged = size != sortedIds.size || sortedIds.any {
            it !in renderableGroup
        }
        if (isChanged) {
            sortedIds = Array(size) { 0 }
            renderableGroup.entities.forEachIndexed { i, entityRef ->
                sortedIds[i] = entityRef.id
            }
        }
        val isSorted = (0 until sortedIds.size - 1).none {
            comparator.compare(sortedIds[it], sortedIds[it + 1]) > 0
        }
        if (!isSorted) {
            sortedIds.sortWith(comparator)
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

    /*private val xRange = when (map.renderOrder) {
        RIGHT_DOWN, RIGHT_UP -> 0 until map.width
        LEFT_DOWN, LEFT_UP -> map.width - 1 downTo 0
    }

    private val yRange = when (map.renderOrder) {
        RIGHT_DOWN, LEFT_DOWN -> 0 until map.height
        RIGHT_UP, LEFT_UP -> map.height - 1 downTo 0
    }*/

    private val matrix = Matrix.identity()

    init {
        map.tileSetCollection.tileSets.forEach {
            canvas.loadBitmap(it.image.resource)
            //canvas.loadBitmap(it.image.source.path)
            //canvas.loadBitmap(Resource("res:///" + it.image.source.path))
        }
    }

    private var currentTimeMillis = 0L

    private fun drawGid(gid: Int, x: Int, y: Int, width: Int, height: Int) {
        if (gid == 0) {
            return
        }
        val viewport = tileSetCollection.getViewport(gid, currentTimeMillis)
        matrix.reset()
        matrix.postScale(
                sx = 1F * width / viewport.width,
                sy = 1F * height / viewport.height
        ).let {
            if (!gid.isFlippedDiagonally) it
            else it.postRotate(90F)
                    .postScale(1F, -1F)
                    .postTranslate(1F * width, 1F * height)
        }.let {
            if (!gid.isFlippedHorizontally) it
            else it.postScale(-1F, 1F).postTranslate(1F * width, 0F)
        }.let {
            if (!gid.isFlippedVertically) it
            else it.postScale(1F, -1F).postTranslate(0F, 1F * height)
        }.postTranslate(1F * x, 1F * y)
        canvas.drawBitmap(
                resource = viewport.image.resource,
                x = viewport.x,
                y = viewport.y,
                width = viewport.width,
                height = viewport.height,
                matrix = matrix
        )
    }

    /*private fun TileLayer.draw(offX: Int, offY: Int) {
        for (ty in yRange) {
            for (tx in xRange) {
                val gid = get(ty * map.width + tx)
                if (gid != 0) {
                    val x = tx * map.tileWidth
                    val y = ty * map.tileHeight
                    val tileSet = map.getTileSet(gid)
                    val tw = tileSet.tileWidth
                    val th = tileSet.tileHeight
                    drawGid(gid, x + offX + offsetX, y + offY + offsetY, tw, th)
                }
            }
        }
    }*/

    private fun Sprite.draw(offX: Int, offY: Int) {
        if (isVisible && gid != 0) {
            drawGid(gid, offX + offsetX, offY + offsetY, width, height)
        } else if (isVisible && color != null) {
            matrix.reset()
            matrix.postTranslate(1F * offX + offsetX, 1F * offY + offsetY)
            canvas.drawRectangle(Color(color.color), width, height, matrix)
        }
    }

    private fun EntityRef.draw(offX: Int, offY: Int) {
        val p = position ?: return
        val x = p.x * tileWidth
        val y = p.y * tileHeight
        sprite?.draw(offX + x, offY + y)
    }

    /*private fun EntityGroup.draw(offX: Int, offY: Int) {
        val size = entities.count { it.position != null && it.sprite != null }
        val entityArray = arrayOfNulls<EntityRef>(size)
        var i = 0
        entities.forEach {
            if (it.position != null && it.sprite != null) {
                entityArray[i++] = it
            }
        }
        entityArray.requireNoNulls().apply {
            sortWith(comparator)
        }.forEach {
            it.draw(offX, offY)
        }
    }*/

    /*private fun Layer.draw(offX: Int, offY: Int) {
        if (isVisible) {
            when (this) {
                is EntityGroup -> draw(offX + offsetX, offY + offsetY)
                is TileLayer -> draw(offX + offsetX, offY + offsetY)
            }
        }
    }*/

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
     * the map background color and every renderable tile and entity in the game
     * is drawn.
     *
     * @param event the rendering request
     */
    @Subscribe fun onRender(event: Render) {
        val cameraWidth = canvas.width
        val cameraHeight = canvas.height
        val cameraX = event.cameraX - canvas.width / 2
        val cameraY = event.cameraY - canvas.height / 2
        if (cameraWidth > 0 && cameraHeight > 0) {
            map.backgroundColor?.color?.let {
                canvas.drawColor(Color(it))
            } ?: canvas.clear()
            updateSortedOrder()
            sortedIds.forEach { renderableGroup[it]?.draw(-cameraX, -cameraY) }
            //map.layers.forEach { it.draw(-cameraX, -cameraY) }
            //map.entityPool.group.draw(-cameraX, -cameraY)
        }
    }
}
