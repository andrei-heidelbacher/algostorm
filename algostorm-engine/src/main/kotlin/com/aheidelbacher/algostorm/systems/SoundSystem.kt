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

package com.aheidelbacher.algostorm.systems

import com.aheidelbacher.algostorm.state.File
import com.aheidelbacher.algostorm.event.Event
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber

import java.io.FileNotFoundException

/**
 * A system which handles playing and stopping sounds.
 *
 * @property soundEngine the sound engine used to play sounds. Methods on this
 * object will be called from the private engine thread and a thread-safe
 * implementation should be provided.
 * @param musicSources the locations of the music files which are loaded at
 * construction time using the [SoundEngine.loadMusic] method
 * @param soundSources the locations of the sounds which are loaded at
 * construction time using the [SoundEngine.loadSound] method
 * @throws FileNotFoundException if any of the given sounds doesn't exist
 */
class SoundSystem @Throws(FileNotFoundException::class) constructor(
        private val soundEngine: SoundEngine,
        musicSources: List<File>,
        soundSources: List<File>
) : Subscriber {
    /**
     * An event that requests a longer sound to be played on a dedicated stream.
     *
     * @property sound the sound which should be played
     * @property loop whether the sound should be looped or not
     */
    data class PlayMusic(val sound: File, val loop: Boolean = false) : Event

    /**
     * An event which signals that the sound played on the dedicated stream for
     * longer sounds should be stopped (sounds played with [PlayMusic] will be
     * stopped).
     */
    object StopMusic : Event

    /**
     * An event that requests a short sound to be played.
     *
     * @property sound the sound which should be played
     * @property onResult the callback which receives the returned stream id
     */
    data class PlaySound(
            val sound: File,
            val onResult: ((Int) -> Unit)? = null
    ) : Event

    /**
     * An event which signals that the sound played on the given [streamId]
     * should be stopped.
     *
     * @property streamId the id of the stream which should be stopped
     */
    data class StopStream(val streamId: Int) : Event

    init {
        musicSources.forEach { soundEngine.loadMusic(it.path) }
        soundSources.forEach { soundEngine.loadSound(it.path) }
    }

    /**
     * After receiving a [PlayMusic] event, the [SoundEngine.playMusic] method
     * is called.
     *
     * @param event the event which requests a sound to be played
     */
    @Subscribe fun onPlayMusic(event: PlayMusic) {
        soundEngine.playMusic(event.sound.path, event.loop)
    }

    /**
     * After receiving a [StopMusic] event, the [SoundEngine.stopMusic] method
     * is called.
     *
     * @param event the event which requests the dedicated stream for longer
     * sounds to be stopped
     */
    @Subscribe fun onStopMusic(event: StopMusic) {
        soundEngine.stopMusic()
    }

    /**
     * After receiving a [PlaySound] event, the [SoundEngine.playSound] method
     * is called.
     *
     * @param event the event which requests a sound to be played
     */
    @Subscribe fun onPlaySoundEffect(event: PlaySound) {
        event.onResult?.invoke(soundEngine.playSound(event.sound.path))
    }

    /**
     * After receiving a [StopStream] event, the [SoundEngine.stopStream] method
     * is called.
     *
     * @param event the event which requests a stream to be stopped
     */
    @Subscribe fun onStopStream(event: StopStream) {
        soundEngine.stopStream(event.streamId)
    }
}
