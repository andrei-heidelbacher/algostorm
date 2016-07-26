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
 * Allows reading input.
 *
 * @param T the input type
 */
interface InputReader<out T : Any> {
    /**
     * Retrieves the most recent input and resets the last input to `null`.
     *
     * @return the most recent input, or `null` if there is not input to read
     */
    fun readInput(): T?
}
