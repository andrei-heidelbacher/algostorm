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

import com.aheidelbacher.algostorm.core.ecs.EntityGroup
import com.aheidelbacher.algostorm.core.ecs.EntityRef.Id
import com.aheidelbacher.algostorm.core.event.Event
import com.aheidelbacher.algostorm.core.event.Service
import com.aheidelbacher.algostorm.core.event.Subscribe
import com.aheidelbacher.algostorm.systems.physics2d.position

class CameraService(
        private val group: EntityGroup,
        private val tileWidth: Int,
        private val tileHeight: Int,
        private val camera: Camera,
        private var followedEntityId: Id? = null
) : Service() {
    object UpdateCamera : Event

    data class FocusOn(val x: Int, val y: Int) : Event

    data class Follow(val entityId: Id) : Event

    data class Scroll(val dx: Int, val dy: Int) : Event

    object UnFollow : Event

    @Suppress("unused_parameter")
    @Subscribe fun onUpdateCamera(event: UpdateCamera) {
        followedEntityId?.let { id ->
            group[id]?.let { entity ->
                val (x, y) = entity.position ?: return
                camera.x = tileWidth * x + tileWidth / 2
                camera.y = tileHeight * y + tileHeight / 2
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
