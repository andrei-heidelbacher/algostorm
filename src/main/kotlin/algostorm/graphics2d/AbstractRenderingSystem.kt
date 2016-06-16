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

import algostorm.ecs.EntityManager
import algostorm.event.Subscribe
import algostorm.event.Subscriber

/**
 * A system which handles the rendering of all entities in the game.
 *
 * When a [Render] event is received, the [render] method is called.
 *
 * @property entityManager an entity manager which can be queried to fetch
 * renderable entities
 */
abstract class AbstractRenderingSystem(
        protected val entityManager: EntityManager,
        protected val tileCollection: TileCollection,
        protected val tileWidth: Int,
        protected val tileHeight: Int
) : Subscriber {
    protected abstract fun render(): Unit

    @Subscribe fun handleRender(event: Render) {
        render()
    }
}
