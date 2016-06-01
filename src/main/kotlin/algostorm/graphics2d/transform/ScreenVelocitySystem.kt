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

package algostorm.graphics2d.transform

import algostorm.ecs.EntitySystem
import algostorm.ecs.MutableEntityManager
import algostorm.event.Subscriber
import algostorm.graphics2d.ScreenPosition
import algostorm.graphics2d.ScreenPosition.Companion.screenPosition
import algostorm.time.Tick

/**
 * A system which handles the translation of [ScreenPosition] components,
 * according to their [ScreenVelocity].
 *
 * When it receives an [Accelerate] event, it adds the given amounts to the
 * screen velocity of the entity. When it receives a [Tick] event, it adds the
 * amounts from the associated screen velocity to the screen position.
 *
 * @property entityManager the entity manager used to update the screen
 * positions
 */
class ScreenVelocitySystem(
        private val entityManager: MutableEntityManager
) : EntitySystem {
    private val tickHandler = Subscriber(Tick::class) { event ->
        entityManager.getEntitiesWithComponentTypes(
                ScreenVelocity::class,
                ScreenPosition::class
        ).forEach { entity ->
            val position = entity.screenPosition
            val velocity = entity.get<ScreenVelocity>()
            if (position != null && velocity != null) {
                entity.set(position.copy(
                        x = position.x + velocity.x,
                        y = position.y + velocity.y
                ))
            }
        }
    }

    private val accelerateHandler = Subscriber(Accelerate::class) { event ->
        entityManager[event.entityId]?.let { entity ->
            entity.set(ScreenVelocity(event.x, event.y) +
                    (entity.get<ScreenVelocity>() ?: ScreenVelocity(0F, 0F))
            )
        }
    }

    /**
     * This system handles [Tick] and [Accelerate] events.
     */
    override val handlers: List<Subscriber<*>> =
            listOf(tickHandler, accelerateHandler)
}
