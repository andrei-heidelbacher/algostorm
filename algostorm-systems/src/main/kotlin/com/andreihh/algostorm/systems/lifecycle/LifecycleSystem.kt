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

package com.andreihh.algostorm.systems.lifecycle

import com.andreihh.algostorm.core.ecs.Component
import com.andreihh.algostorm.core.ecs.EntityPool
import com.andreihh.algostorm.core.ecs.EntityRef
import com.andreihh.algostorm.core.ecs.EntityRef.Id
import com.andreihh.algostorm.core.event.Request
import com.andreihh.algostorm.core.event.Subscribe
import com.andreihh.algostorm.systems.EventSystem

/**
 * A system that handles entity creation and deletion requests.
 *
 * @property entityPool the entity pool used to create and delete entities
 */
class LifecycleSystem : EventSystem() {
    private val entityPool by context<EntityPool>(ENTITY_POOL)

    /**
     * A request to create an entity from the given [components].
     *
     * @property components the initial components of the entity
     */
    class Create(val components: Collection<Component>) : Request<EntityRef>()

    /**
     * A request to delete the entity with the given [id].
     *
     * @property id the id of the entity which should be deleted
     */
    class Delete(val id: Id) : Request<Boolean>()

    @Subscribe
    fun onCreate(request: Create) {
        request.complete(entityPool.create(request.components))
    }

    @Subscribe
    fun onDelete(request: Delete) {
        request.complete(entityPool.remove(request.id))
    }
}
