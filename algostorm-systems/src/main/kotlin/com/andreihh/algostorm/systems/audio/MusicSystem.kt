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

package com.andreihh.algostorm.systems.audio

import com.andreihh.algostorm.core.drivers.audio.AudioStream
import com.andreihh.algostorm.core.drivers.audio.MusicPlayer
import com.andreihh.algostorm.core.drivers.io.Resource
import com.andreihh.algostorm.core.event.Event
import com.andreihh.algostorm.core.event.Subscribe

/**
 * A system which handles playing longer sounds.
 *
 * @property musicPlayer the music player used to play longer sounds
 */
class MusicSystem : AudioSystem() {
    private val musicPlayer by context<MusicPlayer>(AUDIO_DRIVER)

    /**
     * A request to set the music volume to the given value.
     *
     * @property volume the new value of the volume
     * @throws IllegalArgumentException if `volume` is not in the range `0..1`
     */
    class SetMusicVolume(val volume: Float) : Event {
        init {
            require(volume in 0F..1F) { "Invalid music volume $volume!" }
        }
    }

    /**
     * A request to play a longer sound, stopping the previously playing music.
     *
     * @property music the music resource which should be played
     * @property loop whether the sound should be looped or not
     */
    class PlayMusic(
            val music: Resource<AudioStream>,
            val loop: Boolean = false
    ) : Event

    /** A request to stop the currently played music. */
    object StopMusic : Event

    override fun onStart() {
        super.onStart()
        musicPlayer.resumeMusic()
    }

    override fun onStop() {
        super.onStop()
        musicPlayer.pauseMusic()
    }

    /**
     * After receiving a [SetMusicVolume] request, the volume of the music is
     * modified accordingly.
     *
     * @param event the request
     */
    @Subscribe
    fun onSetVolume(event: SetMusicVolume) {
        musicPlayer.setMusicVolume(event.volume)
    }

    /**
     * After receiving a [PlayMusic] request, the previously playing music is
     * stopped and the given music is played.
     *
     * @param event the request
     */
    @Subscribe
    fun onPlayMusic(event: PlayMusic) {
        musicPlayer.playMusic(event.music, event.loop)
    }

    /**
     * After receiving a [StopMusic] request, the currently playing music is
     * stopped.
     *
     * @param event the request
     */
    @Subscribe
    fun onStopMusic(event: StopMusic) {
        musicPlayer.stopMusic()
    }
}
