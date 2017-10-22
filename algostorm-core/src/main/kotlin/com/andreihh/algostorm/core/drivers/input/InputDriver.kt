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

package com.andreihh.algostorm.core.drivers.input

import com.andreihh.algostorm.core.drivers.Driver

/** A driver that allows reading input events. */
interface InputDriver : Driver, InputSource, InputWriter {
    interface GestureInterpreter {
        fun onScroll(dx: Int, dy: Int): Input?

        fun onTouch(x: Int, y: Int): Input?
    }

    fun setGestureInterpreter(interpreter: GestureInterpreter?)
}
