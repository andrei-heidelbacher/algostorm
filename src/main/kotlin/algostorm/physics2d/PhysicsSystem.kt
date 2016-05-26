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
import algostorm.physics2d.Collidable.isCollidable
import algostorm.physics2d.Rigid.isRigid

/**
 * A system that handles [TranslateIntent] events and publishes [Translated] and
 * [Collision] events.
 *
 * When a translation request is received, it translates the entity [Box]
 * component to the new location. If there are no other overlapping [Rigid]
 * boxes at the destination or if the entity that should be translated doesn't
 * contain a `Rigid` component, the translation succeeds and a `Translated`
 * event is published. Otherwise, the translation fails, the `Box` component
 * remains unchanged, and no events are published. The translated entity box is
 * allowed to overlap with itself, as it will not block the translation.
 *
 * After the translation is handled (successfully or not), collisions are
 * checked. If the translated entity contains a [Collidable] component, then for
 * every overlapping `Collidable` box at the destination, a [Collision] event
 * having this entity as the source and the overlapping entity as the target is
 * published. The translated entity box is allowed to overlap with itself, as it
 * will not trigger a collision with itself.
 */
class PhysicsSystem(
        private val entityManager: MutableEntityManager,
        private val publisher: Publisher
) : EntitySystem {
    private val translateHandler = Subscriber(TranslateIntent::class) { event ->
        val entity = entityManager[event.entityId] ?: error(
                "Translated entity doesn't exist!"
        )
        val newBox = entity.box?.translate(event.dx, event.dy) ?: error(
                "Can't translate an entity without a location!"
        )
        val overlappingEntities = entityManager
                .getEntitiesWithComponentType(Box::class)
                .filter { it != entity && it.box?.overlaps(newBox) ?: false }
        if (!entity.isRigid || overlappingEntities.none { it.isRigid }) {
            entity.set(newBox)
            publisher.post(Translated(event.entityId, event.dx, event.dy))
        }
        if (entity.isCollidable) {
            overlappingEntities.filter { it.isCollidable }.forEach {
                publisher.post(Collision(event.entityId, it.id))
            }
        }
    }

    /**
     * This system handles [TranslateIntent] events.
     */
    override val handlers: List<Subscriber<*>> = listOf(translateHandler)
}
