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

package com.aheidelbacher.algostorm.android.test

import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.support.test.InstrumentationRegistry

import com.aheidelbacher.algostorm.android.AndroidAudioDriver

import org.junit.Test

import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AndroidAudioDriverTest {
    private val context = InstrumentationRegistry.getTargetContext()
    private val audioDriver = AndroidAudioDriver(context)
    private val audioManager = context.getSystemService(AUDIO_SERVICE)
            as AudioManager

    @Test fun testPlayMusic() {
        assertFalse(audioManager.isMusicActive)
        assertTrue(audioManager.isMusicActive)
    }
}
