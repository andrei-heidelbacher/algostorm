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

package com.aheidelbacher.algostorm.engine.input

import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber

/**
 * A system which handles user input.
 *
 * @param T the input type
 * @property inputReader the input source from which input is received. Input
 * will be read from the private engine thread and the provided implementation
 * should be thread-safe.
 */
abstract class AbstractInputSystem<in T : Any>(
        private val inputReader: InputReader<T>
) : Subscriber {
    /**
     * Handles the most recent input from the [inputReader].
     *
     * @param input the retrieved input
     */
    protected abstract fun handleInput(input: T): Unit

    /**
     * Upon receiving a [HandleInput] event, the [inputReader] is checked for
     * new input and the [handleInput] method is called.
     *
     * @param event the [HandleInput] event.
     */
    @Subscribe fun onHandleInput(event: HandleInput) {
        inputReader.readInput()?.let { handleInput(it) }
    }
}
