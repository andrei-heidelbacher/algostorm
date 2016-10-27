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

import com.aheidelbacher.algostorm.event.Event
import com.aheidelbacher.algostorm.event.Publisher
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber
import com.aheidelbacher.algostorm.state.Layer.ObjectGroup
import com.aheidelbacher.algostorm.state.Object

/**
 * A system that handles [TransformIntent] events and publishes [Transformed]
 * and [Collision] events.
 *
 * @property objectGroup the object group used to retrieve and update the
 * objects
 * @property publisher the publisher used to post `Transformed` and `Collision`
 * events
 */
class PhysicsSystem(private val objectGroup: ObjectGroup) : Subscriber {
    companion object {
        /**
         * The name of the rigid property. It is of type [Boolean].
         *
         * A rigid object will block movement and trigger collisions.
         */
        const val IS_RIGID: String = "isRigid"

        /** Returns whether this object contains the [IS_RIGID] property. */
        val Object.isRigid: Boolean
            get() = getBoolean(IS_RIGID) ?: false

        /**
         * Transforms this object with the given amounts.
         *
         * @param dx the horizontal translation amount
         * @param dy the vertical translation amount (positive is down)
         */
        fun Object.transform(dx: Int, dy: Int) {
            x += dx
            y += dy
        }

        /**
         * Returns whether the two objects intersect (that is, there exists a
         * pixel `(x, y)` that lies inside both objects).
         *
         * @param other the object with which the intersection is checked
         * @return `true` if the two objects overlap, `false` otherwise
         */
        fun Object.intersects(other: Object): Boolean = intersects(
                x = x,
                y = y - height + 1,
                width = width,
                height = height,
                otherX = other.x,
                otherY = other.y - other.height + 1,
                otherWidth = other.width,
                otherHeight = other.height
        )

        fun Object.intersects(
                x: Int,
                y: Int,
                width: Int,
                height: Int
        ): Boolean = intersects(
                x = this.x,
                y = this.y - this.height + 1,
                width = this.width,
                height = this.height,
                otherX = x,
                otherY = y,
                otherWidth = width,
                otherHeight = height
        )
    }

    private lateinit var publisher: Publisher

    override fun onSubscribe(publisher: Publisher) {
        this.publisher = publisher
    }

    /**
     * An event which signals a transformation that should be applied on the
     * given object.
     *
     * @property objectId the id of the object which should be transformed
     * @property dx the translation amount on the x-axis in pixels
     * @property dy the translation amount on the y-axis in pixels
     */
    data class TransformIntent(
            val objectId: Int,
            val dx: Int,
            val dy: Int
    ) : Event

    /**
     * Upon receiving a [TransformIntent] event, the the object is transformed
     * by the indicated amount. If the moved object is rigid and there are any
     * other rigid objects with their boxes overlapping the destination
     * location, the object is restored to it's initial location and a
     * [Collision] event is triggered with every overlapping object, having this
     * object as the source and each other object as the target.
     *
     * @param event the [TransformIntent] event
     */
    @Subscribe fun onTranslateIntent(event: TransformIntent) {
        objectGroup[event.objectId]?.let { obj ->
            obj.transform(event.dx, event.dy)
            val overlappingObjects = objectGroup.objectSet.filter {
                it != obj && it.isRigid && it.intersects(obj)
            }
            if (!obj.isRigid || overlappingObjects.size == 0) {
                publisher.post(Transformed(event.objectId, event.dx, event.dy))
            } else {
                obj.transform(-event.dx, -event.dy)
                overlappingObjects.forEach {
                    publisher.post(Collision(event.objectId, it.id))
                }
            }
        }
    }
}
