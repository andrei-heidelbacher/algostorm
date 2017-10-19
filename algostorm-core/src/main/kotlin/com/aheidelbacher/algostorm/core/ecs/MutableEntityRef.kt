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

package com.aheidelbacher.algostorm.core.ecs

import kotlin.reflect.KClass

/**
 * A mutable view of an entity.
 *
 * An invalid mutable entity reference cannot be modified.
 */
abstract class MutableEntityRef protected constructor(
        owner: EntityPool,
        id: Id
) : EntityRef(owner, id) {
    /**
     * Sets the value of the specified `component` type.
     *
     * @param component the new value of the component type
     */
    abstract fun set(component: Component)

    /**
     * Removes the component with the specified `type` and returns it.
     *
     * @param T the type of the component; must be final
     * @param type the class object of the component
     * @return the removed component if it exists in this entity when this
     * method is called, `null` otherwise
     */
    abstract fun <T : Component> remove(type: KClass<T>): T?
}
