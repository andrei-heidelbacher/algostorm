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

import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

/**
 * An abstract test class for an [EventBus].
 *
 * In order to test common functionality to all event buses, you may implement
 * this class and provide a concrete event bus instance to test.
 *
 * @property eventBus the event bus instance that should be tested
 */
@Ignore
abstract class EventBusTest(protected val eventBus: EventBus) {
    @Test
    fun publishPostsShouldNotifySubscribers() {
        val postedEvent = EventMock(5)
        var handledEvent: EventMock? = null
        val subscriber = object : Subscriber {
            @Subscribe fun handleEventMock(event: EventMock) {
                handledEvent = event
            }
        }
        eventBus.subscribe(subscriber)
        eventBus.post(postedEvent)
        eventBus.publishPosts()
        assertEquals(postedEvent, handledEvent)
    }
}
