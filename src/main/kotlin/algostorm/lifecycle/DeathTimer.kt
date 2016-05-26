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

package algostorm.lifecycle

import algostorm.ecs.Component

/**
 * A component which indicates that the current entity should die within
 * [remainingTicks] ticks.
 *
 * This component should be used for creating transient entities whose ids are
 * not available.
 *
 * @property remainingTicks the number of ticks after which the entity dies
 * @throws IllegalArgumentException if [remainingTicks] is not positive
 */
data class DeathTimer(val remainingTicks: Int) : Component {
    init {
        require(remainingTicks > 0) {
            "Death timer must have positive remaining ticks!"
        }
    }

    /**
     * Returns a copy of the death timer after a tick has passed.
     *
     * @return the timer information after another tick has elapsed
     */
    fun tick(): DeathTimer = copy(remainingTicks = remainingTicks - 1)
}
