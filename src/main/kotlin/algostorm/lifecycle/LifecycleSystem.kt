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

import algostorm.ecs.MutableEntityManager
import algostorm.ecs.PublisherSystem
import algostorm.engine.Tick
import algostorm.event.Event
import algostorm.event.EventBus
import algostorm.event.Subscriber

/**
 * A system that handles the creation and deletion of entities through [CreateEntity] and [Death]
 * events to allow notifying other systems and handles [DeathTimer] components.
 *
 * After receiving a [CreateEntity] request, it publishes a [Spawn] event. After receiving a [Death]
 * event, it publishes a [DeleteEntity] request. Only the [LifecycleSystem] should listen for
 * [DeleteEntity] events, as the specified entity may not be accessible any longer at the moment of
 * notification. After receiving a [Tick] event, it ticks all entities that have a [DeathTimer] and
 * publishes a [Death] event for the entities whose timers reach `0`.
 */
class LifecycleSystem(
    private val entityManager: MutableEntityManager,
    eventBus: EventBus
) : PublisherSystem(eventBus) {
  /**
   * Requests the deletion of the given [entityId].
   *
   * Should only be listened to by the [LifecycleSystem]. At the time of notification, the entity may
   * already be deleted.
   *
   * @property entityId the id of the entity which should be deleted
   */
  private data class DeleteEntity(val entityId: Int) : Event

  private val createHandler = Subscriber(CreateEntity::class) { event ->
    post(Spawn(entityManager.create(event.components).id))
  }

  private val deleteHandler = Subscriber(DeleteEntity::class) { event ->
    entityManager.delete(event.entityId)
  }

  private val deathHandler = Subscriber(Death::class) { event ->
    post(DeleteEntity(event.entityId))
  }

  private val tickHandler = Subscriber(Tick::class) { event ->
    entityManager.getEntitiesWithComponentType(DeathTimer::class).forEach { entity ->
      entity.get<DeathTimer>()?.let { timer ->
        if (timer.remainingTicks == 1) {
          post(Death(entity.id))
        } else {
          entity.set(timer.tick())
        }
      }
    }
  }

  /**
   * This system handles [CreateEntity], [DeleteEntity], [Death] and [Tick] events.
   */
  final override val handlers: List<Subscriber<*>> = listOf(
      createHandler,
      deleteHandler,
      deathHandler,
      tickHandler
  )
}
