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

package com.aheidelbacher.algostorm.state.builders

import com.aheidelbacher.algostorm.state.Entity
import com.aheidelbacher.algostorm.state.Layer.EntityGroup

class EntityGroupBuilder {
    lateinit var name: String
    var isVisible: Boolean = true
    var offsetX: Int = 0
    var offsetY: Int = 0
    val entities: MutableSet<Entity> = hashSetOf()

    operator fun Entity.unaryPlus() {
        entities.add(this)
    }

    fun build(): EntityGroup = EntityGroup(
            name = name,
            isVisible = isVisible,
            offsetX = offsetX,
            offsetY = offsetY,
            entities = entities.toSet()
    )
}
