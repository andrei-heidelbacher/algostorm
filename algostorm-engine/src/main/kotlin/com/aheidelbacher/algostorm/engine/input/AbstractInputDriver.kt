package com.aheidelbacher.algostorm.engine.input

import java.util.concurrent.ConcurrentSkipListSet

/** A basic input driver implementation that handles notifying listeners. */
abstract class AbstractInputDriver : InputDriver {
    private val listeners = ConcurrentSkipListSet<InputListener>()

    /**
     * Sends the given notifications to all listeners.
     *
     * @param notifier the code that should notify a listener of a received
     * input event
     */
    protected fun notify(notifier: InputListener.() -> Unit) {
        listeners.forEach(notifier)
    }

    override fun addListener(listener: InputListener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: InputListener) {
        listeners.remove(listener)
    }

    /**
     * Unregisters all registered listeners.
     *
     * If this method is overridden, a super-call is mandatory in order to avoid
     * memory leaks.
     */
    override fun release() {
        listeners.clear()
    }
}
