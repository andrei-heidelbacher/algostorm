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

package algostorm.engine

import algostorm.event.Event
import algostorm.event.EventBus
import algostorm.event.Subscriber
import algostorm.event.Subscription

import java.util.LinkedList

import kotlin.reflect.KClass

/**
 * An asynchronous implementation of an [EventBus].
 *
 * The [post] method adds the event to the event queue and it will be processed
 * only when [publishPosts] is called.
 */
class EventQueue : EventBus {
    private val subscribers =
            hashMapOf<KClass<out Event>, MutableSet<Subscriber<*>>>()
    private val eventQueue = LinkedList<Event>()

    override fun subscribe(subscriber: Subscriber<*>): Subscription {
        subscribers.getOrPut(subscriber.topic) { linkedSetOf() }.add(subscriber)
        return object : Subscription {
            private var isCancelled = false

            override fun unsubscribe() {
                check(!isCancelled) {
                    "Can't cancel the same subscription multiple times!"
                }
                isCancelled = true
                subscribers[subscriber.topic]?.remove(subscriber)
            }
        }
    }

    override fun post(event: Event) {
        eventQueue.add(event)
    }

    override fun publishPosts() {
        do {
            val event = eventQueue.poll()
            if (event != null) {
                subscribers[event.javaClass.kotlin].orEmpty().forEach {
                    it.notify(event)
                }
            }
        } while (event != null)
    }
}
