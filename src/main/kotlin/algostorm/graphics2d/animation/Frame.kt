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

package algostorm.graphics2d.animation

import algostorm.graphics2d.Sprite

/**
 * A building-block for frame sequences used in animations.
 *
 * @property sprite the sprite that should be rendered
 * @property durationInTicks how long should the current sprite be rendered
 * @throws IllegalArgumentException if [durationInTicks] is not positive
 */
data class Frame(val sprite: Sprite, val durationInTicks: Int) {
  init {
    require(durationInTicks > 0) { "Frame duration must be positive!" }
  }
}
