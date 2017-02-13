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

package com.aheidelbacher.algostorm.desktop

import com.aheidelbacher.algostorm.core.engine.audio.AudioDriver
import com.aheidelbacher.algostorm.core.engine.driver.Resource

class DesktopAudioDriver : AudioDriver {
    override fun loadMusic(resource: Resource) {
    }

    override fun playMusic(resource: Resource, loop: Boolean) {
    }

    override fun pauseMusic() {
    }

    override fun resumeMusic() {
    }

    override fun stopMusic() {
    }

    override fun setMusicVolume(volume: Float) {
    }

    override fun loadSound(resource: Resource) {
    }

    override fun playSound(resource: Resource) {
    }

    override fun pauseSounds() {
    }

    override fun resumeSounds() {
    }

    override fun stopSounds() {
    }

    override fun setSoundVolume(volume: Float) {
    }

    override fun release() {
    }
}
