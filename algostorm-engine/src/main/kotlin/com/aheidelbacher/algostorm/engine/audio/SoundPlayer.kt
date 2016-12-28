/*
 * Copyright (c) 2016  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.aheidelbacher.algostorm.engine.audio

import com.aheidelbacher.algostorm.engine.driver.Resource

/**
 * A sound player which should be used for short sound effects (each loaded
 * sound must be at most `1 MB` in uncompressed PCM format).
 */
interface SoundPlayer {
    /**
     * Synchronously loads the given sound `resource`, making it available to
     * future calls of [playSound].
     *
     * @param resource the sound resource which should be loaded
     */
    fun loadSound(resource: Resource): Unit

    /**
     * Sets the volume at which sounds are played.
     *
     * @param volume a value between `0` (mute) and `1` (maximum volume)
     * @throws IllegalArgumentException if `volume` is not in the range `0..1`
     */
    fun setSoundVolume(volume: Float): Unit

    /**
     * Asynchronously plays the given sound `resource`.
     *
     * @param resource the sound resource which should be played
     * @throws IllegalArgumentException if the `resource` was not loaded
     */
    fun playSound(resource: Resource): Unit

    /** Pauses all of the currently playing sounds. */
    fun pauseSounds(): Unit

    /** Asynchronously resumes all the paused sounds. */
    fun resumeSounds(): Unit

    /** Stops all of the currently playing sounds. */
    fun stopSounds(): Unit
}
