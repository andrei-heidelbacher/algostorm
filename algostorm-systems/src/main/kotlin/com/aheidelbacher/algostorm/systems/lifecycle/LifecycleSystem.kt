/*
 * Copyright (c) 2017  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aheidelbacher.algostorm.systems.lifecycle

import com.aheidelbacher.algostorm.ecs.Component
import com.aheidelbacher.algostorm.ecs.EntityPool
import com.aheidelbacher.algostorm.ecs.EntityRef.Companion.validateComponents
import com.aheidelbacher.algostorm.ecs.EntityRef.Companion.validateId
import com.aheidelbacher.algostorm.ecs.MutableEntityRef
import com.aheidelbacher.algostorm.event.Request
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber


class LifecycleSystem(private val entityPool: EntityPool) : Subscriber {
    class Create(
            val components: Collection<Component>
    ) : Request<MutableEntityRef>() {
        init {
            validateComponents(components)
        }
    }

    class Delete(val id: Int) : Request<Boolean>() {
        init {
            validateId(id)
        }
    }

    @Subscribe fun onCreate(request: Create) {
        request.complete(entityPool.create(request.components))
    }

    @Subscribe fun onDelete(request: Delete) {
        request.complete(entityPool.delete(request.id))
    }
}
