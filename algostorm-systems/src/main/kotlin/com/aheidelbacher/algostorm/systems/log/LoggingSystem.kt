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

package com.aheidelbacher.algostorm.systems.log

import com.aheidelbacher.algostorm.event.Event
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber

/**
 * A system which captures all events and logs them.
 *
 * @property logger the logger used to record the messages. It will be called
 * from the private engine thread and the provided implementation should be
 * thread-safe.
 */
class LoggingSystem(private val logger: Logger) : Subscriber {
    /**
     * Logs the string representation of the given `event`.
     *
     * @param event the event which should be logged
     */
    @Subscribe fun onEvent(event: Event) {
        logger.log(event)
    }
}
