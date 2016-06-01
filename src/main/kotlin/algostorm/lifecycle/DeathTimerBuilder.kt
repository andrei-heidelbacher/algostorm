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

import algostorm.event.Event
import algostorm.time.RegisterTimer
import algostorm.time.Timer

/**
 * An event builder which indicates that the current entity should die within
 * [remainingTicks] ticks after it is spawned.
 *
 * This event builder should be used for creating transient entities whose ids
 * are not available at the time of creation.
 *
 * It creates a [RegisterTimer] event which will trigger a [Death] event after
 * the timer expires.
 *
 * @property remainingTicks the number of ticks after which the entity dies
 * @throws IllegalArgumentException if [remainingTicks] is negative
 */
data class DeathTimerBuilder(
        val remainingTicks: Int
) : CreateEntity.EventBuilder {
    init {
        require(remainingTicks >= 0) {
            "Death timer can't have negative remaining ticks!"
        }
    }

    override fun build(entityId: Int): Event =
            RegisterTimer(Timer(remainingTicks, Death(entityId)))
}
