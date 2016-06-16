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
 * When a [Render] event is received, the [RenderingEngine.render] method is
 * called.
 *
 * @property renderingEngine the engine that will render the game entities to
 * the screen
 * @property entityManager an entity manager which can be queried to fetch
 * renderable entities
 * @property properties the properties of the game
 */
class RenderingSystem(
        private val renderingEngine: RenderingEngine,
        private val entityManager: EntityManager,
        private val properties: Map<String, Any>
) : Subscriber {
    companion object {
        /**
         * The name of a property used by this system. It should be an object of
         * type [TileCollection].
         */
        const val TILE_COLLECTION: String = "tileCollection"

        /**
         * The name of a property used by this system. It should be an [Int] and
         * should be expressed in pixels.
         */
        const val TILE_WIDTH: String = "tileWidth"

        /**
         * The name of a property used by this system. It should be an [Int] and
         * should be expressed in pixels.
         */
        const val TILE_HEIGHT: String = "tileHeight"
    }

    private val tileCollection: TileCollection
        get() = properties[TILE_COLLECTION] as TileCollection

    private val tileWidth: Int
        get() = properties[TILE_WIDTH] as Int

    private val tileHeight: Int
        get() = properties[TILE_HEIGHT] as Int

    @Subscribe fun handleRender(event: Render) {
        renderingEngine.render(
                tileCollection = tileCollection,
                tileWidth = tileWidth,
                tileHeight = tileHeight,
                entities = entityManager.entities
        )
    }
}
