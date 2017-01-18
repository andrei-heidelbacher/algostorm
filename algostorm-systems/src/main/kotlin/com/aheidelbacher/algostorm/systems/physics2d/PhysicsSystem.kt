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

import com.aheidelbacher.algostorm.core.ecs.EntityRef
import com.aheidelbacher.algostorm.core.ecs.EntityRef.Id
import com.aheidelbacher.algostorm.core.ecs.MutableEntityGroup
import com.aheidelbacher.algostorm.core.ecs.MutableEntityRef
import com.aheidelbacher.algostorm.event.Event
import com.aheidelbacher.algostorm.event.Publisher
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber
import com.aheidelbacher.algostorm.systems.physics2d.Body.KINEMATIC
import com.aheidelbacher.algostorm.systems.physics2d.Body.STATIC
import com.aheidelbacher.algostorm.systems.physics2d.Body.TRIGGER

/**
 * A system that handles [TransformIntent] events and publishes [Transformed],
 * [Collision] and [Triggered] events.
 *
 * [STATIC] entities are preprocessed at initialization time. If any static
 * entity is created, deleted or has its [Body] or [Position] components
 * changed, this system has undefined behavior.
 *
 * @property entityGroup the entity group used to retrieve and update the
 * entities
 */
class PhysicsSystem(
        private val entityGroup: MutableEntityGroup
) : Subscriber {
    companion object {
        /**
         * Transforms this non-body entity by the indicated amount.
         *
         * @param dx the horizontal translation amount in tiles
         * @param dy the vertical translation amount in tiles (positive is down)
         * @throws IllegalStateException if this entity is a [Body] or if it
         * doesn't have a [Position]
         */
        fun MutableEntityRef.transform(dx: Int, dy: Int) {
            check(!isBody) { "Can't transform $this directly if it's a body!" }
            val newPosition = position?.transformed(dx, dy)
                    ?: error("Can't transform $this without position!")
            set(newPosition)
        }
    }

    /**
     * An event which signals a transformation that should be applied on the
     * given [KINEMATIC] entity.
     *
     * @property entityId the id of the entity which should be transformed
     * @property dx the horizontal translation amount in tiles
     * @property dy the vertical translation amount in tiles (positive is down)
     */
    data class TransformIntent(
            val entityId: Id,
            val dx: Int,
            val dy: Int
    ) : Event

    private lateinit var publisher: Publisher
    private lateinit var kinematicBodies: MutableEntityGroup
    private lateinit var staticBodies: Map<Position, EntityRef>

    override fun onSubscribe(publisher: Publisher) {
        this.publisher = publisher
        kinematicBodies = entityGroup.addGroup {
            it.position != null && it.isKinematic
        }
        val map = hashMapOf<Position, EntityRef>()
        for (entity in entityGroup.entities) {
            val position = entity.position
            if (entity.isStatic && position != null) {
                map[position] = entity
            }
        }
        staticBodies = map
    }

    override fun onUnsubscribe(publisher: Publisher) {
        entityGroup.removeGroup(kinematicBodies)
        staticBodies = emptyMap()
    }

    /**
     * Upon receiving a [TransformIntent] event, the entity is transformed by
     * the indicated amount.
     *
     * If there are any colliders at the destination location, the entity is not
     * transformed and a [Collision] event is posted with every overlapping
     * collider. If the entity is transformed, a [Transformed] event is posted
     * and a [Triggered] event is posted for every [TRIGGER] which overlaps the
     * destination location.
     *
     * If the entity doesn't exist, isn't kinematic or doesn't have a position,
     * nothing happens.
     *
     * @param event the transform intent event
     */
    @Subscribe fun onTransformIntent(event: TransformIntent) {
        val entity = kinematicBodies[event.entityId] ?: return
        val nextPosition = entity.position?.transformed(event.dx, event.dy)
                ?: return
        val (nx, ny) = nextPosition
        val static = staticBodies[nextPosition]
        val kinematic = kinematicBodies.getEntitiesAt(nx, ny)
        if (static != null || kinematic.isNotEmpty()) {
            static?.let { publisher.post(Collision(entity.id, it.id)) }
            publisher.post(kinematic.map { Collision(entity.id, it.id) })
        } else {
            entity.set(nextPosition)
            publisher.post(Transformed(entity.id, event.dx, event.dy))
            val trigger = entityGroup.getEntitiesAt(nx, ny).filter {
                it != entity && it.isTrigger
            }
            publisher.post(trigger.map { Triggered(entity.id, it.id) })
        }
    }
}
