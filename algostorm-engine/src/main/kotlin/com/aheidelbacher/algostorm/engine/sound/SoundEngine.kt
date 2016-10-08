/*
 * Copyright 2016 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.aheidelbacher.algostorm.engine.sound

import java.io.FileNotFoundException

/** An object that can play music and multiple short sounds at once. */
interface SoundEngine {
    /**
     * Loads the sound at the specified location, making it available to future
     * calls of [playMusic].
     *
     * @param musicSource the location of the sound which should be loaded
     * @throws FileNotFoundException if the given sound doesn't exist
     * @throws IllegalStateException if this sound engine was released
     */
    @Throws(FileNotFoundException::class)
    fun loadMusic(musicSource: String): Unit

    /**
     * Loads the sound at the specified location, making it available to future
     * calls of [playSound].
     *
     * @param soundSource the location of the sound which should be loaded
     * @throws FileNotFoundException if the given sound doesn't exist
     * @throws IllegalStateException if this sound engine was released
     */
    @Throws(FileNotFoundException::class)
    fun loadSound(soundSource: String): Unit

    /**
     * Plays the given sound on a dedicated stream. This should be used to play
     * longer sounds. Only one stream is active at all times for this type of
     * sounds.
     *
     * @param musicSource the location of the sound which should be played
     * @param loop whether the sound should be looped or not
     * @return the id of the stream on which the sound is played, or `-1` if the
     * sound could not be played
     * @throws IllegalArgumentException if the [musicSource] was not loaded
     * @throws IllegalStateException if this sound engine was released
     */
    fun playMusic(musicSource: String, loop: Boolean = false): Unit

    /**
     * Stops the stream dedicated to longer sounds. This will stop any sounds
     * played with [playMusic].
     *
     * @throws IllegalStateException if this sound engine was released
     */
    fun stopMusic(): Unit

    /**
     * Plays the given sound and returns the stream id on which the sound is
     * played.
     *
     * This should be used for short sound effects (at most a few seconds).
     *
     * @param soundSource the location of the sound which should be played
     * @return the id of the stream on which the sound is played, or `-1` if the
     * sound could not be played
     * @throws IllegalArgumentException if the [soundSource] was not loaded
     * @throws IllegalStateException if this sound engine was released
     */
    fun playSound(soundSource: String): Int

    /**
     * Stops the stream with the given id.
     *
     * @param streamId the id of the stream which should stop playing sounds
     * @throws IllegalStateException if this sound engine was released
     */
    fun stopStream(streamId: Int): Unit

    /**
     * Release all resources associated with this sound engine.
     *
     * @throws IllegalStateException if this sound engine was already released
     */
    fun release(): Unit
}
