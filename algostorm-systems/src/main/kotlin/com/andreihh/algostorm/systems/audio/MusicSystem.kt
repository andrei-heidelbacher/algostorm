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
import com.andreihh.algostorm.core.event.Request
import com.andreihh.algostorm.core.event.Subscribe
import com.andreihh.algostorm.systems.EventSystem

/**
 * A system which handles playing longer sounds.
 *
 * @property musicPlayer the music player used to play longer sounds
 */
class MusicSystem : EventSystem() {
    companion object {
        const val MUSIC_PLAYER: String = "MUSIC_PLAYER"
    }

    private val musicPlayer: MusicPlayer by context(MUSIC_PLAYER)

    /**
     * A request to set the music volume to the given value.
     *
     * @property volume the new value of the volume
     * @throws IllegalArgumentException if `volume` is not in the range `0..1`
     */
    class SetMusicVolume(val volume: Float) : Request<Unit>() {
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
    ) : Request<Unit>()

    /** A request to stop the currently played music. */
    class StopMusic : Request<Unit>()

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
     * @param request the request
     */
    @Subscribe
    fun onSetVolume(request: SetMusicVolume) {
        musicPlayer.setMusicVolume(request.volume)
        request.complete(Unit)
    }

    /**
     * After receiving a [PlayMusic] request, the previously playing music is
     * stopped and the given music is played.
     *
     * @param request the request
     */
    @Subscribe
    fun onPlayMusic(request: PlayMusic) {
        musicPlayer.playMusic(request.music, request.loop)
        request.complete(Unit)
    }

    /**
     * After receiving a [StopMusic] request, the currently playing music is
     * stopped.
     *
     * @param request the request
     */
    @Subscribe
    fun onStopMusic(request: StopMusic) {
        musicPlayer.stopMusic()
        request.complete(Unit)
    }
}
