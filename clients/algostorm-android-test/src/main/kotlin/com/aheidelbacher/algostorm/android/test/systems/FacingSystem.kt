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

package com.aheidelbacher.algostorm.android.test.systems

import com.aheidelbacher.algostorm.core.ecs.Component
import com.aheidelbacher.algostorm.core.ecs.EntityRef
import com.aheidelbacher.algostorm.core.ecs.MutableEntityGroup
import com.aheidelbacher.algostorm.core.ecs.MutableEntityRef
import com.aheidelbacher.algostorm.core.event.Subscribe
import com.aheidelbacher.algostorm.core.event.Subscriber
import com.aheidelbacher.algostorm.systems.physics2d.geometry2d.Direction
import com.aheidelbacher.algostorm.android.test.systems.MovementSystem.Moved

class FacingSystem(private val group: MutableEntityGroup) : Subscriber {
    enum class Facing : Component {
        LEFT, UP, RIGHT, DOWN
    }

    private val EntityRef.isFacing: Boolean
        get() = Facing::class in this

    private val EntityRef.facing: Facing
        get() = get(Facing::class) ?: error("$this must be facing!")

    private fun MutableEntityRef.updateFacing(direction: Direction) {
        val newFacing = when (direction) {
            Direction.N -> Facing.UP
            Direction.E -> Facing.RIGHT
            Direction.S -> Facing.DOWN
            Direction.W -> Facing.LEFT
            else -> facing
        }
        set(newFacing)
    }

    @Subscribe fun onMoved(event: Moved) {
        group[event.entityId]?.let { entity ->
            if (entity.isFacing) {
                entity.updateFacing(event.direction)
            }
        }
    }
}
