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

import algostorm.ecs.EntitySystem
import algostorm.event.Subscriber

/**
 * A system which handles playing and stopping sounds.
 *
 * After receiving a [PlaySound] event, the [SoundEngine.playSound] method is
 * called. After receiving a [StopSound] event, the [SoundEngine.stopSound]
 * method is called.
 *
 * @property soundEngine the engine that will play the sounds
 * @property properties the properties of the game
 */
class SoundSystem(
        private val soundEngine: SoundEngine,
        private val properties: Map<String, Any?>
) : EntitySystem {
    companion object {
        /**
         * The property used by this system. It should be an object of type
         * [SoundSet].
         */
        const val SOUND_SET: String = "soundSet"
    }

    private val soundSet: SoundSet
        get() = (properties[SOUND_SET] as? SoundSet)
                ?: error("Missing $SOUND_SET property!")

    private val playHandler = Subscriber(PlaySound::class) { event ->
        val soundUri = soundSet[event.soundId] ?: error("Missing sound id!")
        soundEngine.playSound(soundUri, event.frequency, event.loop)
    }

    private val stopHandler = Subscriber(StopSound::class) { event ->
        soundEngine.stopSound(event.frequency)
    }

    /**
     * This system handles [PlaySound] and [StopSound] events.
     */
    override val handlers: List<Subscriber<*>> =
            listOf(playHandler, stopHandler)
}
