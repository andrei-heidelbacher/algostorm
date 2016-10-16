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
 * An event bus which allows a [Subscriber] to subscribe and unsubscribe from
 * certain topics and allows to post or publish an event to the bus and notify
 * its subscribers.
 */
interface EventBus : Publisher {
    /**
     * Registers the given [subscriber] to this event bus.
     *
     * @param subscriber the object that subscribes for events posted to this
     * event bus
     * @throws IllegalArgumentException if the subscriber contains an annotated
     * event handler that does not conform to the [Subscribe] contract. However,
     * if any non-public or static method is annotated, it will be ignored
     * instead of throwing an exception.
     */
    fun subscribe(subscriber: Subscriber): Unit

    /**
     * Unregisters the given [subscriber] from this event bus.
     *
     * @param subscriber the object that should be unsubscribed from events
     * posted to this event bus
     */
    fun unsubscribe(subscriber: Subscriber): Unit

    /**
     * Blocks until all posted events have been handled by their subscribers.
     */
    fun publishPosts(): Unit
}
