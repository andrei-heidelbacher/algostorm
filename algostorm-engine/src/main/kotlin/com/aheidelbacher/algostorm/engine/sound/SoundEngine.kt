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

/**
 * An object that can play multiple sounds at once.
 */
interface SoundEngine {
    /**
     * The maximum number of sounds which can be played at the same time. Must
     * not be negative.
     */
    val maxStreams: Int

    /**
     * Loads the sound at the specified location, making it available to future
     * calls of [play].
     *
     * @param soundPath the location of the sound which should be loaded
     * @throws FileNotFoundException if the given sound doesn't exist
     */
    @Throws(FileNotFoundException::class)
    fun loadSound(soundPath: String): Unit

    /**
     * Plays the given sound and returns the stream id on which the sound is
     * played.
     *
     * @param sound the location of the sound which should be played
     * @param loop whether the sound should be looped or not
     * @return the id of the stream on which the sound is played, or `-1` if the
     * sound could not be played
     * @throws IllegalArgumentException if the [sound] was not loaded
     */
    fun play(sound: String, loop: Boolean = false): Int

    /**
     * Stops the stream with the given id.
     *
     * @param streamId the id of the stream which should stop playing sounds
     */
    fun stop(streamId: Int): Unit
}
