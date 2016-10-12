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

package com.aheidelbacher.algostorm.systems.time

import com.aheidelbacher.algostorm.event.Publisher
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber

/**
 * A system that triggers every registered [Timer] when it expires.
 *
 * @property timeline the timeline used to manage timers
 * @property publisher the publisher used to post expired timer events
 */
class TimeSystem(
        private val timeline: Timeline,
        private val publisher: Publisher
) : Subscriber {
    /**
     * Upon receiving a [RegisterTimer] event, it adds it to the [timeline].
     *
     * @param event the event which requests the registration of a timer
     */
    @Subscribe fun onRegisterTimer(event: RegisterTimer) {
        if (event.timer.remainingTicks == 0) {
            publisher.post(event.timer.events)
        } else {
            timeline.timers = timeline.timers + event.timer
        }
    }

    /**
     * Upon receiving a [Tick] event, it updates the [timeline] timers and
     * triggers the expired ones.
     *
     * @param event the event which signals a tick has elapsed
     */
    @Subscribe fun onTick(event: Tick) {
        val (expired, remaining) = timeline.timers.map {
            it.copy(remainingTicks = it.remainingTicks - 1)
        }.partition { it.remainingTicks == 0 }
        timeline.timers = remaining
        expired.forEach { publisher.post(it.events) }
    }
}
