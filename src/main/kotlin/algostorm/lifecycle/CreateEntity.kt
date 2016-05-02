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
 * Requests the creation of an entity with the given components.
 *
 * Should only be listened to by the [LifecycleSystem].
 *
 * @property components the components the created entity should contain
 */
data class CreateEntity(val components: Iterable<Component>) : Event
