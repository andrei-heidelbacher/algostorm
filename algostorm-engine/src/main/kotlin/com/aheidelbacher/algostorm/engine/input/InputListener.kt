package com.aheidelbacher.algostorm.engine.input

/**
 * Raw user input interpreter.
 *
 * The listener might be notified of user input from a different thread. Thus,
 * implementations should be thread-safe.
 *
 * It is recommended to not access or modify game state, but simply to map raw
 * user input to user actions and let other systems perform these actions.
 *
 * The default implementation of the methods does nothing. Override the relevant
 * methods.
 */
interface InputListener {
    /**
     * Notifies that the user touched the screen at the given coordinates.
     *
     * @param x the vertical coordinate in pixels of the touch location
     * @param y the horizontal coordinate in pixels of the touch location
     * (positive is down)
     */
    fun onTouch(x: Int, y: Int) {}

    /**
     * Notifies that the user scrolled the screen by the given amount.
     *
     * @param dx the vertical scrolling amount in pixels
     * @param dy the horizontal scrolling amount in pixels (positive is down)
     */
    fun onScroll(dx: Int, dy: Int) {}

    /**
     * Notifies that the user pressed the key with the given code.
     *
     * @param keyCode the code of the pressed key
     */
    fun onKey(keyCode: Int) {}
}
