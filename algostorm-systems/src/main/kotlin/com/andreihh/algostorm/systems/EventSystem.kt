/*
 * Copyright (c) 2017  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andreihh.algostorm.systems

import com.andreihh.algostorm.core.ecs.System
import com.andreihh.algostorm.core.event.Event
import com.andreihh.algostorm.core.event.EventBus
import com.andreihh.algostorm.core.event.Request
import com.andreihh.algostorm.core.event.Subscriber

abstract class EventSystem : System(), Subscriber {
    companion object {
        const val EVENT_BUS: String = "EVENT_BUS"
    }

    private val eventBus: EventBus by context(EVENT_BUS)

    override fun onStart() {
        super.onStart()
        eventBus.subscribe(this)
    }

    override fun onStop() {
        super.onStop()
        eventBus.unsubscribe(this)
    }

    /**
     * Posts the given `event` to the event bus.
     *
     * @param event the event which should be posted
     */
    protected fun post(event: Event) {
        eventBus.post(event)
    }

    protected fun post(events: List<Event>) {
        events.forEach(eventBus::post)
    }

    protected fun post(vararg events: Event) {
        events.forEach(eventBus::post)
    }

    /**
     * Publishes the given `request` to the event bus and returns it's result.
     *
     * @param request the request which should be posted
     * @throws IllegalStateException if the request is not completed or if it is
     * completed multiple times
     */
    protected fun <T> request(request: Request<T>): T =
            eventBus.request(request)
}
