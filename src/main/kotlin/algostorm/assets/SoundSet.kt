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

package algostorm.assets

/**
 * A container that maps non-negative sound ids to sounds.
 *
 * @property sounds the map that maps ids to sounds
 */
data class SoundSet(private val sounds: Map<SoundId, Sound>) {
  /**
   * Returns the sound with the given [id].
   *
   * @param id the id of the sound
   * @return the requested sound, or `null` if it doesn't exist
   */
  operator fun get(id: SoundId): Sound? = sounds[id]

  /**
   * Returns whether this sound set contains the sound with the given [id].
   *
   * @param id the id of the sound
   * @return `true` if the given sound [id] is contained in this sound set, `false` otherwise
   */
  operator fun contains(id: SoundId): Boolean = id in sounds
}
