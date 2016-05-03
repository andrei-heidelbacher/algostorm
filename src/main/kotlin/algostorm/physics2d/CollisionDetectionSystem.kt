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

import algostorm.ecs.EntityManager
import algostorm.ecs.PublisherSystem
import algostorm.event.EventBus
import algostorm.event.Subscriber
import algostorm.physics2d.Box.Companion.box

/**
 * A system that publishes [Collision] events if any collision occurs as a result of a
 * [TranslateIntent] event.
 *
 * A collision occurs with all entities located at the destination location which contain the
 * [Collidable] component and have overlapping [Box] components. Collisions between an entity and
 * itself will not be triggered.
 */
class CollisionDetectionSystem(
    private val entityManager: EntityManager,
    eventBus: EventBus
) : PublisherSystem(eventBus) {
  private val translateIntentHandler = Subscriber(TranslateIntent::class) { event ->
    val translatedEntity = entityManager[event.entityId] ?: error(
        "Translating non-existent entity!"
    )
    val newBox = translatedEntity.box?.translate(event.dx, event.dy) ?: error(
        "Translating an entity without a location!"
    )
    entityManager
        .getEntitiesWithComponentTypes(Collidable::class, Box::class)
        .filter { entity -> entity != translatedEntity && entity.box?.overlaps(newBox) ?: false }
        .forEach { entity -> post(Collision(event.entityId, entity.id)) }
  }

  /**
   * This system handles [TranslateIntent] events.
   */
  final override val handlers: List<Subscriber<*>> = listOf(translateIntentHandler)
}
