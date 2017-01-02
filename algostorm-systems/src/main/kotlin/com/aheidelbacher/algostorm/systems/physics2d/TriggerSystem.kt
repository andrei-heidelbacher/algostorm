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

import com.aheidelbacher.algostorm.ecs.EntityGroup
import com.aheidelbacher.algostorm.ecs.EntityRef
import com.aheidelbacher.algostorm.event.Publisher
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber

class TriggerSystem(private val entityGroup: EntityGroup) : Subscriber {
    companion object {
        const val TRIGGERS_GROUP: String = "triggers"
    }

    private lateinit var publisher: Publisher
    private lateinit var triggers: EntityGroup

    override fun onSubscribe(publisher: Publisher) {
        this.publisher = publisher
        triggers = entityGroup.addGroup(TRIGGERS_GROUP) { it.isTrigger }
    }

    override fun onUnsubscribe(publisher: Publisher) {
        entityGroup.removeGroup(TRIGGERS_GROUP)
    }

    @Subscribe fun onTransformed(event: Transformed) {
        val entity = entityGroup[event.entityId] ?: return
        val (x, y) = entity.position ?: error("")
        if (!entity.isRigid) return
        val entered = triggers.getEntitiesAt(x, y)
        val exited = triggers.getEntitiesAt(x - event.dx, y - event.dy)
        entered.forEach { publisher.post(TriggerEntered(entity.id, it.id)) }
        exited.forEach { publisher.post(TriggerExited(entity.id, it.id)) }
    }
}
