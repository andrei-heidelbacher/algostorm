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
import com.andreihh.algostorm.core.drivers.ui.UiListener
import com.andreihh.algostorm.core.drivers.ui.UiDriver
import com.andreihh.algostorm.core.drivers.ui.UiEvent

class AndroidUiDriver(context: Context) : UiDriver {
    private val uiThreadHandler = Handler(Looper.getMainLooper())
    private var listener: UiListener? = null

    @Suppress("unchecked_cast")
    override fun setListener(listener: UiListener) {
        this.listener = listener
    }

    override fun notify(event: UiEvent) {
        val notifiedListener = listener ?: return
        uiThreadHandler.post {
            notifiedListener.notify(event)
        }
    }

    override fun release() {
        listener = null
    }
}
