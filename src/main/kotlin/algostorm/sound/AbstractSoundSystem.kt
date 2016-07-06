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

package algostorm.sound

import algostorm.event.Subscribe
import algostorm.event.Subscriber

/**
 * A system which handles playing and stopping sounds.
 *
 * Sounds on different frequencies are played simultaneously and do not affect
 * each other. Each frequency can play at most one sound at once. This allows
 * using a frequency for background music, another frequency for special effect
 * sounds etc.
 *
 * Methods on this object will be called from the private engine thread. All
 * method calls should be non-blocking and thread-safe.
 *
 * @param soundSet the collection that maps sound ids to sound URIs
 */
abstract class AbstractSoundSystem(
        private val soundSet: Map<Int, String>
) : Subscriber {
    /**
     * Plays the sound located at the given [soundUri]. If another sound is
     * already playing on the given [frequency], that sound should be stopped
     * first.
     *
     * @param soundUri the location of the sound which should be played
     * @param frequency the frequency on which the sound should be played
     * @param loop whether the sound should be looped or not
     */
    protected abstract fun playSound(
            soundUri: String,
            frequency: Int,
            loop: Boolean
    ): Unit

    /**
     * Stops the sound playing on the given [frequency].
     *
     * @param frequency the frequency on which sounds should be stopped
     */
    protected abstract fun stopSound(frequency: Int): Unit

    /**
     * After receiving a [PlaySound] event, the [playSound] method is called.
     *
     * @param event the event which requests a sound to be played
     */
    @Subscribe fun handlePlaySound(event: PlaySound) {
        val soundUri = soundSet[event.soundId]
                ?: error("Missing sound id ${event.soundId}!")
        playSound(soundUri, event.frequency, event.loop)
    }

    /**
     * After receiving a [StopSound] event, the [stopSound] method is called.
     *
     * @param event the event which requests a sound to be stopped
     */
    @Subscribe fun handleStopSound(event: StopSound) {
        stopSound(event.frequency)
    }
}
