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

package algostorm.time

import algostorm.ecs.Entity

/**
 * A container for the timers attached to an entity.
 *
 * @property timers the timers attached to the owner entity
 */
data class Timeline(val timers: List<Timer>) {
    companion object {
        /**
         * The name of the timeline property. It is of type [Timeline].
         */
        const val PROPERTY: String = "timeline"

        /**
         * The timers attached to this entity.
         */
    }
}
