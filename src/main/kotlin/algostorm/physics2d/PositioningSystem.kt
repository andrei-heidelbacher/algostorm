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
import algostorm.ecs.PublisherSystem
import algostorm.event.EventBus
import algostorm.event.Subscriber
import algostorm.physics2d.Box.Companion.box

/**
 * A system that publishes [Translated] events as a reaction to successful [TranslateIntent] events.
 *
 * It updates the [Box] component of a given entity and publishes the `Translate` event if there are
 * no other entities which contain the [Rigid] component and have overlapping `Box` components at
 * the destination location.
 */
class PositioningSystem(
    private val entityManager: MutableEntityManager,
    eventBus: EventBus
) : PublisherSystem(eventBus) {
  private val translateIntentHandler = Subscriber(TranslateIntent::class) { event ->
    val translatedEntity = entityManager[event.entityId] ?: error(
        "Translating non-existent entity!"
    )
    val newBox = translatedEntity.box?.translate(event.dx, event.dy) ?: error(
        "Translating an entity without a location!"
    )
    val isBlocked = entityManager
        .getEntitiesWithComponentTypes(Rigid::class, Box::class)
        .any { entity -> entity != translatedEntity && entity.box?.overlaps(newBox) ?: false }
    if (!isBlocked) {
      translatedEntity.set(newBox)
      post(Translated(event.entityId, event.dx, event.dy))
    }
  }

  /**
   * This system handles [TranslateIntent] events.
   */
  override val handlers: List<Subscriber<*>> = listOf(translateIntentHandler)
}
