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

package algostorm.input

/**
 * Thread-safe input socket which allows setting and retrieving actor inputs.
 *
 * This is a mutable and serializable object, however, it may be used in other
 * components, as the internal state of the source is transient. This class may
 * not be serialized alone, but it may be serialized as a member of another
 * class as long as the wrapper class is not generic.
 *
 * @param T the user input type
 */
class InputSocket<T> {
    @Transient private val lock = Any()

    /**
     * The last received input while the socket is enabled, or `null` if no
     * input has been received.
     *
     * After successfully retrieving a non-null input, it is reset to `null`.
     */
    @Transient var input: T? = null
        get() = synchronized(lock) {
            val value = field
            if (value != null) {
                field = null
            }
            value
        }
        set(value) {
            synchronized(lock) {
                if (isEnabled) {
                    field = value
                }
            }
        }

    /**
     * The enabled property of the socket.
     *
     * If the socket is disabled, no input can be received. Upon enabling or
     * disabling the socket, the last [input] is flushed and set to `null`.
     */
    @Transient var isEnabled: Boolean = false
        get() = synchronized(lock) { field }
        set(value) {
            synchronized(lock) {
                input = null
                field = value
            }
        }
}
