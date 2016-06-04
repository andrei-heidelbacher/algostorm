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

import algostorm.ecs.EntitySystem
import algostorm.ecs.MutableEntityManager
import algostorm.event.Publisher
import algostorm.event.Subscriber

/**
 * A system that triggers every registered [Timer] when it expires.
 *
 * For every [Tick] event, it ticks every timer which is saved on a special
 * entity that contains a single [Timeline] component.
 *
 * When this system is created, the timeline owner entity is fetched from the
 * entity manager. If it doesn't exist, a new entity is created; however, the
 * `CreateEntity` - `Spawned` process is skipped. Because this system should be
 * created before the engine is started and before any processing begins, a
 * `ConcurrentModificationException` can't occur.
 *
 * The system handlers will throw an [IllegalStateException] if the timeline
 * entity is deleted from the entity manager.
 *
 * @property entityManager the entity manager of the game
 * @property properties the properties of the game
 * @property publisher the publisher used to post expired timer events
 * @throws IllegalStateException if, at the time of creation, the entity manager
 * contains more than one timeline entity
 */
class TimeSystem(
        private val entityManager: MutableEntityManager,
        private val properties: MutableMap<String, Any?>,
        private val publisher: Publisher
) : EntitySystem {
    companion object {
        /**
         * The property used by this system. It should be an object of type
         * [Timeline].
         */
        const val TIMELINE: String = "timeline"
    }

    /**
     * A special component which is attached to a unique entity and contains all
     * the timers in the game.
     *
     * At most one such component should exist at any point in time, and at most
     * one entity should contain this component at any point in time.
     *
     * @property timers a list which contains all the active timers in the game
     */
    data class Timeline(val timers: List<Timer>)

    private var timeline: Timeline by properties

    private val registerHandler = Subscriber(RegisterTimer::class) { event ->
        if (event.timer.remainingTicks == 0) {
            publisher.post(event.timer.events)
        } else {
            timeline = Timeline(timeline.timers + event.timer)
        }
    }

    private val tickHandler = Subscriber(Tick::class) { event ->
        val newTimers = timeline.timers.map {
            it.copy(remainingTicks = it.remainingTicks - 1)
        }
        val (expired, notExpired) = newTimers.partition {
            it.remainingTicks == 0
        }
        expired.forEach { publisher.post(it.events) }
        timeline = Timeline(notExpired)
    }

    /**
     * This system handles [RegisterTimer] and [Tick] events.
     */
    override val handlers: List<Subscriber<*>> =
            listOf(registerHandler, tickHandler)
}
