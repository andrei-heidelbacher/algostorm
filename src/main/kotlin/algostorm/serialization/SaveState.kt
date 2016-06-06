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

package algostorm.serialization

import algostorm.ecs.Component

/**
 * The data contained in a game, which is serialized when the game is saved.
 *
 * @property entities the entities present in the game
 * @property properties the properties of the game
 */
data class SaveState(
        val entities: Map<Int, List<Component>>,
        val properties: Map<String, Any>
)
