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

package com.aheidelbacher.algostorm.systems.input

import com.aheidelbacher.algostorm.core.drivers.client.input.InputSource
import com.aheidelbacher.algostorm.core.event.Event
import com.aheidelbacher.algostorm.core.event.Service
import com.aheidelbacher.algostorm.core.event.Subscribe

/** A system which handles user input. */
class InputDispatchingService(
        private val inputSource: InputSource
) : Service() {
    /** An event which signals that user input should be processed. */
    object HandleInput : Event

    /**
     * Upon receiving a [HandleInput] event, the unprocessed input actions are
     * processed.
     *
     * @param event the input handling event.
     */
    @Suppress("unused_parameter")
    @Subscribe fun onHandleInput(event: HandleInput) {
        var input = inputSource.read()
        while (input != null) {
            if (input is InputEvent) {
                post(input)
            }
            input = inputSource.read()
        }
    }
}