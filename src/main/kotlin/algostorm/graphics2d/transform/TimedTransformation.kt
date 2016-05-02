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

/**
 * A [transformation] which should be applied over time on the owner entity.
 *
 * @property transformation the transformation that should be applied
 * @property durationInTicks the time span of the transformation in ticks
 * @throws IllegalArgumentException if [durationInTicks] is not positive
 */
data class TimedTransformation(val transformation: Transformation, val durationInTicks: Int) {
  init {
    require(durationInTicks > 0) { "Transformation duration must be positive!" }
  }
}
