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

import com.aheidelbacher.algostorm.core.engine.input.InputListener
import com.aheidelbacher.algostorm.core.engine.input.InputSource
import com.aheidelbacher.algostorm.core.engine.input.PollingInputListener
import com.aheidelbacher.algostorm.core.event.Event
import com.aheidelbacher.algostorm.core.event.Subscribe
import com.aheidelbacher.algostorm.core.event.Subscriber

/** A system which handles user input. */
abstract class AbstractInputSystem(
        private val inputSource: InputSource
) : Subscriber, InputListener {
    /** An event which signals that user input should be processed. */
    object HandleInput : Event

    private val pollingListener = PollingInputListener().apply {
        inputSource.addListener(this@apply)
    }

    /**
     * Upon receiving a [HandleInput] event, the most recent user input is
     * processed.
     *
     * @param event the [HandleInput] event.
     */
    @Suppress("unused_parameter")
    @Subscribe fun onHandleInput(event: HandleInput) {
        pollingListener.pollMostRecent(this)
    }
}
