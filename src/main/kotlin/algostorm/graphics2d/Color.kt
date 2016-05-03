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

package algostorm.graphics2d

/**
 * An object that represents a color in the ARGB format.
 */
data class Color private constructor(private val color: Int) {
  /**
   * Builds a color with the given components.
   *
   * @param alpha the alpha component of the color. Must be between `0` and `255`.
   * @param red the red component of the color. Must be between `0` and `255`.
   * @param green the green component of the color. Must be between `0` and `255`.
   * @param blue the blue component of the color. Must be between `0` and `255`.
   * @throws IllegalArgumentException if any of the given components are less than `0` or greater
   * than `255`
   */
  constructor(alpha: Int, red: Int, green: Int, blue: Int) : this(
      (alpha shl 24) or (red shl 16) or (green shl 8) or blue
  ) {
    require(0 <= alpha && alpha <= 0xFF &&
        0 <= red && red <= 0xFF &&
        0 <= green && green <= 0xFF &&
        0 <= blue && blue <= 0xFF
    ) { "Every color component must be between 0 and 255 inclusive!" }
  }

  /**
   * The alpha component of this color. The returned value is between `0` and `255`.
   */
  val alpha: Int
    get() = (color shr 24) and 0xFF

  /**
   * The red component of this color. The returned value is between `0` and `255`.
   */
  val red: Int
    get() = (color shr 16) and 0xFF

  /**
   * The green component of this color. The returned value is between `0` and `255`.
   */
  val green: Int
    get() = (color shr 8) and 0xFF

  /**
   * The blue component of this color. The returned value is between `0` and `255`.
   */
  val blue: Int
    get() = color and 0xFF
}
