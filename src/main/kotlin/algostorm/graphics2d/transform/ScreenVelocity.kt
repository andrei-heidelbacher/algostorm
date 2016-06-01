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

package algostorm.graphics2d.transform

import algostorm.ecs.Component

/**
 * A component which indicates the amount which the associated `ScreenPosition`
 * translates, once every `Tick`.
 *
 * This component is used internally by the [ScreenVelocitySystem] and should
 * not be set, removed or modified manually.
 *
 * @property x the x-axis translation amount per tick, in tiles
 * @property y the y-axis translation amount per tick, in tiles
 */
data class ScreenVelocity(val x: Float, val y: Float) : Component {
    operator fun unaryMinus(): ScreenVelocity = ScreenVelocity(-x, -y)

    operator fun plus(other: ScreenVelocity): ScreenVelocity =
            ScreenVelocity(x + other.x, y + other.y)

    operator fun minus(other: ScreenVelocity): ScreenVelocity =
            ScreenVelocity(x - other.x, y - other.y)
}
