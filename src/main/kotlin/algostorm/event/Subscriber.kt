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

import kotlin.reflect.KClass

/**
 * An [Event] handler that subscribes for a certain type of events ([topic]).
 *
 * @param T the type of events in which the subscriber is interested
 * @property topic the [KClass] object of the event type which defines the topic
 * @property handler the event handler which is called by the [notify] method when this subscriber
 * is notified of an event it subscribed for
 */
class Subscriber<T : Event>(val topic: KClass<T>, private val handler: (T) -> Unit) {
  /**
   * This method is called by the event bus to notify this subscriber of an [event].
   *
   * @param event the event for which the subscriber is notified
   */
  fun notify(event: Event) {
    if (event.javaClass.kotlin == topic) {
      @Suppress("UNCHECKED_CAST") handler(event as T)
    }
  }
}
