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

package com.aheidelbacher.algostorm.event

/**
 * Provides functionality to post and publish events and notify subscribers.
 *
 * It should preserve the order of posted events (if an event A is posted before
 * an event B, then subscribers will be notified for A before they are notified
 * for B).
 */
interface Publisher {
    /**
     * Posts the given [event] and notifies all subscribers which subscribed at
     * this publisher for this `event` type and topic. This should be an
     * asynchronous method and return before the event was handled by its
     * subscribers.
     *
     * @param event the event that should be posted
     */
    fun post(event: Event): Unit

    /**
     * Posts each given event.
     *
     * @param events the events that should be posted
     */
    fun post(events: List<Event>) {
        events.forEach { post(it) }
    }

    /**
     * Posts for each given event.
     *
     * @param events the events that should be posted
     */
    fun post(vararg events: Event) {
        events.forEach { post(it) }
    }

    /**
     * Immediately publishes the given event and blocks until it was handled by
     * all subscribers. This is a synchronous method and may not respect the
     * order of other posted events.
     *
     * @param event the event that should be published
     */
    fun publish(event: Event): Unit

    /**
     * Publishes each given event.
     *
     * @param events the events that should be published
     */
    fun publish(events: List<Event>) {
        events.forEach { publish(it) }
    }

    /**
     * Publishes each given event.
     *
     * @param events the events that should be published
     */
    fun publish(vararg events: Event) {
        events.forEach { publish(it) }
    }
}
