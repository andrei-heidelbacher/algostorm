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

package com.aheidelbacher.algostorm.engine.input

import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

/** A basic input driver implementation that handles notifying listeners. */
abstract class AbstractInputDriver : InputDriver {
    private val listeners = Collections.newSetFromMap(
            ConcurrentHashMap<InputListener, Boolean>()
    )

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
