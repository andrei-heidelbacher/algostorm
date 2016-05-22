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
 * A mutable view of an [Entity] that allows setting or removing components.
 */
abstract class MutableEntity(id: Int) : Entity(id) {
  /**
   * Even if a component is removed, added or replaced after retrieving the components, the returned
   * list will not change.
   */
  override abstract val components: List<Component>

  /**
   * Sets the [value] of the specified component [type].
   *
   * If this entity already contains a component of the specified type, the value is overwritten.
   *
   * @param type the type of the component that is set
   * @param value the value of the component that is set
   */
  abstract operator fun <T : Component> set(type: KClass<T>, value: T): Unit

  /**
   * Sets the [value] of the specified component type [T].
   *
   * If this entity already contains a component of the specified type, the value is overwritten.
   *
   * @param T the type of the component that is set
   * @param value the value of the component that is set
   */
  inline fun <reified T : Component> set(value: T) {
    this[T::class] = value
  }

  /**
   * Removes the specified component [type].
   *
   * @param type the type of the component to be removed
   */
  abstract fun <T : Component> remove(type: KClass<T>): Unit

  /**
   * Removes the specified component type [T].
   *
   * @param T the type of the component to be removed
   */
  inline fun <reified T : Component> remove() {
    remove(T::class)
  }

  /**
   * Removes all the components from this entity.
   */
  fun clear() {
    components.forEach { remove(it.javaClass.kotlin) }
  }
}
