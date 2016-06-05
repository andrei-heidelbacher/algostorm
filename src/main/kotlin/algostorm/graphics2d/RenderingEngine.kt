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

package algostorm.graphics2d

import algostorm.ecs.Entity

/**
 * An object that handles rendering to the screen.
 *
 * Methods on this object will be called from the private engine thread. All
 * method calls should be thread-safe.
 */
interface RenderingEngine {
    /**
     * Draws the screen using the data from the received [entities]. This should
     * be a blocking call.
     *
     * @param tileCollection the [TileCollection] used for rendering
     * @param entities the lazy view of the current game state
     */
    fun render(tileCollection: TileCollection, entities: Sequence<Entity>): Unit
}
