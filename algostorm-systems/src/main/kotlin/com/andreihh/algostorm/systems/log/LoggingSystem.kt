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

package com.andreihh.algostorm.systems.log

import com.andreihh.algostorm.core.event.Event
import com.andreihh.algostorm.core.event.Subscribe
import com.andreihh.algostorm.systems.EventSystem

/**
 * A system which captures all events and logs them.
 *
 * @property logger the logger used to record the messages. It will be called
 * from the private engine thread and the provided implementation should be
 * thread-safe.
 */
class LoggingSystem : EventSystem() {
    companion object {
        const val LOGGER: String = "LOGGER"
    }

    private val logger: Logger by context(LOGGER)

    /**
     * Logs the string representation of the given `event`.
     *
     * @param event the event which should be logged
     */
    @Subscribe
    fun onEvent(event: Event) {
        logger.log(event)
    }
}
