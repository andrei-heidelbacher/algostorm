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

package com.aheidelbacher.algostorm.core.drivers.client.input

/**
 * Raw user input interpreter.
 *
 * The listener might be notified of user input from a different thread. Thus,
 * implementations should be thread-safe.
 *
 * It is recommended to not access or modify game state, but simply to map raw
 * user input to user actions and let other systems perform these actions.
 *
 * The default implementations of the methods do nothing. Override the relevant
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
