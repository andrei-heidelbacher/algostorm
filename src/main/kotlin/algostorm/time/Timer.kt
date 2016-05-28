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

package algostorm.time

import algostorm.event.Event

/**
 * A timer which will post the associated [event] when [remainingTicks] ticks
 * have elapsed.
 *
 * @property remainingTicks the number of ticks that need to pass until the
 * timer expires
 * @property event the event which will be posted when the timer expires
 * @throws IllegalArgumentException if [remainingTicks] is negative
 */
data class Timer(val remainingTicks: Int, val event: Event) {
    init {
        require(remainingTicks >= 0) { "Timer duration can't be negative!" }
    }

    /**
     * Returns a copy of the timer after a tick has elapsed.
     *
     * @return the new timer after a tick has passed
     */
    fun tick(): Timer = copy(remainingTicks = remainingTicks - 1)
}
