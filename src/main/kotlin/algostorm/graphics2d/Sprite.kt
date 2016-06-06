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
 * A component that contains rendering information.
 *
 * A sprite is rendered as a tile and is identified through its [tileId].
 *
 * Entities should be rendered in ascending order by their [z] coordinate. In
 * case of equality, the user can define a custom ordering.
 *
 * @property tileId the unique identifier of the sprite
 * @property flippedHorizontally whether the sprite should be flipped
 * horizontally
 * @property flippedVertically whether the sprite should be flipped vertically
 * @property flippedDiagonally whether the sprite should be flipped diagonally
 * @property z the z-coordinate of the owner entity
 */
data class Sprite(
        val tileId: Int,
        val flippedHorizontally: Boolean,
        val flippedVertically: Boolean,
        val flippedDiagonally: Boolean,
        val z: Int
) : Component {
    companion object {
        /**
         * The [Sprite] component of this entity, or `null` if it doesn't have a
         * sprite.
         */
        val Entity.sprite: Sprite?
            get() = get()
    }
}
