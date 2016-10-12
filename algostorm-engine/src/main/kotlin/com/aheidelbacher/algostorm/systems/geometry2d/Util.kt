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

package com.aheidelbacher.algostorm.systems.geometry2d

fun squareDistance(x0: Int, y0: Int, x1: Int, y1: Int): Long =
        1L * (x1 - x0) * (x1 - x0) + 1L * (y1 - y0) * (y1 - y0)

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
