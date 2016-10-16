package com.aheidelbacher.algostorm.engine.input

import com.aheidelbacher.algostorm.engine.driver.Driver

/** A driver that provides listening for input events. */
interface InputDriver : Driver {
    fun addListener(listener: InputListener): Unit

    fun removeListener(listener: InputListener): Unit
}
