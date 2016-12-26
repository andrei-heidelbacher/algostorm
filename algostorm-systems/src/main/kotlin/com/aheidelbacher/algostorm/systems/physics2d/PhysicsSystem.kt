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

import com.aheidelbacher.algostorm.ecs.MutableEntityGroup
import com.aheidelbacher.algostorm.event.Event
import com.aheidelbacher.algostorm.event.Publisher
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber

/**
 * A system that handles [TransformIntent] events and publishes [Transformed]
 * and [Collision] events.
 *
 * @property entityGroup the entity group used to retrieve and update the
 * entities
 */
class PhysicsSystem(
        private val entityGroup: MutableEntityGroup
) : Subscriber {
    private lateinit var publisher: Publisher

    override fun onSubscribe(publisher: Publisher) {
        this.publisher = publisher
    }

    /**
     * An event which signals a transformation that should be applied on the
     * given entity.
     *
     * @property entityId the id of the entity which should be transformed
     * @property dx the horizontal translation amount in tiles
     * @property dy the vertical translation amount in tiles (positive is down)
     */
    data class TransformIntent(
            val entityId: Int,
            val dx: Int,
            val dy: Int
    ) : Event

    /**
     * Upon receiving a [TransformIntent] event, the entity is transformed by
     * the indicated amount. If the moved entity is rigid and there are any
     * other rigid entities with their boxes overlapping the destination
     * location, the entity is not transformed and a [Collision] event is
     * triggered with every overlapping entity.
     *
     * @param event the [TransformIntent] event
     * @throws IllegalStateException if the transformed entity doesn't have a
     * [Position] component
     */
    @Subscribe fun onTransformIntent(event: TransformIntent) {
        val entity = entityGroup[event.entityId] ?: return
        val nextPosition = entity.position?.transformed(event.dx, event.dy)
                ?: error("Can't transform $entity without a position!")
        val collidingEntities = entityGroup.entities.filter {
            it != entity && it.isRigid && it.position == nextPosition
        }
        if (!entity.isRigid || collidingEntities.isEmpty()) {
            entity.set(nextPosition)
            publisher.post(Transformed(entity.id, event.dx, event.dy))
        } else {
            collidingEntities.forEach {
                publisher.post(Collision(entity.id, it.id))
            }
        }
    }
}