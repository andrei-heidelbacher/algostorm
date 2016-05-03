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

import algostorm.assets.SoundId
import algostorm.event.Event

/**
 * An event that requests a sound to be played on the given [frequency].
 *
 * @property soundId the id of the sound that should be played
 * @property frequency the frequency on which the sound should be played
 * @property loop `true` if the given sound should be looped, `false` otherwise
 */
data class PlaySound(val soundId: SoundId, val frequency: Int, val loop: Boolean) : Event
