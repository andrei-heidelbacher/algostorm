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
import com.andreihh.algostorm.core.event.Request
import com.andreihh.algostorm.core.event.Subscribe
import com.andreihh.algostorm.systems.EventSystem
import com.andreihh.algostorm.systems.physics2d.geometry2d.Direction
import java.util.PriorityQueue

class PathFindingSystem : EventSystem() {
    companion object {
        /**
         * Returns the path going from `source` to `destination` using the given
         * `directions` and avoiding the given colliders.
         *
         * @param source the source location
         * @param destination the destination location
         * @param directions the directions used for movement
         * @param isCollider the map which contains all colliders
         * @return the directions in which the `source` should move in order to
         * reach the `destination`, or `null` if the `destination` can't be
         * reached
         */
        fun findPath(
                source: Position,
                destination: Position,
                directions: List<Direction>,
                isCollider: (Position) -> Boolean
        ): List<Direction>? {
            data class HeapNode(
                    val p: Position,
                    val f: Int
            ) : Comparable<HeapNode> {
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
            return null
        }
    }

    private val entities: EntityGroup by context(ENTITY_POOL)

    class FindPath(
            val sourceId: Id,
            val destinationX: Int,
            val destinationY: Int,
            val directions: List<Direction> = Direction.ORDINAL,
            val ignoreColliderDestination: Boolean = true
    ) : Request<List<Direction>?>()

    @Subscribe
    fun onFindPath(request: FindPath) {
        val colliders = entities.filter { it.isCollider }
                .mapNotNullTo(hashSetOf(), EntityRef::position)
        val source = checkNotNull(entities[request.sourceId]?.position)
        val destination = Position(request.destinationX, request.destinationY)
        request.complete(findPath(source, destination, request.directions) {
            it in colliders
                    && (it != destination || request.ignoreColliderDestination)
        })
    }
}
