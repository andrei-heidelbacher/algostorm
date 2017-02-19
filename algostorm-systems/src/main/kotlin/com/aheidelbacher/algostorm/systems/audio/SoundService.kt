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

package com.aheidelbacher.algostorm.systems.audio

import com.aheidelbacher.algostorm.core.drivers.Resource
import com.aheidelbacher.algostorm.core.drivers.client.audio.SoundPlayer
import com.aheidelbacher.algostorm.core.event.Request
import com.aheidelbacher.algostorm.core.event.Service
import com.aheidelbacher.algostorm.core.event.Subscribe

/**
 * A system which handles playing short sound effects.
 *
 * @property soundPlayer the sound player used to play short sound effects
 */
class SoundService(private val soundPlayer: SoundPlayer) : Service() {
    /**
     * A request to set the sound effects volume to the given value.
     *
     * @property volume the new value of the volume
     * @throws IllegalArgumentException if `volume` is not in the range `0..1`
     */
    class SetSoundVolume(val volume: Float) : Request<Unit>() {
        init {
            require(volume in 0F..1F) { "Invalid sound volume $volume!" }
        }
    }

    /**
     * A request to play a short sound.
     *
     * @property resource the sound resource which should be played
     */
    class PlaySoundEffect(val resource: Resource) : Request<Unit>()

    override fun onStart() {
        soundPlayer.resumeSounds()
    }

    override fun onStop() {
        soundPlayer.pauseSounds()
    }

    /**
     * After receiving a [SetSoundVolume] request, the volume of the sounds is
     * modified accordingly.
     *
     * @param request the request
     */
    @Subscribe fun onSetVolume(request: SetSoundVolume) {
        soundPlayer.setSoundVolume(request.volume)
        request.complete(Unit)
    }

    /**
     * After receiving a [PlaySoundEffect] request, the given sound is played.
     *
     * @param request the request
     */
    @Subscribe fun onPlaySoundEffect(request: PlaySoundEffect) {
        soundPlayer.playSound(request.resource)
        request.complete(Unit)
    }
}
