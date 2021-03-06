/*
 * Copyright 2017 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.andreihh.algostorm.systems.physics2d

import com.andreihh.algostorm.core.ecs.EntityRef.Id
import com.andreihh.algostorm.core.event.Event

/**
 * An event which signals that [selfId] collided with [otherId].
 *
 * Only the [PhysicsSystem] should post this event.
 *
 * @property selfId the id of this entity, which triggered the collision
 * @property otherId the id of the other entity, which was collided
 */
data class Collision internal constructor(
        val selfId: Id,
        val otherId: Id
) : Event
