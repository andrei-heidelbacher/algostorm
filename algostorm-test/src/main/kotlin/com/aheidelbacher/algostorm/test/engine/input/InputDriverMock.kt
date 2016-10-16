package com.aheidelbacher.algostorm.test.engine.input

import com.aheidelbacher.algostorm.engine.input.InputDriver
import com.aheidelbacher.algostorm.engine.input.InputListener

class InputDriverMock : InputDriver {
    private val listeners = mutableListOf<InputListener>()

    override fun addListener(listener: InputListener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: InputListener) {
        listeners.remove(listener)
    }

    override fun release() {
        listeners.clear()
    }

    fun touch(x: Int, y: Int) {
        listeners.forEach { it.onTouch(x, y) }
    }

    fun key(keyCode: Int) {
        listeners.forEach { it.onKey(keyCode) }
    }

    fun scroll(dx: Int, dy: Int) {
        listeners.forEach { it.onScroll(dx, dy) }
    }
}
