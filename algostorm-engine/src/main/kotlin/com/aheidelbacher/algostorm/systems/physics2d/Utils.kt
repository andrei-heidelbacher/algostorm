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

import com.aheidelbacher.algostorm.state.Entity
import com.aheidelbacher.algostorm.state.Layer.EntityGroup

/** The `Body` component of this entity. */
val Entity.body: Body?
    get() = get(Body::class)

val Entity.isRigid: Boolean
    get() = body?.type == Body.Type.RIGID

val Entity.isTrigger: Boolean
    get() = body?.type == Body.Type.TRIGGER

val Entity.position: Position?
    get() = get(Position::class)

fun Position.transform(dx: Int, dy: Int): Position =
        copy(x = x + dx, y = y + dy)

fun Position.collidesWith(other: Position): Boolean =
        x == other.x && y == other.y

fun EntityGroup.getEntitiesAt(x: Int, y: Int): List<Entity> =
        entities.filter { entity ->
            entity.position?.let { position ->
                position.x == x && position.y == y
            } ?: false
        }
