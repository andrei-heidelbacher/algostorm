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

import com.aheidelbacher.algostorm.event.Request
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber
import com.aheidelbacher.algostorm.systems.physics2d.geometry2d.Direction

class PathFindingSystem : Subscriber {
    class FindPath(
            sourceId: Int,
            destinationX: Int,
            destinationY: Int,
            directions: List<Direction> = Direction.ORDINAL,
            ignoreRigidDestination: Boolean = true
    ) : Request<List<Direction>?>()

    @Subscribe
    fun onFindPath(request: FindPath) {
        request.complete(null)
    }
}
