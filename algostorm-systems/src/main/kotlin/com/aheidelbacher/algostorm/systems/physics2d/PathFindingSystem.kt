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

import com.aheidelbacher.algostorm.ecs.EntityGroup
import com.aheidelbacher.algostorm.event.Request
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber
import com.aheidelbacher.algostorm.systems.physics2d.geometry2d.Direction
import com.aheidelbacher.algostorm.systems.physics2d.geometry2d.Point
import java.util.PriorityQueue

class PathFindingSystem(private val entityGroup: EntityGroup) : Subscriber {
    class FindPath(
            val sourceId: Int,
            val destinationX: Int,
            val destinationY: Int,
            val directions: List<Direction> = Direction.ORDINAL,
            val ignoreRigidDestination: Boolean = true
    ) : Request<List<Direction>?>()

    private fun findPath(
            source: Point,
            destination: Point,
            isRigid: (Point) -> Boolean
    ): List<Direction>? {
        data class HeapNode(val p: Point, val f: Int) : Comparable<HeapNode> {
            override fun compareTo(other: HeapNode): Int = f - other.f
        }

        fun hScore(p : Point): Int = Math.max(
                Math.abs(p.x - destination.x),
                Math.abs(p.y - destination.y)
        )

        val INF = 0x0fffffff

        val visited = hashSetOf<Point>()
        val father = hashMapOf<Point, Direction>()
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
                        head = head.translate(-d.dx, -d.dy)
                    }
                }
                path.reverse()
                return path
            }
            val vCost = gScore[v] ?: INF
            visited.add(v)
            for (d in Direction.values()) {
                val w = v.translate(d.dx, d.dy)
                val wCost = gScore[w] ?: INF
                if (!isRigid(w) && w !in visited && vCost + 1 < wCost) {
                    gScore[w] = vCost + 1
                    father[w] = d
                    heap.add(HeapNode(w, vCost + 1 + hScore(w)))
                }
            }
        }
        return null
    }

    @Subscribe fun onFindPath(request: FindPath) {
        val rigid = hashSetOf<Point>()
        for (entity in entityGroup.entities) {
            if (entity.isRigid) {
            }
        }
        val source = entityGroup[request.sourceId]?.position?.let {
            Point(it.x, it.y)
        } ?: error("")
        val destination = Point(request.destinationX, request.destinationY)
        request.complete(findPath(source, destination) {
            it in rigid && (it != destination || request.ignoreRigidDestination)
        })
    }
}
