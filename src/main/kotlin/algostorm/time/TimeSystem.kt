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

import algostorm.ecs.Component
import algostorm.ecs.EntitySystem
import algostorm.ecs.MutableEntityManager
import algostorm.engine.Tick
import algostorm.event.Publisher
import algostorm.event.Subscriber

/**
 * A system that triggers every registered [Timer] when it expires.
 *
 * For every [Tick] event, it ticks every timer which is saved on a special entity that contains
 * a single [Timeline] component.
 */
class TimeSystem(
    private val entityManager: MutableEntityManager,
    private val publisher: Publisher
) : EntitySystem() {
  private data class Timeline(val timers: List<Timer>) : Component

  private val timelineEntity = entityManager
      .getEntitiesWithComponentType(Timeline::class)
      .singleOrNull() ?:
      entityManager.create(listOf(Timeline(emptyList())))

  private val timeline: Timeline
    get() = timelineEntity.get<Timeline>() ?: error(
        "Timeline entity must contain the timeline component!"
    )

  private val registerHandler = Subscriber(RegisterTimer::class) { event ->
    if (event.timer.remainingTicks == 0) {
      publisher.post(event.timer.events)
    } else {
      timelineEntity.set(Timeline(timeline.timers + event.timer))
    }
  }

  private val tickHandler = Subscriber(Tick::class) { event ->
    val newTimers = timeline.timers.map { timer -> timer.tick() }
    val (expired, notExpired) = newTimers.partition { timer -> timer.remainingTicks == 0 }
    expired.forEach { timer -> publisher.post(timer.events) }
    timelineEntity.set(Timeline(notExpired))
  }

  /**
   * This system handles [RegisterTimer] and [Tick] events.
   */
  override val handlers: List<Subscriber<*>> = listOf(registerHandler, tickHandler)
}
