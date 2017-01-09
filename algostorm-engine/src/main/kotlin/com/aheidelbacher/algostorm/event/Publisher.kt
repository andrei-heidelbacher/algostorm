/*
 * Copyright 2017 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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
 * Provides functionality to [post] events, [request] services and notify
 * registered subscribers.
 *
 * It should preserve the order of posted events (if an event A is posted before
 * an event B, then subscribers will be notified for A before they are notified
 * for B).
 */
interface Publisher {
    /**
     * Posts the given `event` and notifies all subscribers.
     *
     * This should be an asynchronous method and return before the `event` was
     * handled by its subscribers.
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

    fun post(vararg events: Event) {
        events.forEach { post(it) }
    }

    /**
     * Immediately publishes the given `request` and returns its result.
     *
     * @param T the result type
     * @param request the request which should be completed
     * @return the result with which the `request` was completed
     * @throws IllegalStateException if the `request` was not completed or if it
     * was completed more than once
     */
    fun <T> request(request: Request<T>): T
}
