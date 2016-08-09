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

import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.LinkedList

/**
 * An asynchronous implementation of an [EventBus].
 *
 * The [post] method adds the event to the event queue and it will be processed
 * only when [publishPosts] is called.
 */
class EventQueue : EventBus {
    private fun Method.validateEventHandler() {
        require(Modifier.isFinal(modifiers)) { "$name is not final!" }
        require(returnType.name == "void") { "$name doesn't return Unit/void!" }
        require(parameterTypes.size == 1) {
            "$name doesn't have single parameter!"
        }
        require(Event::class.java.isAssignableFrom(parameterTypes[0])) {
            "$name doesn't receive a subtype of Event as a parameter!"
        }
    }

    private val subscribers = hashMapOf<Subscriber, List<Method>>()
    private val eventQueue = LinkedList<Event>()

    override fun subscribe(subscriber: Subscriber): Subscription {
        val handlers = subscriber.javaClass.methods.filter {
            it.isAnnotationPresent(Subscribe::class.java)
        }
        handlers.forEach { it.validateEventHandler() }
        subscribers[subscriber] = handlers
        return object : Subscription {
            private var isCancelled = false

            override fun unsubscribe() {
                check(!isCancelled) {
                    "Can't cancel the same subscription multiple times!"
                }
                subscribers.remove(subscriber)
                isCancelled = true
            }
        }
    }

    override fun <T : Event> post(event: T) {
        eventQueue.add(event)
    }

    override fun publishPosts() {
        while (eventQueue.isNotEmpty()) {
            val event = eventQueue.remove()
            for ((subscriber, handlers) in subscribers) {
                handlers.filter {
                    it.parameterTypes[0].isInstance(event)
                }.forEach { it.invoke(subscriber, event) }
            }
        }
    }
}
