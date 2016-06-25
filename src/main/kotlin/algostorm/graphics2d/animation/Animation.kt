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

package algostorm.graphics2d.animation

import algostorm.ecs.Entity

/**
 * A component which contains the animation information of an entity.
 *
 * This component should not be directly created or removed. Instead, use the
 * [Animate] event.
 *
 * @property sheetId the id of the animation sheet used for this entity
 * @property name the name of the animation
 * @property remainingTicks the number of ticks after which the animation is
 * finished
 * @throws IllegalArgumentException if [remainingTicks] is negative
 */
data class Animation(
        val sheetId: Int,
        val name: String,
        val remainingTicks: Int
) {
    companion object {
        /**
         * The name of the animation property. It is of type [Animation].
         */
        const val PROPERTY: String = "animation"

        /**
         * The [Animation] component of this entity, or `null` if it doesn't
         * have an animation.
         */
        val Entity.animation: Animation?
            get() = get(PROPERTY) as Animation?
    }

    init {
        require(remainingTicks >= 0) { "Remaining ticks can't be negative!" }
    }
}
