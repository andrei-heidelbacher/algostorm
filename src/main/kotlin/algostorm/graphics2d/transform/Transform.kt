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

import algostorm.event.Event

/**
 * An event which signals a sequence of [transformations] that are to be applied
 * successively on the specified entity.
 *
 * @property entityId the id of the entity that is to be transformed
 * @property transformations the transformation sequence that must be applied
 * @throws IllegalArgumentException if [transformations] is empty
 */
data class Transform(
        val entityId: Int,
        val transformations: List<TimedTransformation>
) : Event {
    init {
        require(transformations.isNotEmpty()) {
            "Transformation sequence can't be empty!"
        }
    }
}
