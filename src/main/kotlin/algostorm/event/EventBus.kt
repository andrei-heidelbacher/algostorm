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
 * An event bus which allows a [Subscriber] to [subscribe] and unsubscribe from
 * certain topics through the returned [Subscription] and allows to [post] an
 * [Event] to the bus and notify its subscribers.
 */
interface EventBus : Publisher {
    /**
     * Registers the given [subscriber] and returns the associated subscription.
     *
     * @param subscriber the object that subscribes for events to this event
     * bus
     * @return the subscription which allows the subscriber to unsubscribe and
     * stop listening for events which are posted to this event bus
     * @throws IllegalArgumentException if the subscriber contains an annotated
     * event handler that does not conform to the [Subscribe] contract
     */
    fun subscribe(subscriber: Subscriber): Subscription

    /**
     * Blocks until all posted events have been handled by their subscribers.
     */
    fun publishPosts(): Unit
}
