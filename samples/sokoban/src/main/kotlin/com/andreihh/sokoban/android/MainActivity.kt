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

package com.andreihh.sokoban.android

import android.os.Bundle
import android.widget.Toast
import com.andreihh.algostorm.android.ClientActivity
import com.andreihh.algostorm.core.drivers.ui.UiListener
import com.andreihh.algostorm.core.drivers.ui.UiEvent
import com.andreihh.sokoban.R

class MainActivity : ClientActivity() {
    override val splashLayoutId: Int get() = R.layout.activity_main_splash
    override val clientLayoutId: Int get() = R.layout.activity_main_client
    override val surfaceViewId: Int get() = R.id.clientSurfaceView
    override val listener: UiListener get() = object : UiListener {
        override fun notify(event: UiEvent) {
            Toast.makeText(
                this@MainActivity,
                "100 frames elapsed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
