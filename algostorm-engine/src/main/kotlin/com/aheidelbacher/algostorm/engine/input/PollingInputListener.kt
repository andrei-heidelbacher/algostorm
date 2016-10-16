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
