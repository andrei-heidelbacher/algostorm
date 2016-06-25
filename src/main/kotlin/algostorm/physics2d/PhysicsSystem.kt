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

import algostorm.ecs.MutableEntityManager
import algostorm.event.Publisher
import algostorm.event.Subscribe
import algostorm.event.Subscriber
import algostorm.physics2d.Box.Companion.box
import algostorm.physics2d.Rigid.isRigid

/**
 * A system that handles [TranslateIntent] events and publishes [Translated] and
 * [Collision] events.
 *
 * @property entityManager the entity manager used to retrieve and update the
 * entities
 * @property publisher the publisher used to post `Translated` and `Collision`
 * events
 */
class PhysicsSystem(
        private val entityManager: MutableEntityManager,
        private val publisher: Publisher
) : Subscriber {
    /**
     * Upon receiving a [TranslateIntent] event, the [Box] of the entity is
     * translated by the indicated amount. If the moved entity is [Rigid] and
     * there are any other `Rigid` entities with their boxes overlapping the
     * destination location, the entity is restored to it's initial location and
     * a [Collision] event is triggered with every overlapping entity, having
     * this entity as the source and each other entity as the target.
     */
    @Subscribe fun handleTranslateIntent(event: TranslateIntent) {
        entityManager[event.entityId]?.let { entity ->
            val newBox = entity.box?.translate(event.dx, event.dy)
                    ?: error("Can't translate an entity without a location!")
            val overlappingEntities = entityManager.entities.filter {
                it != entity && it.isRigid && it.box?.overlaps(newBox) ?: false
            }
            if (!entity.isRigid || overlappingEntities.count() == 0) {
                entity[Box.PROPERTY] = newBox
                publisher.post(Translated(event.entityId, event.dx, event.dy))
            } else {
                overlappingEntities.forEach {
                    publisher.post(Collision(event.entityId, it.id))
                }
            }
        }
    }
}
