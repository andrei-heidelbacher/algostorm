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

import algostorm.ecs.Entity
import algostorm.ecs.MutableEntityManager
import algostorm.event.Publisher
import algostorm.event.Subscribe
import algostorm.event.Subscriber

/**
 * A system that triggers every registered [Timer] when it expires.
 *
 * @property timeline the timeline used to manage timers
 * @property publisher the publisher used to post expired timer events
 */
class TimeSystem(
        private val entityManager: MutableEntityManager,
        private val publisher: Publisher
) : Subscriber {
    private companion object {
        val Entity.timers: List<Timer>
            get() = (get(Timeline.PROPERTY) as Timeline?)?.timers ?: emptyList()
    }
    /**
     * Upon receiving a [RegisterTimer] event, it calls the
     * [Timeline.registerTimer] method.
     *
     * @param event the event which requests the registration of a timer
     */
    @Subscribe fun handleRegisterTimer(event: RegisterTimer) {
        if (event.timer.remainingTicks == 0) {
            publisher.post(event.timer.events)
        } else {
        }
    }

    /**
     * Upon receiving a [Tick] event, it calls the [Timeline.tick] method and
     * triggers the expired timers.
     *
     * @param event the event which signals a tick has elapsed
     */
    @Subscribe fun handleTick(event: Tick) {
        entityManager.entities.forEach {
            it.timers
        }
    }
}
