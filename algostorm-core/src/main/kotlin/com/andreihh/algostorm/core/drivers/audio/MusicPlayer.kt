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

package com.andreihh.algostorm.core.drivers.audio

import com.andreihh.algostorm.core.drivers.io.Resource

/**
 * A music player which should be used for playing longer sounds.
 *
 * Only one stream is active at all times. As such, only one music resource can
 * be played at once.
 */
interface MusicPlayer {
    /**
     * Sets the volume at which music is played.
     *
     * @param volume a value between `0` (mute) and `1` (maximum volume)
     * @throws IllegalArgumentException if `volume` is not in the range `0..1`
     */
    fun setMusicVolume(volume: Float)

    /**
     * Stops the previously playing music and asynchronously plays the given
     * `music`.
     *
     * @param music the music resource which should be played
     * @param loop whether the music should be looped or not
     * @throws IllegalArgumentException if the given `resource` was not loaded
     * by the audio driver
     */
    fun playMusic(music: Resource<AudioStream>, loop: Boolean = false)

    /**
     * Pauses the currently playing music.
     *
     * If no music resource is currently playing, this method has no effect.
     */
    fun pauseMusic()

    /**
     * Asynchronously resumes the currently paused music.
     *
     * If no music resource was previously paused or if it was stopped, this
     * method has no effect.
     */
    fun resumeMusic()

    /**
     * Stops the currently playing music.
     *
     * If no music resource is currently playing, this method has no effect.
     */
    fun stopMusic()
}
