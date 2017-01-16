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

import com.aheidelbacher.algostorm.ecs.EntityPool
import com.aheidelbacher.algostorm.ecs.EntityRef.Id
import com.aheidelbacher.algostorm.ecs.MutableEntityRef
import com.aheidelbacher.algostorm.ecs.Prefab
import com.aheidelbacher.algostorm.event.Request
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber

/**
 * A system that handles entity creation and deletion requests.
 *
 * @property entityPool the entity pool used to create and delete entities
 */
class LifecycleSystem(private val entityPool: EntityPool) : Subscriber {
    /**
     * A request to create an entity from the given [prefab].
     *
     * @property prefab the prefab containing the initial components of the
     * entity
     */
    class Create(val prefab: Prefab) : Request<MutableEntityRef>()

    /**
     * A request to delete the entity with the given [id].
     *
     * @property id the id of the entity which should be deleted
     */
    class Delete(val id: Id) : Request<Boolean>()

    @Subscribe fun onCreate(request: Create) {
        request.complete(entityPool.create(request.prefab))
    }

    @Subscribe fun onDelete(request: Delete) {
        request.complete(entityPool.remove(request.id))
    }
}
