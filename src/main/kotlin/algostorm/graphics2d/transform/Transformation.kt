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

package algostorm.graphics2d.transform

import algostorm.ecs.Entity
import algostorm.ecs.MutableEntity
import algostorm.graphics2d.ScreenPosition.Companion.screenPosition

/**
 * A transformation that is to be applied to the screen position of an entity.
 *
 * @property dx the translation amount on the x-axis in tiles
 * @property dy the translation amount on the y-axis in tiles
 */
data class Transformation(val dx: Float, val dy: Float) {
  companion object {
    /**
     * The identity transformation which leaves the entity on which it is applied unchanged.
     */
    val identity: Transformation = Transformation(0F, 0F)

    /**
     * The [Transformation] applied on this entity, or [identity] if no transformation is applied.
     */
    val Entity.transformation: Transformation
      get() = this.get<TransformationTimer>()?.transformation ?: identity

    /**
     * Applies the given [transformation] to this entity.
     *
     * @param transformation the transformation that is to be applied
     */
    fun MutableEntity.apply(transformation: Transformation) {
      screenPosition?.let { screenPosition ->
        val newScreenPosition = screenPosition.copy (
            x = screenPosition.x + transformation.dx,
            y = screenPosition.y + transformation.dy
        )
        set(newScreenPosition)
      }
    }
  }

  operator fun unaryMinus(): Transformation = Transformation(-dx, -dy)

  operator fun plus(other: Transformation): Transformation =
      Transformation(dx + other.dx, dy + other.dy)

  operator fun minus(other: Transformation): Transformation =
      Transformation(dx - other.dx, dy - other.dy)

  operator fun times(other: Float): Transformation =
      Transformation(dx * other, dy * other)
}
