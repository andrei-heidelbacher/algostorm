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

package algostorm.graphics2d

import algostorm.ecs.Component
import algostorm.ecs.Entity

/**
 * A component which contains the rendering position.
 *
 * Entities should be rendered in ascending order by their [z] coordinate. In
 * case of equality, the user can define a custom ordering according to [x] and
 * [y] coordinates.
 *
 * @property x the x-coordinate of the current entity in tiles
 * @property y the y-coordinate of the current entity in tiles
 * @property z the z-coordinate of the current entity
 */
data class ScreenPosition(val x: Float, val y: Float, val z: Int) : Component {
    companion object {
        /**
         * The [ScreenPosition] component of this entity, or `null` if it
         * doesn't have a screen position.
         */
        val Entity.screenPosition: ScreenPosition?
            get() = get()
    }
}
