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

import algostorm.assets.AssetCollection
import algostorm.assets.Sound
import algostorm.ecs.EntitySystem
import algostorm.event.Subscriber

/**
 * A system which handles playing sounds.
 *
 * Sounds on different frequencies are played simultaneously and do not affect each other. Each
 * frequency can play at most one sound at once. This allows using a frequency for background music,
 * another frequency for special effect sounds etc.
 *
 * @property sounds the sounds used in the game
 */
abstract class AbstractSoundSystem(protected val sounds: AssetCollection<Sound>) : EntitySystem() {
  /**
   * This method is called when a [PlaySound] event occurs. If another sound is already playing on
   * the given [frequency], that sound should be stopped first.
   *
   * @param sound the sound which should be played
   * @param frequency the frequency on which the sound should be played
   */
  protected abstract fun playSound(sound: Sound, frequency: Int): Unit

  /**
   * This method is called when a [StopSound] event occurs.
   *
   * @param frequency the frequency on which sounds should be stopped
   */
  protected abstract fun stopSound(frequency: Int): Unit

  private val playHandler = Subscriber(PlaySound::class) { event ->
    playSound(sounds[event.soundId] ?: error("Sound id doesn't exist!"), event.frequency)
  }

  private val stopHandler = Subscriber(StopSound::class) { event ->
    stopSound(event.frequency)
  }

  /**
   * This system handles [PlaySound] and [StopSound] events.
   */
  final override val handlers: List<Subscriber<*>> = listOf(playHandler, stopHandler)
}
