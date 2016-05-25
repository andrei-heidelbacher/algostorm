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

package algostorm.ecs

import kotlin.reflect.KClass

/**
 * An object with the role of a database that allows fetching [entities] that exist in the game.
 *
 * This is a read-only view, but the underlying implementation might be mutable.
 */
interface EntityManager {
  /**
   * A lazy view of all the entities in this manager.
   *
   * Creation and deletion of entities will be reflected in the returned view and might lead to a
   * concurrent modification exception.
   */
  val entities: Sequence<Entity>

  /**
   * Fetches the given entity from this manager.
   *
   * @param entityId the id of the requested entity
   * @return the entity with the given id, or `null` if it doesn't exist
   */
  operator fun get(entityId: Int): Entity?

  /**
   * Returns all entities that have a component of the given [type].
   *
   * @param type the component type the entities must contain
   * @return all entities that have a component of the requested type
   */
  fun <T : Component> getEntitiesWithComponentType(type: KClass<T>): Sequence<Entity>

  /**
   * Returns all entities that have a component of each provided type.
   *
   * @param types the component types the requested entities must contain
   * @return all the entities in the manager that contain the requested component types
   */
  fun getEntitiesWithComponentTypes(vararg types: KClass<out Component>): Sequence<Entity> =
      entities.filter { entity -> types.all { type -> type in entity } }

  /**
   * Checks if the given entity is contained in this manager or not.
   *
   * @param entityId the id of the given entity
   * @return `true` if the given entity is contained in the manager, `false` otherwise
   */
  operator fun contains(entityId: Int): Boolean = get(entityId) != null

  /**
   * Returns an immutable and frozen (unchanging over time) view of all entities currently present
   * in this manager.
   *
   * @return the frozen state of the entity manager
   */
  fun snapshot(): Map<Int, List<Component>> = entities.associate { it.id to it.components }
}
