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
 * Returns a default implementation of an event bus.
 *
 * @return the event bus
 */
fun eventBusOf(): EventBus = EventBusImpl()

private class EventBusImpl : EventBus {
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

    private val subscribers =
            hashMapOf<Subscriber, Array<Pair<Method, Class<*>>>>()
    private val eventQueue = LinkedList<Event>()

    override fun subscribe(subscriber: Subscriber) {
        val handlers = subscriber.javaClass.methods.filter {
            it.isAnnotationPresent(Subscribe::class.java)
        }
        handlers.forEach { it.validateEventHandler() }
        subscribers[subscriber] = handlers.map {
            it to it.parameterTypes[0]
        }.toTypedArray()
    }

    override fun unsubscribe(subscriber: Subscriber) {
        subscribers.remove(subscriber)
    }

    override fun post(event: Event) {
        eventQueue.add(event)
    }

    override fun publish(event: Event) {
        for ((subscriber, handlers) in subscribers) {
            for ((handler, parameterType) in handlers) {
                if (parameterType.isInstance(event)) {
                    handler.invoke(subscriber, event)
                }
            }
        }
    }

    override fun publishPosts() {
        while (eventQueue.isNotEmpty()) {
            publish(eventQueue.remove())
        }
    }
}
