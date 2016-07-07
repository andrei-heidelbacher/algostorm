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

import algostorm.event.Event

/**
 * An event that requests a sound to be played on the given [frequency].
 *
 * @property soundUri the location of the sound which should be played
 * @property frequency the frequency on which the sound should be played
 * @property loop whether the sound should be looped or not
 */
data class PlaySound(
        val soundUri: String,
        val frequency: Int,
        val loop: Boolean = false
) : Event
