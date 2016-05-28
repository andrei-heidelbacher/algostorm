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

import algostorm.ecs.EntitySystem
import algostorm.ecs.MutableEntityManager
import algostorm.event.Event
import algostorm.event.Publisher
import algostorm.event.Subscriber
import algostorm.time.RegisterTimer
import algostorm.time.Timer

/**
 * A system that handles the creation and deletion of entities through
 * [CreateEntity] and [Death] events to allow notifying other systems and to
 * avoid `ConcurrentModificationException`s. Also handles [DeathTimer]
 * components.
 *
 * After receiving a [CreateEntity] request, it publishes a [Spawned] event.
 * After receiving a [Death] event, it publishes a [DeleteEntity] request. Only
 * the [LifecycleSystem] should listen for [DeleteEntity] events, as the
 * specified entity may not be accessible any longer at the moment of
 * notification. After receiving a [Spawned] event with an entity that contains
 * a [DeathTimer], it publishes a [RegisterTimer] event which will trigger the
 * entity's [Death] when the timer expires.
 *
 * @property entityManager the entity manager which supports the creation and
 * deletion of entities
 * @property publisher the publisher used to post events
 */
class LifecycleSystem(
        private val entityManager: MutableEntityManager,
        private val publisher: Publisher
) : EntitySystem {
    /**
     * Requests the deletion of the given [entityId].
     *
     * Should only be listened to by the [LifecycleSystem]. At the time of
     * notification, the entity may already be deleted.
     *
     * @property entityId the id of the entity which should be deleted
     */
    private data class DeleteEntity(val entityId: Int) : Event

    private val createHandler = Subscriber(CreateEntity::class) { event ->
        publisher.post(Spawned(entityManager.create(event.components).id))
    }

    private val deleteHandler = Subscriber(DeleteEntity::class) { event ->
        entityManager.delete(event.entityId)
    }

    private val deathHandler = Subscriber(Death::class) { event ->
        publisher.post(DeleteEntity(event.entityId))
    }

    private val spawnedHandler = Subscriber(Spawned::class) { event ->
        entityManager[event.entityId]?.let { entity ->
            entity.get<DeathTimer>()?.let { timer ->
                entity.remove<DeathTimer>()
                publisher.post(RegisterTimer(Timer(
                        remainingTicks = timer.remainingTicks,
                        event = Death(entity.id)
                )))
            }
        }
    }

    /**
     * This system handles [CreateEntity], [DeleteEntity], [Death] and [Spawned]
     * events.
     */
    override val handlers: List<Subscriber<*>> = listOf(
            createHandler,
            deleteHandler,
            deathHandler,
            spawnedHandler
    )
}
