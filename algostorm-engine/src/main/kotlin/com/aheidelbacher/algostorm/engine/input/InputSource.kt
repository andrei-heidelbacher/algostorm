package com.aheidelbacher.algostorm.engine.input

/** A source of input events. */
interface InputSource {
    /**
     * Registers the given listener to input events.
     *
     * @param listener the listener that should be registered to input events
     */
    fun addListener(listener: InputListener): Unit

    /**
     * Unregisters the given listener from input events.
     *
     * @param listener the listener that should be unregistered from input
     * events
     */
    fun removeListener(listener: InputListener): Unit
}
