/*
 * Copyright (c) 2016  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
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

/**
 * The primary eight ordinal directions.
 *
 * @property dx the equivalent horizontal translation amount
 * @property dy the equivalent vertical translation amount (positive is down)
 */
enum class Direction(val dx: Int, val dy: Int) {
    N(dx = 0, dy = -1),
    NE(dx = 1, dy = -1),
    E(dx = 1, dy = 0),
    SE(dx = 1, dy = 1),
    S(dx = 0, dy = 1),
    SW(dx = -1, dy = 1),
    W(dx = -1, dy = 0),
    NW(dx = -1, dy = -1);

    companion object {
        /** The cardinal directions: north, east, south, west. */
        val CARDINAL: List<Direction> = listOf(N, E, S, W)

        /**
         * All eight ordinal directions: north, north-east, east, south-east,
         * south, south-west, west, north-west.
         */
        val ORDINAL: List<Direction> = values().asList()
    }
}
