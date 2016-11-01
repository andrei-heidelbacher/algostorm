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

package com.aheidelbacher.algostorm.systems.physics2d

import com.aheidelbacher.algostorm.event.Event

/**
 * An event which signals that the given entity has been transformed.
 *
 * Only the [PhysicsSystem] should post this event.
 *
 * @property entityId the id of the transformed entity
 * @property dx the horizontal translation amount in tiles
 * @property dy the vertical translation amount in tiles (positive is down)
 */
data class Transformed(
        val entityId: Int,
        val dx: Int,
        val dy: Int
) : Event
