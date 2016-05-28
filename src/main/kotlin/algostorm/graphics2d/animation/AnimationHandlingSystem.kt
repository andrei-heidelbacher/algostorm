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

package algostorm.graphics2d.animation

import algostorm.ecs.EntitySystem
import algostorm.ecs.MutableEntity
import algostorm.ecs.MutableEntityManager
import algostorm.event.Subscriber
import algostorm.graphics2d.animation.Animation.Companion.animation
import algostorm.time.Tick

/**
 * A system that manages the animation information of entities.
 *
 * Upon receiving a [Tick] event, it updates all the [Animation] components. If
 * any of them completes, they are continued with the associated idle
 * animations. Upon receiving an [Animate] event, it overwrites the current
 * animation with the indicated animation.
 *
 * @property entityManager the entity manager used to retrieve and update entity
 * animations
 */
class AnimationHandlingSystem(
        private val entityManager: MutableEntityManager
) : EntitySystem {
    private fun update(entity: MutableEntity, animation: Animation?) {
        animation ?: error(
                "Can't animate an entity without an animation component!"
        )
        entity.set(animation)
        entity.set(animation.sprite)
      }

    private val tickHandler = Subscriber(Tick::class) { event ->
        entityManager.getEntitiesWithComponentType(Animation::class)
                .forEach { entity ->
                    val animation = entity.animation?.tick()
                    update(entity, animation)
                }
    }

    private val animateHandler = Subscriber(Animate::class) { event ->
        entityManager[event.entityId]?.let { entity ->
            val animation = entity.animation?.let { animation ->
                animation.copy(
                        frames = animation.animationSheet[event.animation],
                        elapsedTicks = 0
                )
            }
            update(entity, animation)
        }
    }

    /**
     * This system handles [Tick] and [Animate] events.
     */
    override val handlers: List<Subscriber<*>> =
            listOf(tickHandler, animateHandler)
}
