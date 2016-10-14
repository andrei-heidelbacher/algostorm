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

package com.aheidelbacher.algostorm.systems.graphics2d.camera

import com.aheidelbacher.algostorm.event.Event
import com.aheidelbacher.algostorm.event.Publisher
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber
import com.aheidelbacher.algostorm.state.Layer.ObjectGroup
import com.aheidelbacher.algostorm.systems.Update

class CameraSystem(
        private val camera: Camera,
        private val objectGroup: ObjectGroup,
        private val publisher: Publisher,
        private var followedObjectId: Int? = null
) : Subscriber {
    data class FocusOn(val x: Int, val y: Int) : Event

    data class Follow(val objectId: Int) : Event

    data class Scroll(val dx: Int, val dy: Int) : Event

    object UnFollow : Event

    object UpdateCamera : Event

    @Subscribe fun onUpdateCamera(event: UpdateCamera) {
        followedObjectId?.let { id ->
            objectGroup[id]?.let { obj ->
                camera.x = obj.x //+ obj.width / 2
                camera.y = obj.y //+ obj.height / 2
            }
        }
    }

    @Subscribe fun onFocusOn(event: FocusOn) {
        followedObjectId = null
        camera.x = event.x
        camera.y = event.y
    }

    @Subscribe fun onFollow(event: Follow) {
        followedObjectId = event.objectId
    }

    @Subscribe fun onScroll(event: Scroll) {
        followedObjectId = null
        camera.x += event.dx
        camera.y += event.dy
    }

    @Subscribe fun onUnFollow(event: UnFollow) {
        followedObjectId = null
    }

    @Subscribe fun onUpdate(event: Update) {
        publisher.post(UpdateCamera)
    }
}
