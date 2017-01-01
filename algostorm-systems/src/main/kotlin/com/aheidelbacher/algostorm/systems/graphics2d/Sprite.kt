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

import com.aheidelbacher.algostorm.ecs.Component
import com.aheidelbacher.algostorm.engine.graphics2d.Color

/**
 * A component which contains data required for rendering.
 *
 * @property gid the global id of this tile
 * @property width the width in pixels
 * @property height the height in pixels
 * @property z the rendering layer (sprites should be rendered ascending by `z`)
 * @property priority the rendering priority (sprites with equal `z` and equal
 * `y` should be rendered ascending by `priority`)
 * @property isVisible
 * @property offsetX the horizontal rendering offset in pixels
 * @property offsetY the vertical rendering offset in pixels (positive is down)
 * @property color
 * @throws IllegalArgumentException if [gid] is negative or if [width] or
 * [height] are not positive
 */
data class Sprite(
        val width: Int,
        val height: Int,
        val z: Int,
        val priority: Int,
        val gid: Int = 0,
        val isVisible: Boolean = true,
        val offsetX: Int = 0,
        val offsetY: Int = 0,
        val color: Color? = null
) : Component {
    init {
        require(gid >= 0) { "$this gid must not be negative!" }
        require(width > 0) { "$this width must be positive!" }
        require(height > 0) { "$this height must be positive!" }
    }
}
