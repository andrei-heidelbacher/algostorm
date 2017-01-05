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
    companion object {
        const val KINEMATIC_BODIES_GROUP: String = "kinematic-bodies"
        fun Position.transformed(dx: Int, dy: Int): Position =
                copy(x = x + dx, y = y + dy)
    }

    private lateinit var publisher: Publisher
    private lateinit var kinematicBodies: MutableEntityGroup
    private lateinit var staticIds: Map<Position, Int>

    override fun onSubscribe(publisher: Publisher) {
        this.publisher = publisher
        kinematicBodies = entityGroup.addGroup(KINEMATIC_BODIES_GROUP) {
            it.position != null && it.isKinematic
        }
        val map = hashMapOf<Position, Int>()
        for (entity in entityGroup.entities) {
            val position = entity.position
            if (entity.isStatic && position != null) {
                map[position] = entity.id
            }
        }
        staticIds = map
    }

    override fun onUnsubscribe(publisher: Publisher) {
        entityGroup.removeGroup(KINEMATIC_BODIES_GROUP)
        staticIds = emptyMap()
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
        val currentPosition = entity.position
                ?: error("Can't transform $entity without a position!")
        val nextPosition = currentPosition.transformed(event.dx, event.dy)
        val bodies = entityGroup.entities.filter {
            it != entity && Body::class in it && it.position == nextPosition
        }
        /*val (collided, enteredTriggers) = bodies.partition { it.isRigid }
        val exitedTriggers = entityGroup.entities.filter {
            it != entity && it.isTrigger && it.position == currentPosition
        }
        if (!entity.isRigid || collided.isEmpty()) {
            entity.set(nextPosition)
            publisher.post(Transformed(entity.id, event.dx, event.dy))
            if (entity.isRigid) {
                publisher.post(exitedTriggers.map {
                    TriggerExited(entity.id, it.id)
                })
                publisher.post(enteredTriggers.map {
                    TriggerEntered(entity.id, it.id)
                })
            }
        } else {
            publisher.post(collided.map { Collision(entity.id, it.id) })
        }*/
    }
}
