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

package com.aheidelbacher.algostorm.systems.physics2d

import com.aheidelbacher.algostorm.ecs.Entity
import com.aheidelbacher.algostorm.ecs.EntityGroup
import com.aheidelbacher.algostorm.ecs.EntityManager
import com.aheidelbacher.algostorm.ecs.EntityRef
import com.aheidelbacher.algostorm.ecs.MutableEntityGroup
import com.aheidelbacher.algostorm.ecs.MutableEntityRef

val EntityRef.position: Position?
    get() = get(Position::class)

val EntityRef.isRigid: Boolean
    get() = RigidBody::class in this

fun Position.transformed(dx: Int, dy: Int): Position =
        copy(x = x + dx, y = y + dy)

fun EntityRef.overlaps(other: EntityRef): Boolean = position.let { p ->
    p != null && p == other.position
}

fun EntityGroup.getEntitiesAt(x: Int, y: Int): List<EntityRef> =
        Position(x, y).let { position ->
            entities.filter { entity -> entity.position == position }
        }

fun MutableEntityGroup.getEntitiesAt(x: Int, y: Int): List<MutableEntityRef> =
        Position(x, y).let { position ->
            entities.filter { entity -> entity.position == position }
        }
