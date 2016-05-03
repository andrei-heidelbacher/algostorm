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

import com.fasterxml.jackson.annotation.JsonValue

/**
 * A type-safe non-negative [Sound] identifier.
 *
 * Two ids are equal if and only if they have the same [id] value. This id is serialized as a single
 * integer and is deserialized from a single integer, there is no wrapper object.
 *
 * @property id the sound id
 * @throws IllegalArgumentException if the given [id] is negative
 */
data class SoundId(@get:JsonValue private val id: Int) {
  init {
    require(id >= 0) { "Sound id can't be negative!" }
  }
}
