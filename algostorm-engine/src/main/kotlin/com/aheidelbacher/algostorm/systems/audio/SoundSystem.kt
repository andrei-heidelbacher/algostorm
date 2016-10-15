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

package com.aheidelbacher.algostorm.systems.audio

import com.aheidelbacher.algostorm.engine.audio.SoundPlayer
import com.aheidelbacher.algostorm.state.File
import com.aheidelbacher.algostorm.event.Event
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber

import java.io.FileNotFoundException

/**
 * A system which handles playing short sound effects.
 *
 * @property soundPlayer the sound player used to play short sound effects
 * @param soundSources the paths of the sound resources which are loaded at
 * construction time
 * @throws FileNotFoundException if any of the given resources doesn't exist
 */
class SoundSystem @Throws(FileNotFoundException::class) constructor(
        private val soundPlayer: SoundPlayer,
        soundSources: List<File>
) : Subscriber {
    /**
     * An event that requests a short sound to be played.
     *
     * @property sound the path of the sound which should be played
     */
    data class PlaySoundEffect(val sound: File) : Event

    init {
        soundSources.forEach { soundPlayer.loadSound(it.path) }
    }

    /**
     * After receiving a [PlaySoundEffect] event, the given sound is played.
     *
     * @param event the event which requests a sound to be played
     */
    @Subscribe fun onPlaySoundEffect(event: PlaySoundEffect) {
        soundPlayer.playSound(event.sound.path)
    }
}
