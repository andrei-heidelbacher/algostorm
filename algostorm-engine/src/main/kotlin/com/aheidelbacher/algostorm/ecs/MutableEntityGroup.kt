/*
 * Copyright (c) 2016  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.aheidelbacher.algostorm.ecs

/** A read-only view of a group of filtered mutable entities. */
interface MutableEntityGroup : EntityGroup {
    override val entities: Iterable<MutableEntityRef>

    override fun get(id: Int): MutableEntityRef?

    override fun addGroup(
            name: String,
            filter: (EntityRef) -> Boolean
    ): MutableEntityGroup?
}
