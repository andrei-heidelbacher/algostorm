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

@file:JvmName("Util")

package com.aheidelbacher.algostorm.engine.tiled

import com.aheidelbacher.algostorm.engine.geometry2d.Point
import com.aheidelbacher.algostorm.engine.geometry2d.Rectangle
import com.aheidelbacher.algostorm.engine.geometry2d.boundingBox
import com.aheidelbacher.algostorm.engine.tiled.TileSet.Viewport

private fun Object.getRawShape(): List<Point> = when (this) {
    is Object.Tile -> listOf(
            Point(x, y),
            Point(x + width - 1, y),
            Point(x + width - 1, y - height + 1),
            Point(x, y - height + 1),
            Point(x, y)
    )
    is Object.Rectangle -> listOf(
            Point(x, y),
            Point(x + width - 1, y),
            Point(x + width - 1, y + height - 1),
            Point(x, y + height - 1),
            Point(x, y)
    )
    is Object.Ellipse -> listOf(
            Point(x, y),
            Point(x + width - 1, y),
            Point(x + width - 1, y + height - 1),
            Point(x, y + height - 1),
            Point(x, y)
    )
    is Object.Polygon -> arrayListOf<Point>().apply {
        polygon.mapTo(this) { it.translate(x, y) }
        add(Point(x, y))
    }
    is Object.Polyline -> polyline.map { it.translate(x, y) }
}

@Strictfp fun Object.getShape(): List<Point> {
    return if (rotation == 0F) getRawShape()
    else {
        val radians = Math.PI * rotation / 180.0
        val cos = Math.cos(radians)
        val sin = Math.sin(radians)
        getRawShape().map {
            val px = it.x - x
            val py = it.y - y
            Point(
                    x = (px * cos + py * sin + x + 0.5).toInt(),
                    y = (-px * sin + py * cos + y + 0.5).toInt()
            )
        }
    }
}

fun Object.getBoundingBox(): Rectangle = boundingBox(getShape())

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
