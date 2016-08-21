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

import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber

import java.io.FileNotFoundException

/**
 * A system which handles playing and stopping sounds.
 *
 * @property soundEngine the sound engine used to play sounds. Methods on this
 * object will be called from the private engine thread and a thread-safe
 * implementation should be provided.
 * @param soundPaths the locations of the sounds which are loaded at
 * construction time using the [SoundEngine.loadSound] method
 * @throws FileNotFoundException if any of the given sounds doesn't exist
 */
class SoundSystem @Throws(FileNotFoundException::class) constructor(
        private val soundEngine: SoundEngine,
        soundPaths: List<String>
) : Subscriber {
    init {
        soundPaths.forEach { soundEngine.loadSound(it) }
    }

    /**
     * After receiving a [PlaySound] event, the [SoundEngine.play] method is
     * called.
     *
     * @param event the event which requests a sound to be played
     */
    @Subscribe fun onPlaySound(event: PlaySound) {
        event.onResult?.invoke(soundEngine.play(event.sound, event.loop))
    }

    /**
     * After receiving a [StopStream] event, the [SoundEngine.stop] method is
     * called.
     *
     * @param event the event which requests a stream to be stopped
     */
    @Subscribe fun onStopStream(event: StopStream) {
        soundEngine.stop(event.streamId)
    }
}
