/*
 * Copyright 2018 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.andreihh.algostorm.android

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.andreihh.algostorm.core.drivers.ui.Listener
import com.andreihh.algostorm.core.drivers.ui.UiDriver
import kotlin.reflect.KClass

class AndroidUiDriver(context: Context) : UiDriver {
    private val listeners = hashMapOf<KClass<*>, ArrayList<Listener<Any>>>()
    private val uiThreadHandler = Handler(Looper.getMainLooper())

    @Suppress("unchecked_cast")
    override fun <T : Any> addListener(type: KClass<T>, listener: Listener<T>) {
        if (type !in listeners) {
            listeners[type] = arrayListOf()
        }
        listeners.getValue(type) += listener as Listener<Any>
    }

    override fun <T : Any> notify(event: T) {
        val notifiedListeners = listeners[event::class]?.toList() ?: emptyList()
        uiThreadHandler.post {
            for (listener in notifiedListeners) {
                listener.invoke(event)
            }
        }
    }

    override fun release() {
        listeners.clear()
    }
}
