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

import algostorm.ecs.Component
import algostorm.ecs.Entity
import algostorm.graphics2d.Sprite

/**
 * A component which contains all information required to animate an entity.
 *
 * @property animationSheet a map with all the animations associated with the entity
 * @property frames the current animation applied to the entity
 * @property elapsedTicks how many ticks have elapsed from the current animation
 * @throws IllegalArgumentException if the current animation has less than 2 frames or if
 * [elapsedTicks] is negative or greater than [durationInTicks]
 */
data class Animation(
    val animationSheet: AnimationSheet,
    val frames: List<Frame>,
    val elapsedTicks: Int
) : Component {
  companion object {
    val Entity.animation: Animation?
      get() = get()
  }

  init {
    require(frames.size > 1) { "Animation must contain at least two frames!" }
    require(0 <= elapsedTicks && elapsedTicks <= durationInTicks) {
      "Elapsed ticks can't be negative or greater than the duration in ticks!"
    }
  }

  val durationInTicks: Int
    get() = frames.sumBy { frame -> frame.durationInTicks }

  /**
   * Returns the sprite that should be rendered at the time of calling.
   *
   * @return the sprite that should be rendered
   */
  val sprite: Sprite
    get() {
      var remainingTicks = elapsedTicks
      for ((sprite, durationInTicks) in frames) {
        if (durationInTicks <= remainingTicks) {
          remainingTicks -= durationInTicks
        } else {
          return sprite
        }
      }
      return frames.last().sprite
    }

  /**
   * Returns a copy of the animation information after a tick has passed.
   *
   * If the current animation finishes, it continues with the idle animation in the
   * [animationSheet].
   *
   * @return the animation information after a tick
   */
  fun tick(): Animation {
    return if (elapsedTicks + 1 < durationInTicks) copy(elapsedTicks = elapsedTicks + 1)
    else copy(
        frames = animationSheet.idle,
        elapsedTicks = (elapsedTicks + 1 - durationInTicks) %
            animationSheet.idle.sumBy { frame -> frame.durationInTicks }
    )
  }
}
