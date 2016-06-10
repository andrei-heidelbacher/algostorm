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
import org.junit.Test

class SubscriberTest {
    data class EventMock(val eventId: Int) : Event

    @Test
    fun notifyWhenInterestedShouldHandleEqualEvent() {
        val postedEvent = EventMock(862)
        var handledEvent: Event? = null
        val subscriber = Subscriber(EventMock::class) { event ->
            handledEvent = event
        }
        subscriber.notify(postedEvent)
        assertEquals(postedEvent, handledEvent)
    }

    @Test
    fun notifyWhenNotInterestedShouldNotHandle() {
        var handled = false
        val subscriber = Subscriber(EventMock::class) { event ->
            handled = true
        }
        subscriber.notify(object : Event {})
        assertEquals(false, handled)
    }
}
