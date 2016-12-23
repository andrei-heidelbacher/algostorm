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

package com.aheidelbacher.algostorm.test.engine.input

import com.aheidelbacher.algostorm.engine.input.InputListener
import java.util.LinkedList
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InputListenerMock : InputListener {
    private interface InputCall {
        data class Touch(val x: Int, val y: Int) : InputCall
        data class Scroll(val dx: Int, val dy: Int) : InputCall
        data class Key(val keyCode: Int) : InputCall
    }

    private val queue = LinkedList<InputCall>()

    override fun onTouch(x: Int, y: Int) {
        queue.add(InputCall.Touch(x, y))
    }

    override fun onScroll(dx: Int, dy: Int) {
        queue.add(InputCall.Scroll(dx, dy))
    }

    override fun onKey(keyCode: Int) {
        queue.add(InputCall.Key(keyCode))
    }

    fun assertTouch(x: Int, y: Int) {
        assertEquals(InputCall.Touch(x, y), queue.poll())
    }

    fun assertScroll(dx: Int, dy: Int) {
        assertEquals(InputCall.Scroll(dx, dy), queue.poll())
    }

    fun assertKey(keyCode: Int) {
        assertEquals(InputCall.Key(keyCode), queue.poll())
    }

    fun assertEmptyInputQueue() {
        assertTrue(queue.isEmpty())
    }
}
