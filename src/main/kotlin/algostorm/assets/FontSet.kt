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
 * A container that maps non-negative font ids to fonts.
 *
 * @property fonts the map that maps ids to fonts
 */
data class FontSet(private val fonts: Map<FontId, Font>) {
  /**
   * Returns the font with the given id.
   *
   * @param id the id of the font
   * @return the requested font, or `null` if it doesn't exist
   */
  operator fun get(id: FontId): Font? = fonts[id]

  /**
   * Returns whether this font set contains the font with the given id.
   *
   * @param id the id of the font
   * @return `true` if the given font [id] is contained in this font set, `false` otherwise
   */
  operator fun contains(id: FontId): Boolean = get(id) != null
}
