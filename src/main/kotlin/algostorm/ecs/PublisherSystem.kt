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

package algostorm.ecs

import algostorm.event.Event
import algostorm.event.EventBus
import algostorm.event.Publisher

/**
 * A system that may emit other events to the [eventBus] through the [Publisher] interface as a
 * means of communication with other systems.
 *
 * @property eventBus the event bus to which the system may post events
 */
abstract class PublisherSystem(private val eventBus: EventBus) : EntitySystem(), Publisher {
  final override fun post(event: Event) {
    eventBus.post(event)
  }
}
