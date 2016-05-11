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
 * An image object.
 *
 * @property source the location of this image
 * @property width the width of this image in pixels
 * @property height the height of this image in pixels
 * @throws IllegalArgumentException if the width or height are negative
 */
data class Image(val source: String, val width: Int, val height: Int) : Asset {
  /**
   * A viewport projected over an image.
   *
   * @property x the lower-left corner x-coordinate of this viewport
   * @property y the lower-left corner y-coordinate of this viewport
   * @property width the width of this viewport in pixels
   * @property height the height of this viewport in pixels
   * @throws IllegalArgumentException if the width or height are negative
   */
  data class Viewport(val x: Int, val y: Int, val width: Int, val height: Int) {
    init {
      require(width >= 0 && height >= 0) { "Viewport dimensions must be non-negative!" }
    }
  }

  init {
    require(width >= 0 && height >= 0) { "Image dimensions must be non-negative!" }
  }
}
