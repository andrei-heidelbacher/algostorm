package com.aheidelbacher.algostorm.test.engine.input

import com.aheidelbacher.algostorm.engine.input.AbstractInputDriver

class InputDriverMock : AbstractInputDriver() {
    fun touch(x: Int, y: Int) {
        notify { onTouch(x, y) }
    }

    fun key(keyCode: Int) {
        notify { onKey(keyCode) }
    }

    fun scroll(dx: Int, dy: Int) {
        notify { onScroll(dx, dy) }
    }
}
