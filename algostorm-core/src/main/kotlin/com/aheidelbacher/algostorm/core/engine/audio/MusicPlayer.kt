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

package com.aheidelbacher.algostorm.core.engine.audio

import com.aheidelbacher.algostorm.core.engine.driver.Resource

/**
 * A music player which should be used for playing longer sounds.
 *
 * Only one stream is active at all times. As such, only one music resource can
 * be played at once.
 */
interface MusicPlayer {
    /**
     * Synchronously loads the given music `resource`, making it available to
     * future calls of [playMusic].
     *
     * If the same resource is loaded multiple times, this method has no effect.
     *
     * @param resource the music resource which should be loaded
     */
    fun loadMusic(resource: Resource): Unit

    /**
     * Sets the volume at which music is played.
     *
     * @param volume a value between `0` (mute) and `1` (maximum volume)
     * @throws IllegalArgumentException if `volume` is not in the range `0..1`
     */
    fun setMusicVolume(volume: Float): Unit

    /**
     * Stops the previously playing music and asynchronously plays the given
     * music `resource`.
     *
     * @param resource the music resource which should be played
     * @param loop whether the music should be looped or not
     * @throws IllegalArgumentException if the given `resource` was not loaded
     */
    fun playMusic(resource: Resource, loop: Boolean = false): Unit

    /**
     * Pauses the currently playing music.
     *
     * If no music resource is currently playing, this method has no effect.
     */
    fun pauseMusic(): Unit

    /**
     * Asynchronously resumes the currently paused music.
     *
     * If no music resource was previously paused or if it was stopped, this
     * method has no effect.
     */
    fun resumeMusic(): Unit

    /**
     * Stops the currently playing music.
     *
     * If no music resource is currently playing, this method has no effect.
     */
    fun stopMusic(): Unit
}
