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

package algostorm.audio

import algostorm.assets.AssetCollection
import algostorm.assets.Sound

/**
 * An object which handles playing sounds.
 *
 * Sounds on different frequencies are played simultaneously and do not affect each other. Each
 * frequency can play at most one sound at once. This allows using a frequency for background music,
 * another frequency for special effect sounds etc.
 *
 * Methods on this object will be called from the private engine thread. All method calls should be
 * non-blocking and thread-safe.
 */
interface AudioEngine {
  /**
   * The sounds used in the game.
   */
  val sounds: AssetCollection<Sound>

  /**
   * Plays the given [sound]. If another sound is already playing on the given [frequency], that
   * sound should be stopped first.
   *
   * @param sound the sound which should be played
   * @param frequency the frequency on which the sound should be played
   * @param loop whether the sound should be looped or not
   */
  fun playSound(sound: Sound, frequency: Int, loop: Boolean = false): Unit

  /**
   * Fetches the sound with the given [soundId] and calls [playSound] with the found [Sound].
   *
   * @param soundId the id of the sound which should be played
   * @param frequency the frequency on which the sound should be played
   * @param loop whether the sound should be looped or not
   * @throws IllegalStateException if the given [soundId] doesn't exist in the [sounds] collection
   */
  fun playSound(soundId: Int, frequency: Int, loop: Boolean = false) {
    playSound(sounds[soundId] ?: error("Sound id doesn't exist!"), frequency, loop)
  }

  /**
   * Stops the sound playing on the given [frequency].
   *
   * @param frequency the frequency on which sounds should be stopped
   */
  fun stopSound(frequency: Int): Unit
}
