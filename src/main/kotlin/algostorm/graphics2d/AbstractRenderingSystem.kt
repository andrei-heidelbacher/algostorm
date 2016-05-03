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

import algostorm.assets.FontSet
import algostorm.assets.TileSet
import algostorm.ecs.EntityManager
import algostorm.ecs.EntitySystem
import algostorm.event.Subscriber

/**
 * A system which handles the rendering of all entities in the game upon [RenderAll] events.
 *
 * @property entityManager an entity manager which can be queried to fetch renderable entities
 * @property tileWidth the width of a single tile in pixels
 * @property tileHeight the height of a single tile in pixels
 * @property tileSets the tilesets used for rendering
 * @property fonts the fonts used for rendering
 */
abstract class AbstractRenderingSystem(
    protected val entityManager: EntityManager,
    protected val tileWidth: Int,
    protected val tileHeight: Int,
    private val tileSets: Iterable<TileSet>,
    protected val fonts: FontSet
) : EntitySystem() {
  /**
   * Returns the tileset that contains the given [tileId].
   *
   * @param tileId the unique identifier of the tile
   * @return the tileset that contains the given tile, or `null` if no tileset contains it
   */
  protected fun getTileSet(tileId: Int): TileSet? = tileSets.find { tileSet -> tileId in tileSet }

  /**
   * This method is called when a [RenderAll] event is received.
   *
   * It should implement the rendering of all entities in the game.
   */
  protected abstract fun renderAll(): Unit

  private val renderHandler = Subscriber(RenderAll::class) { event -> renderAll() }

  /**
   * This system handles [RenderAll] events.
   */
  final override val handlers: List<Subscriber<*>> = listOf(renderHandler)
}
