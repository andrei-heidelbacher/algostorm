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

import algostorm.event.Publisher
import algostorm.event.Subscribe
import algostorm.event.Subscriber

/**
 * A system that triggers every registered [Timer] when it expires.
 *
 * For every [Tick] event, it ticks every timer which is saved in the [TIMELINE]
 * property.
 *
 * @property properties the properties of the game
 * @property publisher the publisher used to post expired timer events
 */
class TimeSystem(
        private val properties: MutableMap<String, Any>,
        private val publisher: Publisher
) : Subscriber {
    companion object {
        /**
         * The property used by this system. It should be an object of type
         * [Timeline].
         */
        const val TIMELINE: String = "timeline"
    }

    private var timeline: Timeline
        get() = properties[TIMELINE] as Timeline
        set(value: Timeline) {
            properties[TIMELINE] = value
        }

    @Subscribe fun handleRegisterTimer(event: RegisterTimer) {
        if (event.timer.remainingTicks == 0) {
            publisher.post(event.timer.events)
        } else {
            timeline = Timeline(timeline.timers + event.timer)
        }
    }

    @Subscribe fun handleTick(event: Tick) {
        val newTimers = timeline.timers.map {
            it.copy(remainingTicks = it.remainingTicks - 1)
        }
        val (expired, notExpired) = newTimers.partition {
            it.remainingTicks == 0
        }
        expired.forEach { publisher.post(it.events) }
        timeline = Timeline(notExpired)
    }
}
