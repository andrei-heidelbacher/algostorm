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

package algostorm.graphics2d.transform

import algostorm.ecs.Component

/**
 * A component that indicates a sequence of [transformations] that should be applied to the current
 * entity.
 *
 * @property transformations the transformation sequence that is to be applied
 * @property elapsedTicks the number of ticks that have elapsed since the beginning of the
 * transformation
 * @throws IllegalArgumentException if [elapsedTicks] is negative or greater than [durationInTicks]
 * or if [transformations] is empty
 */
data class TransformationTimer(
    val transformations: List<TimedTransformation>,
    val elapsedTicks: Int
) : Component {
  init {
    require(transformations.isNotEmpty()) { "Transformation sequence can't be empty!" }
    require(0 <= elapsedTicks && elapsedTicks <= durationInTicks) {
      "Elapsed ticks must be non-negative and less than the duration in ticks!"
    }
  }

  /**
   * The total duration in ticks of the [transformations].
   */
  val durationInTicks: Int
    get() = transformations.sumBy { transformation -> transformation.durationInTicks }

  /**
   * The interpolated transformation that should be applied to the entity at the time of calling.
   */
  val transformation: Transformation
    get() {
      var current = Transformation.identity
      var remainingTicks = elapsedTicks
      for ((transformation, durationInTicks) in transformations) {
        if (remainingTicks >= durationInTicks) {
          current += transformation
          remainingTicks -= durationInTicks
        } else {
          current += transformation * (remainingTicks.toFloat() / durationInTicks.toFloat())
          break
        }
      }
      return current
    }

  /**
   * Returns a copy of the transformation information after a tick has passed.
   *
   * @return the transformation information after another ticks has elapsed
   */
  fun tick(): TransformationTimer = copy(elapsedTicks = elapsedTicks + 1)
}
