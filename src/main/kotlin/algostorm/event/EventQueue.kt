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

import java.lang.reflect.Modifier
import java.util.LinkedList

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.defaultType
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.memberFunctions

/**
 * An asynchronous implementation of an [EventBus].
 *
 * The [post] method adds the event to the event queue and it will be processed
 * only when [publishPosts] is called.
 */
class EventQueue : EventBus {
    private companion object {
        val <T : Any> T.kClass: KClass<T>
            get() = javaClass.kotlin

        val KType.kClass: KClass<*>
            get() = Class.forName(toString()).kotlin

        fun KClass<*>.isAssignableFrom(cls: KClass<*>): Boolean =
                java.isAssignableFrom(cls.java)

        fun validateEventHandler(handler: KFunction<*>) {
            val name = handler.name
            val returnsUnit = handler.returnType == Unit::class.defaultType
            val parameters = handler.parameters
            require(returnsUnit) { "$name doesn't return Unit!" }
            require(parameters.size == 2) {
                "$name receives more than one parameter!"
            }
            require(Event::class.isAssignableFrom(parameters[1].type.kClass)) {
                "$name doesn't receive a subtype of Event!"
            }
        }
    }

    private val subscribers =
            hashMapOf<Subscriber, Map<KType, List<KFunction<*>>>>()
    private val eventQueue = LinkedList<Event>()

    override fun subscribe(subscriber: Subscriber): Subscription {
        val handlers = subscriber.kClass.memberFunctions.filter { function ->
            Subscribe::class in function.annotations.map { it.annotationClass }
        }
        handlers.forEach { validateEventHandler(it) }
        handlers.forEach { it.isAccessible = true }
        subscribers[subscriber] = handlers.groupBy { it.parameters[1].type }
        return object : Subscription {
            private var isCancelled = false

            override fun unsubscribe() {
                check(!isCancelled) {
                    "Can't cancel the same subscription multiple times!"
                }
                isCancelled = true
                subscribers.remove(subscriber)
            }
        }
    }

    override fun <T : Event> post(event: T) {
        eventQueue.add(event)
    }

    override fun publishPosts() {
        while (eventQueue.isNotEmpty()) {
            val event = eventQueue.remove()
            subscribers.forEach { entry ->
                val (subscriber, map) = entry
                map[event.kClass.defaultType]?.forEach { handler ->
                    handler.call(subscriber, event)
                }
            }
        }
    }
}
