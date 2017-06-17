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

import org.junit.After
import org.junit.Before
import org.junit.Test

import com.aheidelbacher.algostorm.test.event.EventMock
import com.aheidelbacher.algostorm.test.event.RequestMock
import com.aheidelbacher.algostorm.test.event.ServiceTest
import com.aheidelbacher.algostorm.test.event.SubscriberMock

import kotlin.test.assertEquals

class ServiceImplTest : ServiceTest() {
    class ServiceImpl : Service() {
        private var id = 1

        fun postNext() {
            post(EventMock(id++))
        }

        fun postNextList(count: Int = 1) {
            require(count > 0)
            val events = arrayListOf<Event>()
            repeat(count) {
                events.add(EventMock(id++))
            }
            post(events)
        }

        fun postNextVararg(count: Int = 1) {
            require(count > 0)
            val events = arrayListOf<Event>()
            repeat(count) {
                events.add(EventMock(id++))
            }
            post(*events.toTypedArray())
        }

        fun requestNext(): Int = request(RequestMock())
    }

    override val service  = ServiceImpl()
    private val subscriberMock = object : SubscriberMock() {
        private var id = 1

        @Subscribe fun onRequest(request: RequestMock) {
            request.complete(id++)
        }
    }

    @Before fun addSubscriberMock() {
        eventBus.subscribe(subscriberMock)
    }

    @Test fun testPost() {
        service.start(eventBus)
        service.postNext()
        service.stop()
        eventBus.publishPosts()
        subscriberMock.assertPosted(EventMock(1))
        subscriberMock.assertEmpty()
    }

    @Test(expected = IllegalStateException::class)
    fun testPostWhenNotRunningThrows() {
        service.postNext()
    }

    @Test fun testPostList() {
        service.start(eventBus)
        service.postNextList(count = 10)
        service.stop()
        eventBus.publishPosts()
        for (id in 1..10) {
            subscriberMock.assertPosted(EventMock(id))
        }
        subscriberMock.assertEmpty()
    }

    @Test(expected = IllegalStateException::class)
    fun testPostListWhenNotRunningThrows() {
        service.postNextList(count = 10)
    }

    @Test fun testPostVararg() {
        service.start(eventBus)
        service.postNextVararg(count = 10)
        service.stop()
        eventBus.publishPosts()
        for (id in 1..10) {
            subscriberMock.assertPosted(EventMock(id))
        }
        subscriberMock.assertEmpty()
    }

    @Test(expected = IllegalStateException::class)
    fun testPostVarargWhenNotRunningThrows() {
        service.postNextVararg(count = 10)
    }

    @Test fun testRequest() {
        service.start(eventBus)
        for (id in 1..10) {
            assertEquals(id, service.requestNext())
        }
        service.stop()
    }

    @Test(expected = IllegalStateException::class)
    fun testRequestWhenNotRunningThrows() {
        service.requestNext()
    }

    @After fun removeSubscriberMock() {
        eventBus.unsubscribe(subscriberMock)
    }
}
