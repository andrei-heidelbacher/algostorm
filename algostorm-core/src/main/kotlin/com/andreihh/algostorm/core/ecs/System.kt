/*
 * Copyright (c) 2017  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.andreihh.algostorm.core.ecs

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.cast

abstract class System {
    companion object {
        const val ENTITY_POOL: String = "ENTITY_POOL"
    }

    private lateinit var context: Map<String, Any?>
    private var isInitialized = false
    private var isRunning = false
    private var onStartSuperCalled = false
    private var onStopSuperCalled = false

    protected fun <T : Any> context(
            key: String,
            type: KClass<T>
    ): ReadOnlyProperty<Any?, T> = object : ReadOnlyProperty<Any?, T> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T =
                type.cast(context[key])
    }

    protected inline fun <reified T : Any> context(
            key: String
    ): ReadOnlyProperty<Any?, T> = context(key, T::class)

    protected open fun onStart() {
        onStartSuperCalled = true
    }

    protected open fun onStop() {
        onStopSuperCalled = true
    }

    fun initialize(context: Map<String, Any?>) {
        require(!isInitialized) { "'$this' is already initialized!" }
        this.context = context
        isInitialized = true
    }

    fun start() {
        require(!isRunning) { "'$this' is already running!" }
        isRunning = true
        onStartSuperCalled = false
        onStart()
        check(onStartSuperCalled) { "'$this' didn't super call 'onStart'!" }
        onStartSuperCalled = false
    }

    fun stop() {
        require(isRunning) { "'$this' isn't running!" }
        isRunning = false
        onStopSuperCalled = false
        onStop()
        check(onStopSuperCalled) { "'$this' didn't super call 'onStop'!" }
        onStopSuperCalled = false
    }
}
