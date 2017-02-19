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

import com.aheidelbacher.algostorm.core.event.Event
import com.aheidelbacher.algostorm.core.event.Subscribe
import com.aheidelbacher.algostorm.core.event.Subscriber

import java.util.LinkedList
import java.util.Queue

import kotlin.test.assertEquals
import kotlin.test.assertTrue

open class SubscriberMock : Subscriber {
    private val eventQueue: Queue<Event> = LinkedList()

    @Subscribe fun onEvent(event: Event) {
        eventQueue.add(event)
    }

    fun assertPosted(event: Event) {
        assertEquals(event, eventQueue.poll())
    }

    fun assertEmpty() {
        assertTrue(eventQueue.isEmpty())
    }
}
