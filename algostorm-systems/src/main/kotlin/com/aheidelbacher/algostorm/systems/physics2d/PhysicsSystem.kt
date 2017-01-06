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

import com.aheidelbacher.algostorm.ecs.EntityRef
import com.aheidelbacher.algostorm.ecs.MutableEntityGroup
import com.aheidelbacher.algostorm.event.Event
import com.aheidelbacher.algostorm.event.Publisher
import com.aheidelbacher.algostorm.event.Request
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber
import com.aheidelbacher.algostorm.systems.physics2d.geometry2d.Direction

import java.util.PriorityQueue

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
    private lateinit var staticBodies: Map<Position, EntityRef>

    override fun onSubscribe(publisher: Publisher) {
        this.publisher = publisher
        kinematicBodies = entityGroup.addGroup(KINEMATIC_BODIES_GROUP) {
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
        entityGroup.removeGroup(KINEMATIC_BODIES_GROUP)
        staticBodies = emptyMap()
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

    class FindPath(
            val sourceId: Int,
            val destinationX: Int,
            val destinationY: Int,
            val directions: List<Direction> = Direction.ORDINAL,
            val ignoreColliderDestination: Boolean = true
    ) : Request<List<Direction>>()

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
            publisher.post(trigger.map { TriggerEntered(entity.id, it.id) })
        }
    }

    private fun findPath(
            source: Position,
            destination: Position,
            directions: List<Direction>,
            isCollider: (Position) -> Boolean
    ): List<Direction> {
        data class HeapNode(val p: Position, val f: Int) : Comparable<HeapNode> {
            override fun compareTo(other: HeapNode): Int = f - other.f
        }

        fun hScore(p : Position): Int = Math.max(
                Math.abs(p.x - destination.x),
                Math.abs(p.y - destination.y)
        )

        val INF = 0x0fffffff

        val visited = hashSetOf<Position>()
        val father = hashMapOf<Position, Direction>()
        val gScore = hashMapOf(source to 0)
        val fScore = hashMapOf(source to hScore(source))
        val heap = PriorityQueue<HeapNode>()
        heap.add(HeapNode(source, fScore[source] ?: INF))
        while (heap.isNotEmpty()) {
            val v = heap.poll().p
            if (v == destination) {
                val path = arrayListOf<Direction>()
                var head = destination
                while (head in father) {
                    father[head]?.let { d ->
                        path.add(d)
                        head = head.transformed(-d.dx, -d.dy)
                    }
                }
                path.reverse()
                return path
            }
            val vCost = gScore[v] ?: INF
            visited.add(v)
            for (d in directions) {
                val w = v.transformed(d.dx, d.dy)
                val wCost = gScore[w] ?: INF
                if (!isCollider(w) && w !in visited && vCost + 1 < wCost) {
                    gScore[w] = vCost + 1
                    father[w] = d
                    heap.add(HeapNode(w, vCost + 1 + hScore(w)))
                }
            }
        }
        return emptyList()
    }

    @Subscribe fun onFindPath(request: FindPath) {
        val kinematic = kinematicBodies.entities
                .mapNotNullTo(hashSetOf(), EntityRef::position)
        val source = checkNotNull(entityGroup[request.sourceId]?.position)
        val destination = Position(request.destinationX, request.destinationY)
        request.complete(findPath(source, destination, request.directions) {
            (it in kinematic || it in staticBodies)
                    && (it != destination || request.ignoreColliderDestination)
        })
    }
}
