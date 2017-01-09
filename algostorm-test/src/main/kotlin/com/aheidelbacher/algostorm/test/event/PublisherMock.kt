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

import com.aheidelbacher.algostorm.event.Event
import com.aheidelbacher.algostorm.event.Publisher
import com.aheidelbacher.algostorm.event.Request

import java.util.LinkedList
import java.util.Queue

/**
 * A publisher that should be used for testing purposes.
 *
 * Every posted and published event is added to the back of a queue. Posted and
 * published events are saved in separate queues.
 */
class PublisherMock : Publisher {
    private val postedQueue: Queue<Event> = LinkedList()
    private val requestedQueue: Queue<Request<*>> = LinkedList()

    override fun post(event: Event) {
        postedQueue.add(event)
    }

    /**
     * Pops the event in the front of the posted events queue and checks if it
     * is equal to the given [event].
     *
     * @param event the expected event in the front of the posted events queue
     * @throws IllegalStateException if the given event doesn't correspond to
     * the front of the queue
     */
    fun checkPosted(event: Event) {
        val actualEvent = postedQueue.poll()
        val expectedEvent = event
        check(actualEvent == expectedEvent) {
            "The given event was not posted!\n" +
                    "Expected: $expectedEvent\n" +
                    "Actual: $actualEvent"
        }
    }

    /**
     * Checks if the posted events queue is empty.
     *
     * @throws IllegalStateException if the posted events queue is not empty
     */
    fun checkEmptyPostedQueue() {
        check(postedQueue.isEmpty()) {
            "There were more events posted!\nFound: ${postedQueue.toList()}"
        }
    }

    override fun <T> request(request: Request<T>): T = TODO()
}
