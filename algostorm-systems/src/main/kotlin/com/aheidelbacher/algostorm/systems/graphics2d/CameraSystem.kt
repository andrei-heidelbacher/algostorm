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

import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.Canvas
import com.aheidelbacher.algostorm.core.ecs.EntityGroup
import com.aheidelbacher.algostorm.core.ecs.EntityRef.Id
import com.aheidelbacher.algostorm.core.event.Event
import com.aheidelbacher.algostorm.core.event.Subscribe
import com.aheidelbacher.algostorm.systems.physics2d.position

class CameraSystem : GraphicsSystem() {
    private val group: EntityGroup by context(ENTITY_POOL)
    private val camera: Camera by context(CAMERA)
    private val canvas: Canvas by context(CANVAS)
    private var followedEntityId: Id? = null

    object UpdateCamera : Event

    data class FocusOn(val x: Int, val y: Int) : Event

    data class Follow(val entityId: Id) : Event

    data class Scroll(val dx: Int, val dy: Int) : Event

    object UnFollow : Event

    @Suppress("unused_parameter")
    @Subscribe fun onUpdateCamera(event: UpdateCamera) {
        camera.resize(canvas.width, canvas.height)
        val id = followedEntityId ?: return
        val entity = group[id] ?: return
        val (tx, ty) = entity.position ?: return
        val x = tileWidth * tx + tileWidth / 2
        val y = tileHeight * ty + tileHeight / 2
        camera.focusOn(x, y)
    }

    @Subscribe fun onFocusOn(event: FocusOn) {
        followedEntityId = null
        camera.focusOn(event.x, event.y)
    }

    @Subscribe fun onFollow(event: Follow) {
        followedEntityId = event.entityId
    }

    @Subscribe fun onScroll(event: Scroll) {
        followedEntityId = null
        camera.translate(event.dx, event.dy)
    }

    @Suppress("unused_parameter")
    @Subscribe fun onUnFollow(event: UnFollow) {
        followedEntityId = null
    }
}
