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

package com.aheidelbacher.algostorm.core.event

/** An abstract service which implements a set of operations. */
abstract class Service : Subscriber {
    private var eventBus: EventBus? = null

    /**
     * Subscribes this service to the given `eventBus` and calls [onStart].
     *
     * @throws IllegalStateException if this service is already running
     */
    fun start(eventBus: EventBus) {
        check(this.eventBus == null) { "$this is already running!" }
        eventBus.subscribe(this)
        onStart()
        this.eventBus = eventBus
    }

    /**
     * Unsubscribes this service from the event bus and calls [onStop].
     *
     * @throws IllegalStateException if this service is not running
     */
    fun stop() {
        eventBus?.unsubscribe(this) ?: error("$this is not running!")
        onStop()
        eventBus = null
    }

    /** Performs start-up initialization. */
    protected open fun onStart() {}

    /**
     * Posts the given `event` to the event bus.
     *
     * @param event the event which should be posted
     * @throws IllegalStateException if this service is not running
     */
    protected fun post(event: Event) {
        checkNotNull(eventBus).post(event)
    }

    protected fun post(events: List<Event>) {
        events.forEach { checkNotNull(eventBus).post(it) }
    }

    protected fun post(vararg events: Event) {
        events.forEach { checkNotNull(eventBus).post(it) }
    }

    /**
     * Publishes the given `request` to the event bus and returns it's result.
     *
     * @param request the request which should be posted
     * @throws IllegalStateException if this service is not running or if the
     * request is not completed or if it is completed multiple times
     */
    protected fun <T> request(request: Request<T>): T =
            checkNotNull(eventBus).request(request)

    /** Releases the resources acquired in [onStart]. */
    protected open fun onStop() {}
}
