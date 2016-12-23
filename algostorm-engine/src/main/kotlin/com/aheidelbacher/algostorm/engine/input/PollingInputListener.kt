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

package com.aheidelbacher.algostorm.engine.input

import java.util.concurrent.ConcurrentLinkedQueue

/** A thread-safe implementation of a listener that buffers received inputs. */
class PollingInputListener : InputListener {
    private sealed class Input {
        class Touch(val x: Int, val y: Int) : Input()
        class Key(val keyCode: Int) : Input()
        class Scroll(val dx: Int, val dy: Int) : Input()
    }

    private val inputs = ConcurrentLinkedQueue<Input>()

    private fun notify(listener: InputListener, input: Input) {
        when (input) {
            is Input.Touch -> listener.onTouch(input.x, input.y)
            is Input.Key -> listener.onKey(input.keyCode)
            is Input.Scroll -> listener.onScroll(input.dx, input.dy)
        }
    }

    override fun onTouch(x: Int, y: Int) {
        inputs.add(Input.Touch(x, y))
    }

    override fun onKey(keyCode: Int) {
        inputs.add(Input.Key(keyCode))
    }

    override fun onScroll(dx: Int, dy: Int) {
        inputs.add(Input.Scroll(dx, dy))
    }

    /**
     * Forwards to the given listener the oldest received input, if any, and
     * returns whether there was any input processed.
     *
     * The oldest input is discarded.
     *
     * @param listener the listener to which input is forwarded
     * @return `true` if there was a processed user input, `false` otherwise
     */
    fun poll(listener: InputListener): Boolean = inputs.poll()?.let { input ->
        notify(listener, input)
        true
    } ?: false

    /**
     * Forwards to the given listener the most recent input received, if any.
     *
     * All previous inputs are discarded.
     *
     * @param listener the listener to which input is forwarded
     */
    fun pollMostRecent(listener: InputListener) {
        var input: Input? = null
        while (inputs.isNotEmpty()) {
            input = inputs.poll()
        }
        if (input != null) {
            notify(listener, input)
        }
    }
}
