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
 * An event bus which allows a [Subscriber] to [subscribe] and unsubscribe from certain topics
 * through the returned [Subscription] and allows to [post] an [Event] to the bus and notify its
 * subscribers.
 *
 * It should preserve the order of submitted events (if an event A is posted before an event B, then
 * subscribers will be notified for A before they are notified for B).
 */
interface EventBus {
  /**
   * Registers the given [subscriber] to the specified topic and returns the subscription.
   *
   * @param subscriber the object that subscribes for events to this event bus.
   * @return the subscription which allows the subscriber to unsubscribe and stop listening for
   * events of the specified topic which are posted to this event bus
   */
  fun subscribe(subscriber: Subscriber<*>): Subscription

  /**
   * Posts the given [event] to this event bus.
   *
   * @param event the event that should be posted
   */
  fun post(event: Event): Unit

  /**
   * Calls [post] for each given event.
   *
   * @param events the events that should be posted
   */
  fun post(events: List<Event>) {
    events.forEach { event -> post(event) }
  }

  /**
   * Calls [post] for each given event.
   *
   * @param events the events that should be posted
   */
  fun post(vararg events: Event) {
    events.forEach { event -> post(event) }
  }

  /**
   * Blocks until all other events in this event bus have been handled by their subscribers.
   */
  fun publishAll(): Unit
}
