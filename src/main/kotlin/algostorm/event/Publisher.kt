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

package algostorm.event

/**
 * Provides posting functionality to an associated [EventBus].
 */
interface Publisher {
  /**
   * Posts the given [event] to the associated event bus.
   *
   * @param event the event that should be posted
   */
  fun post(event: Event): Unit

  /**
   * Calls [post] for each given event.
   *
   * @param events the events that should be posted
   */
  fun postAll(vararg events: Event) {
    events.forEach { event -> post(event) }
  }
}
