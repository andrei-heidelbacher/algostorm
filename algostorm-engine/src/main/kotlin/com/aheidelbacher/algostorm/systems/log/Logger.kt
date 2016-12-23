/*
 * Copyright (c) 2016  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
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

/**
 * An object which provides logging facilities.
 */
interface Logger {
    companion object {
        /**
         * Returns a logger that writes all filtered events to [System.out]. If
         * no filter is given, all events are logged.
         *
         * @param eventFilter the filter which should return `true` for all
         * logged events
         * @return the system console logger
         */
        operator fun invoke(eventFilter: ((Event) -> Boolean)? = null): Logger =
                object : Logger {
                    override fun log(event: Event) {
                        if (eventFilter?.invoke(event) ?: true) {
                            println(event)
                        }
                    }
                }
    }

    /**
     * Logs the given `event`.
     *
     * @param event the event to be logged
     */
    fun log(event: Event): Unit
}
