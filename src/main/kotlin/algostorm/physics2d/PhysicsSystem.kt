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

package algostorm.physics2d

import algostorm.ecs.EntitySystem
import algostorm.ecs.MutableEntityManager
import algostorm.event.Publisher
import algostorm.event.Subscriber
import algostorm.physics2d.Box.Companion.box
import algostorm.physics2d.Rigid.isRigid
import algostorm.physics2d.Velocity.Companion.velocity
import algostorm.time.Tick

/**
 * A system that handles [Tick] and [Accelerate] events and publishes
 * [Collision] events.
 *
 * Upon receiving an [Accelerate] event, it adds the indicated amounts to the
 * [Velocity] of the specified entity.
 *
 * Upon receiving a [Tick] event, it fetches all entities that have a [Velocity]
 * component with at least one non-null component, sorts them increasing by the
 * entity id, and moves each of them in this order. When an entity is moved, the
 * [Box] is translated by the values of the velocity. If the destination
 * location overlaps with a different [Rigid] box, a [Collision] is triggered
 * with the current entity as the source and the overlapping entity as the
 * target and the current entity is restored to the initial position.
 *
 * Entities without a box can't be accelerated or moved.
 *
 * @property entityManager the entity manager used to retrieve and update the
 * entities
 * @property publisher the publisher used to post `Translated` and `Collision`
 * events
 */
class PhysicsSystem(
        private val entityManager: MutableEntityManager,
        private val publisher: Publisher
) : EntitySystem {
    private val tickHandler = Subscriber(Tick::class) { event ->
        entityManager.filterEntities(Velocity::class).filter {
            it.velocity != Velocity(0, 0)
        }.sortedBy { entity ->
            entity.id
        }.forEach { entity ->
            val box = entity.box ?: error("Entity has no box!")
            val (dx, dy) = entity.velocity
            val newBox = box.copy(x = box.x + dx, y = box.y + dy)
            val overlappingEntities = entityManager
                    .filterEntities(Rigid::class, Box::class)
                    .filter {
                        it != entity && it.box?.overlaps(newBox) ?: false
                    }
            if (!entity.isRigid || overlappingEntities.count() == 0) {
                entity.set(newBox)
            } else {
                entity.remove<Velocity>()
                overlappingEntities.forEach {
                    publisher.post(Collision(entity.id, it.id))
                }
            }
        }
    }

    private val accelerateHandler = Subscriber(Accelerate::class) { event ->
        entityManager[event.entityId]?.let { entity ->
            entity.box ?: error("Can't accelerate entity without a box!")
            val (vx, vy) = entity.velocity
            val newVelocity = Velocity(vx + event.x, vy + event.y)
            if (newVelocity != Velocity(0, 0)) {
                entity.set(newVelocity)
            } else {
                entity.remove<Velocity>()
            }
        }
    }

    /**
     * This system handles [Tick] and [Accelerate] events.
     */
    override val handlers: List<Subscriber<*>> =
            listOf(tickHandler, accelerateHandler)
}
