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

package com.aheidelbacher.algostorm.test.event

import org.junit.Ignore
import org.junit.Test

import com.aheidelbacher.algostorm.core.event.Event
import com.aheidelbacher.algostorm.core.event.EventBus
import com.aheidelbacher.algostorm.core.event.Request
import com.aheidelbacher.algostorm.core.event.Subscribe
import com.aheidelbacher.algostorm.core.event.Subscriber

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail

/**
 * An abstract test class for an [EventBus].
 *
 * In order to test common functionality to all event buses, you may implement
 * this class and provide a concrete event bus instance to test.
 *
 * @property eventBus the event bus instance that should be tested
 */
@Ignore
abstract class EventBusTest {
    protected abstract val eventBus: EventBus

    @Test fun testPublishPostsShouldNotifySubscribersInCorrectOrder() {
        val postedEvents = listOf<Event>(EventMock(5), EventMock(3))
        val handledEvents = mutableListOf<Event>()
        val subscriber = object : Subscriber {
            @Suppress("unused", "unused_parameter")
            @Subscribe fun handleEventMock(event: EventMock) {
                handledEvents.add(event)
            }
        }

        eventBus.subscribe(subscriber)
        postedEvents.forEach { eventBus.post(it) }
        eventBus.publishPosts()
        eventBus.unsubscribe(subscriber)
        assertEquals(postedEvents, handledEvents)
    }

    @Test fun testPublishPostsShouldNotifyCorrectSubscribers() {
        data class HandledEvent(val id: Int) : Event
        data class UnhandledEvent(val id: Int) : Event

        val postedEvents = listOf(EventMock(5), HandledEvent(3))
        val handledEvents = mutableListOf<Event>()
        val subscriber = object : Subscriber {
            @Suppress("unused", "unused_parameter")
            @Subscribe fun handleEventMock(event: EventMock) {
                handledEvents.add(event)
            }

            @Suppress("unused", "unused_parameter")
            @Subscribe fun handleOtherEvent(event: HandledEvent) {
                handledEvents.add(event)
            }

            @Suppress("unused", "unused_parameter")
            @Subscribe fun handleAnotherEvent(event: UnhandledEvent) {
                fail()
            }
        }

        eventBus.subscribe(subscriber)
        postedEvents.forEach { eventBus.post(it) }
        eventBus.publishPosts()
        eventBus.unsubscribe(subscriber)
        assertEquals(postedEvents, handledEvents)
    }

    @Test fun testPublishPostsAfterUnsubscribeShouldNotNotifySubscriber() {
        val subscriber = object : Subscriber {
            @Suppress("unused", "unused_parameter")
            @Subscribe fun handleEventMock(event: EventMock) {
                fail()
            }
        }

        eventBus.subscribe(subscriber)
        eventBus.unsubscribe(subscriber)
        eventBus.post(EventMock(1))
        eventBus.publishPosts()
    }

    @Test fun testRequestShouldBeCompleted() {
        val publishedRequest = RequestMock()
        val id = 132
        val subscriber = object : Subscriber {
            @Suppress("unused", "unused_parameter")
            @Subscribe fun handleRequestMock(request: RequestMock) {
                request.complete(id)
            }
        }

        assertFalse(publishedRequest.isCompleted)
        eventBus.subscribe(subscriber)
        assertEquals(id, eventBus.request(publishedRequest))
        eventBus.unsubscribe(subscriber)
        assertTrue(publishedRequest.isCompleted)
    }

    @Test fun testRequestShouldNotifyCorrectSubscriber() {
        class UnhandledRequest : Request<Unit>()

        val publishedRequest = RequestMock()
        val id = 132
        val subscriber = object : Subscriber {
            @Suppress("unused", "unused_parameter")
            @Subscribe fun handleRequestMock(request: RequestMock) {
                request.complete(id)
            }

            @Suppress("unused", "unused_parameter")
            @Subscribe fun handleOtherRequest(request: UnhandledRequest) {
                fail()
            }
        }

        assertFalse(publishedRequest.isCompleted)
        eventBus.subscribe(subscriber)
        assertEquals(id, eventBus.request(publishedRequest))
        eventBus.unsubscribe(subscriber)
        assertTrue(publishedRequest.isCompleted)
    }

    @Test(expected = IllegalStateException::class)
    fun testRequestAfterUnsubscribeShouldThrow() {
        val publishedRequest = RequestMock()
        val id = 132
        val subscriber = object : Subscriber {
            @Suppress("unused", "unused_parameter")
            @Subscribe fun handleRequestMock(request: RequestMock) {
                request.complete(id)
            }
        }

        assertFalse(publishedRequest.isCompleted)
        eventBus.subscribe(subscriber)
        eventBus.unsubscribe(subscriber)
        eventBus.request(publishedRequest)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSubscribeMultipleParametersReceivedShouldThrow() {
        eventBus.subscribe(object : Subscriber {
            @Suppress("unused", "unused_parameter")
            @Subscribe fun handleEventMock(event: EventMock, other: Any) {}
        })
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSubscribeNonEventShouldThrow() {
        eventBus.subscribe(object : Subscriber {
            @Suppress("unused", "unused_parameter")
            @Subscribe fun handleNonEvent(any: Any) {}
        })
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSubscribeReturningNonVoidShouldThrow() {
        eventBus.subscribe(object : Subscriber {
            @Suppress("unused", "unused_parameter")
            @Subscribe fun handleEventMock(event: EventMock): Any = event
        })
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSubscribeNonFinalShouldThrow() {
        open class OpenSubscriber : Subscriber {
            @Suppress("unused", "unused_parameter")
            @Subscribe open fun handleEventMock(event: EventMock) {}
        }

        eventBus.subscribe(OpenSubscriber())
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSubscribeGenericHandlerShouldThrow() {
        eventBus.subscribe(object : Subscriber {
            @Suppress("unused", "unused_parameter")
            @Subscribe fun <T : Event> handleGeneric(event: T) {}
        })
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSubscribeGenericRequestShouldThrow() {
        eventBus.subscribe(object : Subscriber {
            @Suppress("unused", "unused_parameter")
            @Subscribe fun handleRequest(request: Request<*>) {}
        })
    }

    @Test fun testProtectedHandlerShouldBeIgnored() {
        val subscriber = object : Subscriber {
            @Suppress("unused", "unused_parameter")
            @Subscribe protected fun handleEventMock(event: EventMock) {
                fail()
            }
        }

        eventBus.subscribe(subscriber)
        eventBus.post(EventMock(1))
        eventBus.publishPosts()
        eventBus.unsubscribe(subscriber)
    }

    @Test fun testPrivateHandlerShouldBeIgnored() {
        val subscriber = object : Subscriber {
            @Suppress("unused", "unused_parameter")
            @Subscribe private fun handleEventMock(event: EventMock) {
                fail()
            }
        }

        eventBus.subscribe(subscriber)
        eventBus.post(EventMock(1))
        eventBus.publishPosts()
        eventBus.unsubscribe(subscriber)
    }
}
