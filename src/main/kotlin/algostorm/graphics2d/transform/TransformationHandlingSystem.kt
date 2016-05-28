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
import algostorm.graphics2d.transform.Transformation.Companion.apply
import algostorm.graphics2d.transform.Transformation.Companion.transformation
import algostorm.time.Tick

/**
 * A system that manages the transformations applied to entities.
 *
 * Upon receiving a [Tick] event, it updates all [TransformationTimer]
 * components. If any of them completes, it applies the final transformation to
 * the entity and removes the timer. Upon receiving a [Transform] event, it
 * flushes the current `TransformationTimer` and applies it, then sets the new
 * transformation timer to the event transformation.
 *
 * @property entityManager the entity manager used to retrieve and update entity
 * transformations
 */
class TransformationHandlingSystem(
        private val entityManager: MutableEntityManager
) : EntitySystem {
    private val tickHandler = Subscriber(Tick::class) { event ->
        entityManager.getEntitiesWithComponentTypes(TransformationTimer::class)
                .forEach { entity ->
                    entity.get<TransformationTimer>()?.tick()?.let { timer ->
                        if (timer.elapsedTicks < timer.durationInTicks) {
                            entity.set(timer)
                        } else {
                            entity.apply(timer.transformation)
                            entity.remove<TransformationTimer>()
                        }
                    }
                }
    }

    private val transformHandler = Subscriber(Transform::class) { event ->
        entityManager[event.entityId]?.let { entity ->
            entity.apply(entity.transformation)
            entity.set(TransformationTimer(event.transformations, 0))
        }
    }

    /**
     * This system handles [Tick] and [Transform] events.
     */
    override val handlers: List<Subscriber<*>> =
            listOf(tickHandler, transformHandler)
}
