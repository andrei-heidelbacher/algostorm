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

package com.aheidelbacher.algostorm.engine.event

import com.aheidelbacher.algostorm.event.Event
import com.aheidelbacher.algostorm.event.Publisher

import java.util.LinkedList
import java.util.Queue

/**
 * A publisher that should be used for testing purposes.
 *
 * Every posted event is added to the back of an event queue.
 */
class PublisherMock : Publisher {
    private val queue: Queue<Event> = LinkedList()

    override fun <T : Event> post(event: T) {
        queue.add(event)
    }

    override fun <T : Event> publish(event: T) {
    }

    /**
     * Pops the event in the front of the queue and checks if it is equal to the
     * given [event].
     *
     * @param event the expected event in the front of the queue
     * @throws IllegalStateException if the given event doesn't correspond to
     * the front of the queue
     */
    fun verify(event: Event) {
        val actualEvent = queue.poll()
        val expectedEvent = event
        check(actualEvent == expectedEvent) {
            "The given event was not posted!\n" +
                    "Expected: $expectedEvent\n" +
                    "Actual: $actualEvent"
        }
    }

    /**
     * Checks if the event queue is empty.
     *
     * @throws IllegalStateException if the event queue is not empty
     */
    fun verifyEmpty() {
        check(queue.isEmpty()) {
            "There were more events posted!\nFound: ${queue.toList()}"
        }
    }
}
