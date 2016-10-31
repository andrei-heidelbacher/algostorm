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

import com.aheidelbacher.algostorm.systems.physics2d.squareDistance

data class Circle(val x: Int, val y: Int, val radius: Int) {
    init {
        require(radius >= 0) { "$this radius can't be negative!" }
    }

    fun contains(x: Int, y: Int): Boolean =
            squareDistance(this.x, this.y, x, y) <= 1L * radius * radius

    operator fun contains(point: Point): Boolean = contains(point.x, point.y)

    fun translate(dx: Int, dy: Int): Circle = copy(x = x + dx, y = y + dy)
}
