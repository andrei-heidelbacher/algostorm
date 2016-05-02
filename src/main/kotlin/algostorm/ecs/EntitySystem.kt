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

import algostorm.event.Subscriber

/**
 * An object that implements the behaviour of a core concept of the game.
 *
 * All processing occurs when it is notified for an event one of its [handlers] subscribed for.
 */
abstract class EntitySystem {
  /**
   * The event handlers of the system.
   */
  abstract val handlers: List<Subscriber<*>>
}
