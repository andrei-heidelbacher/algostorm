/*
 * Copyright (c) 2017  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.andreihh.algostorm.systems.input

import com.andreihh.algostorm.core.drivers.input.Input
import com.andreihh.algostorm.core.drivers.input.InputDriver
import com.andreihh.algostorm.core.drivers.input.InputDriver.GestureInterpreter
import com.andreihh.algostorm.core.event.Event
import com.andreihh.algostorm.core.event.Subscribe
import com.andreihh.algostorm.systems.EventSystem

abstract class InputSystem : EventSystem(), GestureInterpreter {
    companion object {
        const val INPUT_DRIVER: String = "INPUT_DRIVER"
    }

    /** An event which signals that user input should be processed. */
    object HandleInput : Event

    private val inputDriver: InputDriver by context(INPUT_DRIVER)

    override fun onStart() {
        super.onStart()
        inputDriver.setGestureInterpreter(this)
    }

    override fun onStop() {
        super.onStop()
        inputDriver.setGestureInterpreter(null)
    }

    protected abstract fun onInput(input: Input)

    /**
     * Upon receiving a [HandleInput] event, the unprocessed input actions are
     * processed.
     *
     * @param event the input handling event.
     */
    @Suppress("unused_parameter")
    @Subscribe
    fun onHandleInput(event: HandleInput) {
        var input: Input?
        do {
            input = inputDriver.read()
            input?.let(this::onInput)
        } while (input != null)
    }
}
