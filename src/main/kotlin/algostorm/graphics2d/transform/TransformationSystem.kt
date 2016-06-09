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
import algostorm.graphics2d.transform.Transformation.Companion.transformation

/**
 * A system that manages the transformations applied to entities.
 *
 * Upon receiving a [Transform] event, it adds the indicated [Transformation] to
 * the current entity `Transformation`.
 *
 * @property entityManager the entity manager used to fetch and update entity
 * transformations
 */
class TransformationSystem(
        private val entityManager: MutableEntityManager
) : EntitySystem {
    private val transformHandler = Subscriber(Transform::class) { event ->
        entityManager[event.entityId]?.let { entity ->
            entity.set(entity.transformation + event.transformation)
        }
    }

    /**
     * This system handles [Transform] events.
     */
    override val handlers: List<Subscriber<*>> = listOf(transformHandler)
}
