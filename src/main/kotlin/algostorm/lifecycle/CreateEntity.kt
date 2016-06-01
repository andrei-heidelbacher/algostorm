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

package algostorm.lifecycle

import algostorm.ecs.Component
import algostorm.event.Event

/**
 * Requests the creation of an entity with the given [components] and requests
 * the [onSpawned] events to be published right after the entity is [Spawned].
 *
 * Should only be listened to by the [LifecycleSystem].
 *
 * @property components the components the created entity should contain
 * @property onSpawned a sequence of events that should be posted after the
 * entity is spawned
 */
data class CreateEntity(
        val components: Iterable<Component>,
        val onSpawned: List<EventBuilder>
) : Event {
    /**
     * An object which encapsulates the data required to build an [Event] whose
     * subject is an entity.
     *
     * The entity id, which is not available until after the entity is created,
     * is provided through the [build] method.
     *
     * Concrete event builders should not be generic, otherwise they may not be
     * serializable. It is recommended to not use anonymous classes to implement
     * the interface, as that may also lead to serialization issues.
     */
    interface EventBuilder {
        /**
         * Builds the encapsulated event by providing the [entityId].
         *
         * @param entityId the id of the entity
         * @return the encapsulated event
         */
        fun build(entityId: Int): Event
    }

    /**
     * Requests the creation of an entity with the given `components`.
     *
     * @param components the components the created entity should contain
     */
    constructor(components: Iterable<Component>) : this(components, emptyList())
}
