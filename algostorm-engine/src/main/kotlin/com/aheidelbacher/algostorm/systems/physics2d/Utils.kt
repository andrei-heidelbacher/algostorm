/*
 * Copyright (c) 2016  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
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
import com.aheidelbacher.algostorm.ecs.EntityManager

val Entity.position: Position?
    get() = get(Position::class)

fun Position.transformed(dx: Int, dy: Int): Position =
        copy(x = x + dx, y = y + dy)

val Entity.isRigid: Boolean
    get() = RigidBody::class in this

fun Entity.overlaps(other: Entity): Boolean = position.let { p ->
    p != null && p == other.position
}

fun EntityManager.getEntitiesAt(x: Int, y: Int): List<Entity> =
        Position(x, y).let { position ->
            entities.filter { entity -> entity.position == position }
        }
