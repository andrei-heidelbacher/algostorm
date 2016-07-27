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

package com.aheidelbacher.algostorm.input

/**
 * Thread-safe input socket which allows setting and retrieving inputs.
 *
 * @param T the user lastInput type
 */
class InputSocket<T : Any> : InputReader<T>, InputWriter<T> {
    @Transient private val lock = Any()

    /**
     * The last received input, or `null` if no input has been received.
     *
     * After successfully retrieving a non-null input, it is reset to `null`.
     */
    private var lastInput: T? = null

    override fun readInput(): T? = synchronized(lock) {
        val value = lastInput
        lastInput = null
        value
    }

    override fun writeInput(input: T?) {
        synchronized(lock) {
            lastInput = input
        }
    }
}
