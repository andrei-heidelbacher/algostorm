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

import com.andreihh.algostorm.core.ecs.EntityGroup
import com.andreihh.algostorm.core.ecs.EntityRef
import com.andreihh.algostorm.core.ecs.EntityRef.Id
import com.andreihh.algostorm.core.event.Event
import com.andreihh.algostorm.core.event.Subscribe
import com.andreihh.algostorm.systems.EventSystem
import com.andreihh.algostorm.systems.physics2d.Body.KINEMATIC
import com.andreihh.algostorm.systems.physics2d.Body.STATIC
import com.andreihh.algostorm.systems.physics2d.Body.TRIGGER
import com.andreihh.algostorm.systems.physics2d.PhysicsSystem.TransformIntent

/**
 * A system that handles [TransformIntent] events and publishes [Transformed],
 * [Collision] and [Triggered] events.
 *
 * [STATIC] entities are preprocessed at initialization time. If any static
 * entity is created, deleted or has its [Body] or [Position] components
 * changed, this system has undefined behavior.
 *
 * @property group the entity group used to retrieve and update the entities
 */
class PhysicsSystem : EventSystem() {
    companion object {
        /**
         * Transforms this non-body entity by the indicated amount.
         *
         * @param dx the horizontal translation amount in tiles
         * @param dy the vertical translation amount in tiles (positive is down)
         * @throws IllegalStateException if this entity is a [Body] or if it
         * doesn't have a [Position]
         */
        fun EntityRef.transform(dx: Int, dy: Int) {
            check(!isBody) { "Can't transform $this directly if it's a body!" }
            val newPosition = position?.transformed(dx, dy)
                    ?: error("Can't transform $this without position!")
            set(newPosition)
        }
    }

    private val entities: EntityGroup by context(ENTITY_POOL)

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

    private val kinematicBodies: EntityGroup =
        entities.filter { it.position != null && it.isKinematic }

    private lateinit var staticBodies: Map<Position, EntityRef>

    override fun onStart() {
        super.onStart()
        val map = hashMapOf<Position, EntityRef>()
        for (entity in entities) {
            val position = entity.position
            if (entity.isStatic && position != null) {
                map[position] = entity
            }
        }
        staticBodies = map
    }

    override fun onStop() {
        super.onStop()
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
    @Subscribe
    fun onTransformIntent(event: TransformIntent) {
        val entity = kinematicBodies[event.entityId] ?: return
        val nextPosition = entity.position?.transformed(event.dx, event.dy)
                ?: return
        val (nx, ny) = nextPosition
        val static = staticBodies[nextPosition]
        val kinematic = kinematicBodies.getEntitiesAt(nx, ny)
        if (static != null || kinematic.isNotEmpty()) {
            static?.let { post(Collision(entity.id, it.id)) }
            post(kinematic.map { Collision(entity.id, it.id) })
        } else {
            entity.set(nextPosition)
            post(Transformed(entity.id, event.dx, event.dy))
            val trigger = entities.getEntitiesAt(nx, ny).filter {
                it != entity && it.isTrigger
            }
            post(trigger.map { Triggered(entity.id, it.id) })
        }
    }
}
