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

package com.aheidelbacher.algostorm.engine.geometry2d

fun squareDistance(x0: Int, y0: Int, x1: Int, y1: Int): Long =
        1L * (x1 - x0) * (x1 - x0) + 1L * (y1 - y0) * (y1 - y0)

@Strictfp fun distance(x0: Int, y0: Int, x1: Int, y1: Int): Float =
        Math.sqrt(squareDistance(x0, y0, x1, y1).toDouble()).toFloat()

fun intersects(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        otherX: Int,
        otherY: Int,
        otherWidth: Int,
        otherHeight: Int
): Boolean {
    require(width > 0) { "Rectangle width must be positive!" }
    require(height > 0) { "Rectangle height must be positive!" }
    require(otherWidth > 0) { "Other rectangle width must be positive!" }
    require(otherHeight > 0) { "Other rectangle height must be positive!" }
    return x < otherX + otherWidth && x + width > otherX &&
            y < otherY + otherHeight && y + height > otherY
}

private fun det(a: Point, b: Point, c: Point): Long =
        1L * (b.x - a.x) * (c.y - a.y) - 1L * (b.y - a.y) * (c.x - a.x)

private fun sgn(value: Long): Int =
        if (value < 0) -1 else if (value > 0) 1 else 0

fun intersectLines(
        from: Point,
        to: Point,
        otherFrom: Point,
        otherTo: Point
): Boolean = if (from == to && otherFrom == otherTo) from == otherFrom
else if (from == to) sgn(det(otherFrom, otherTo, from)) == 0 &&
        Math.min(otherFrom.x, otherTo.x) <= from.x &&
        Math.min(otherFrom.y, otherTo.y) <= from.y &&
        from.x <= Math.max(otherFrom.x, otherTo.x) &&
        from.y <= Math.max(otherFrom.y, otherTo.y)
else if (otherFrom == otherTo) sgn(det(from, to, otherFrom)) == 0 &&
        Math.min(from.x, to.x) <= otherFrom.x &&
        Math.min(from.y, to.y) <= otherFrom.y &&
        otherFrom.x <= Math.max(from.x, to.x) &&
        otherFrom.y <= Math.max(from.y, to.y)
else sgn(det(from, to, otherFrom)) * sgn(det(from, to, otherTo)) <= 0 &&
        sgn(det(otherFrom, otherTo, from)) *
                sgn(det(otherFrom, otherTo, to)) <= 0

fun intersectShapes(shape: List<Point>, otherShape: List<Point>): Boolean {
    if (!boundingBox(shape).intersects(boundingBox(otherShape))) {
        return false
    }
    for (i in 0..shape.size - 2) {
        for (j in 0..otherShape.size - 2) {
            if (intersectLines(
                    from = shape[i],
                    to = shape[i + 1],
                    otherFrom = otherShape[j],
                    otherTo = otherShape[j + 1])
            ) {
                return true
            }
        }
    }
    return false
}

fun boundingBox(points: List<Point>): Rectangle {
    require(!points.isEmpty()) {
        "Bounding box of empty point set is not defined!"
    }
    var (minX, minY) = points.first()
    var (maxX, maxY) = points.first()
    points.forEach { p ->
        minX = Math.min(minX, p.x)
        minY = Math.min(minY, p.y)
        maxX = Math.max(maxX, p.x)
        maxY = Math.max(maxY, p.y)
    }
    return Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1)
}

fun boundingBox(vararg points: Point): Rectangle = boundingBox(points.asList())
