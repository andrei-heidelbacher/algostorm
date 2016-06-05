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

package algostorm.physics2d

import algostorm.event.Event

/**
 * An event which signals that the given entity should have it's velocity
 * increased by the given amount.
 *
 * @property entityId the id of the entity
 * @property x the amount by which the velocity should increase on the x-axis
 * @property y the amount by which the velocity should increase on the y-axis
 */
data class Accelerate(val entityId: Int, val x: Int, val y: Int) : Event
