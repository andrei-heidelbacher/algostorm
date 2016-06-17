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

import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.LinkedList
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.memberFunctions

/**
 * An asynchronous implementation of an [EventBus].
 *
 * The [post] method adds the event to the event queue and it will be processed
 * only when [publishPosts] is called.
 */
class EventQueue : EventBus {
    private val subscribers =
            hashMapOf<Subscriber, Map<Class<*>, List<Method>>>()
    private val eventQueue = LinkedList<Event>()

    private fun validateEventHandler(handler: Method) {
        val name = handler.name
        val returnsUnit = Unit.javaClass.isAssignableFrom(handler.returnType)
        val isPublic = Modifier.isPublic(handler.modifiers)
        val parameterTypes = handler.parameterTypes
        require(returnsUnit) { "$name doesn't return Unit!" }
        require(isPublic) { "$name is not public!" }
        require(parameterTypes.size == 1) {
            "$name receives more than one parameter!"
        }
        require(Event::class.java.isAssignableFrom(parameterTypes.single())) {
            "$name doesn't receive a subtype of Event!"
        }
    }

    private companion object {
        val <T : Any> T.kClass: KClass<T>
            get() = javaClass.kotlin

        fun KType.isSubtypeOf(superType: KClass<*>): Boolean =
                superType.java.isAssignableFrom(Class.forName(toString()))

        fun validateEventHandler(handler: KFunction<*>) {
            val name = handler.name
            val returnsUnit = handler.returnType.toString() == Unit.toString()
            val parameterTypes = handler.parameters.map { it.type }
            require(returnsUnit) { "$name doesn't return Unit!" }
            require(parameterTypes.size == 2) {
                "$name receives more than one parameter!"
            }
            require(parameterTypes[1].isSubtypeOf(Event::class)) {
                "$name doesn't receive a subtype of Event!"
            }
        }
    }

    override fun subscribe(subscriber: Subscriber): Subscription {
        subscriber.kClass.memberFunctions.filter { it.annotations.any { it.annotationClass == Subscribe::class } }.forEach { validateEventHandler(it) }
        val handlers = subscriber.javaClass.methods.filter {
            it.isAnnotationPresent(Subscribe::class.java)
        }
        handlers.forEach { validateEventHandler(it) }
        subscribers[subscriber] = handlers.groupBy {
            it.parameterTypes.single()
        }
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
                map[event.javaClass]?.forEach { handler ->
                    handler.invoke(subscriber, event)
                }
            }
        }
    }
}
