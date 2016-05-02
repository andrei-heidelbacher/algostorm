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
 * A game object containing a set of [components].
 *
 * It can only contain one component of a specific type. Two entities are equal if and only if they
 * have the same [id]. Raw entities should not be serialized; instead, serialize the id and
 * components.
 *
 * This is a read-only view, but the underlying implementation might be mutable.
 *
 * @property id the unique identifier of the entity
 */
abstract class Entity(val id: EntityId) {
  /**
   * All components owned by this entity at the time of calling.
   */
  abstract val components: List<Component>

  /**
   * Returns the component of the given [type] owned by this entity.
   *
   * @param type the type of the requested component
   * @return the component of the specified type, or `null` if this entity doesn't contain such a
   * component
   */
  abstract operator fun <T : Component> get(type: KClass<T>): T?

  /**
   * Returns the component of the given type [T] owned by this entity.
   *
   * @param T the type of the requested component
   * @return the component of the specified type, or `null` if this entity doesn't contain such a
   * component
   */
  inline fun <reified T : Component> get(): T? = this[T::class]

  /**
   * Returns whether this entity contains the given component [type].
   *
   * @param type the component type
   * @return `true` if this entity contains the given component type, `false` otherwise
   */
  operator fun <T : Component> contains(type: KClass<T>): Boolean = get(type) != null

  /**
   * Returns whether this entity contains the given component type [T].
   *
   * @param T the component type
   * @return `true` if this entity contains the given component type, `false` otherwise
   */
  inline fun <reified T : Component> contains(): Boolean = T::class in this

  final override fun equals(other: Any?): Boolean = other is Entity && id == other.id

  final override fun hashCode(): Int = id.hashCode()
}
