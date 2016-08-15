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

package com.aheidelbacher.algostorm.engine.graphics2d.camera

import com.aheidelbacher.algostorm.engine.state.ObjectManager
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber

class CameraSystem(
        private val camera: Camera,
        private val objectManager: ObjectManager,
        private var followedObjectId: Int? = null
) : Subscriber {
    @Subscribe fun onUpdateCamera(event: UpdateCamera) {
        followedObjectId?.let { id ->
            objectManager[id]?.let { obj ->
                camera.x = obj.x + obj.width / 2
                camera.y = obj.y + obj.height / 2
            }
        }
    }

    @Subscribe fun onFocusOn(event: FocusOn) {
        camera.x = event.x
        camera.y = event.y
    }

    @Subscribe fun onFollow(event: Follow) {
        followedObjectId = event.objectId
    }

    @Subscribe fun onScroll(event: Scroll) {
        camera.x += event.dx
        camera.y += event.dy
    }

    @Subscribe fun onStopFollowing(event: StopFollowing) {
        followedObjectId = null
    }
}
