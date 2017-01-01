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

package com.aheidelbacher.algostorm.systems.graphics2d

import com.aheidelbacher.algostorm.ecs.EntityGroup
import com.aheidelbacher.algostorm.ecs.EntityManager
import com.aheidelbacher.algostorm.event.Event
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber
import com.aheidelbacher.algostorm.systems.physics2d.position

class CameraSystem(
        private val camera: Camera,
        private val entityGroup: EntityGroup,
        private var followedEntityId: Int? = null
) : Subscriber {
    object UpdateCamera : Event

    data class FocusOn(val x: Int, val y: Int) : Event

    data class Follow(val entityId: Int) : Event

    data class Scroll(val dx: Int, val dy: Int) : Event

    object UnFollow : Event

    @Suppress("unused_parameter")
    @Subscribe fun onUpdateCamera(event: UpdateCamera) {
        followedEntityId?.let { id ->
            entityGroup[id]?.position?.let { entity ->
                camera.x = entity.x //+ obj.width / 2
                camera.y = entity.y //+ obj.height / 2
            }
        }
    }

    @Subscribe fun onFocusOn(event: FocusOn) {
        followedEntityId = null
        camera.x = event.x
        camera.y = event.y
    }

    @Subscribe fun onFollow(event: Follow) {
        followedEntityId = event.entityId
    }

    @Subscribe fun onScroll(event: Scroll) {
        followedEntityId = null
        camera.x += event.dx
        camera.y += event.dy
    }

    @Suppress("unused_parameter")
    @Subscribe fun onUnFollow(event: UnFollow) {
        followedEntityId = null
    }
}
