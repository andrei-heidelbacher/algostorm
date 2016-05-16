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

package algostorm.script

import algostorm.ecs.EntityManager
import algostorm.event.Publisher

/**
 * The context of every executed script through a [RunScript] request. This should be passed as the
 * first argument to the executed scripts.
 *
 * @property entityManager a read-only view of the entity manager which handles the game entities
 * @property publisher a publisher which provides posting functionality to the game event bus
 */
data class ScriptContext(val entityManager: EntityManager, val publisher: Publisher)
