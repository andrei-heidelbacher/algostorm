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

package com.aheidelbacher.algostorm.systems.physics2d.geometry2d

import com.aheidelbacher.algostorm.systems.physics2d.geometry2d.Point

/**
 * A rectangle which covers the area `[x, x + width - 1] x [y, y + height - 1]`.
 *
 * @property x the x-axis coordinate of the upper-left corner of the rectangle
 * @property y the y-axis coordinate of the upper-left corner of the rectangle
 * @property width the width of the rectangle
 * @property height the height of the rectangle
 * @throws IllegalArgumentException if [width] or [height] are not positive
 */
data class Rectangle(val x: Int, val y: Int, val width: Int, val height: Int) {
    init {
        require(width > 0) { "$this width must be positive!" }
        require(height > 0) { "$this height must be positive!" }
    }

    /**
     * Returns whether the two rectangles intersect (that is, there is at least
     * one point `(x, y)` which is contained in both rectangles).
     *
     * @param other the rectangle with which the intersection is checked
     * @return `true` if the two rectangles intersect, `false` otherwise
     */
    fun intersects(other: Rectangle): Boolean = com.aheidelbacher.algostorm.systems.physics2d.intersects(
            x = x,
            y = y,
            width = width,
            height = height,
            otherX = other.x,
            otherY = other.y,
            otherWidth = other.width,
            otherHeight = other.height
    )

    /**
     * Returns whether the two rectangles intersect (that is, there is at least
     * one point `(x, y)` which is contained in both rectangles).
     *
     * @param x the x-axis coordinate of the top-left corner of the other
     * rectangle
     * @param y the y-axis coordinate of the top-left corner of the other
     * rectangle
     * @param width the width of the other rectangle
     * @param height the height of the other rectangle
     * @return `true` if the two rectangles intersect, `false` otherwise
     * @throws IllegalArgumentException if [width] or [height] are not positive
     */
    fun intersects(x: Int, y: Int, width: Int, height: Int): Boolean =
            com.aheidelbacher.algostorm.systems.physics2d.intersects(
                    x = this.x,
                    y = this.y,
                    width = this.width,
                    height = this.height,
                    otherX = x,
                    otherY = y,
                    otherWidth = width,
                    otherHeight = height
            )

    /**
     * Checks if the given point is inside this rectangle.
     *
     * @param x the x-axis coordinate of the point
     * @param y the y-axis coordinate of the point
     * @return `true` if the given point is contained in this rectangle, `false`
     * otherwise
     */
    fun contains(x: Int, y: Int): Boolean =
            this.x <= x && x < this.x + width &&
                    this.y <= y && y < this.y + height

    /**
     * Checks if the given point is inside this rectangle.
     *
     * @param point the point which should be checked
     * @return `true` if the given point is contained in this rectangle, `false`
     * otherwise
     */
    operator fun contains(point: Point): Boolean = contains(point.x, point.y)

    /**
     * Returns a copy of this rectangle with the top-left corner translated by
     * the given amount.
     *
     * @param dx the horizontal translation amount
     * @param dy the vertical translation amount (positive is down)
     * @return the translated rectangle
     */
    fun translate(dx: Int, dy: Int): Rectangle = copy(x = x + dx, y = y + dy)
}
