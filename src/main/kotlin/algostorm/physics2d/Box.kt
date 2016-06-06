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

package algostorm.physics2d

import algostorm.ecs.Component
import algostorm.ecs.Entity

/**
 * A rectangular box containing [width] x [height] tiles with the bottom-left
 * corner tile being at
 * ([x], [y]).
 *
 * @property x the x-axis coordinate of the bottom-left corner of this box
 * @property y the y-axis coordinate of the bottom-left corner of this box
 * @property width the width of this box in tiles
 * @property height the height of this box in tiles
 * @throws IllegalArgumentException if [width] or [height] are not positive
 */
data class Box(
        val x: Int,
        val y: Int,
        val width: Int,
        val height: Int
) : Component {
    companion object {
        /**
         * The [Box] component of this entity, or `null` if it doesn't have a
         * box.
         */
        val Entity.box: Box?
            get() = get()
    }

    init {
        require(width > 0 && height > 0) { "Box dimensions must be positive!" }
    }

    /**
     * Returns whether the given tile lies inside this box.
     *
     * @param x the x-axis coordinate of the tile
     * @param y the y-axis coordinate of the tile
     * @return `true` if the given tile is inside this box, `false` otherwise
     */
    fun contains(x: Int, y: Int): Boolean =
            this.x <= x && x <= this.x + this.width &&
                    this.y <= y && y <= this.y + this.height

    /**
     * Returns whether the two boxes overlap (that is, there exists a tile
     * (x, y) such that it lies inside both boxes).
     *
     * @param other the box with which the intersection is checked
     * @return `true` if the two boxes overlap, `false` otherwise
     */
    fun overlaps(other: Box): Boolean = contains(other.x, other.y) ||
            contains(other.x, other.y + other.height) ||
            contains(other.x + other.width, other.y) ||
            contains(other.x + other.width, other.y + other.height)

    /**
     * Returns a copy of this box translated with the indicated amount.
     *
     * @param dx the translation amount on the x-axis
     * @param dy the translation amount on the y-axis
     * @return the translated box
     */
    fun translate(dx: Int, dy: Int): Box = copy(x = x + dx, y = y + dy)
}
