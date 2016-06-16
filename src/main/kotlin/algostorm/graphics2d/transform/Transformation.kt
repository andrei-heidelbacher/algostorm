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

import algostorm.ecs.Component
import algostorm.ecs.Entity

/**
 * A transformation that is to be applied to the screen position of an entity.
 *
 * @property dx the translation amount on the x-axis in tiles
 * @property dy the translation amount on the y-axis in tiles
 */
data class Transformation(val dx: Float, val dy: Float) : Component {
    companion object {
        /**
         * The identity transformation which leaves the entity on which it is
         * applied unchanged.
         */
        val IDENTITY: Transformation = Transformation(0F, 0F)

        /**
         * The [Transformation] applied on this entity, or [IDENTITY] if no
         * transformation is applied.
         */
        val Entity.transformation: Transformation
            get() = get() ?: IDENTITY
    }

    /**
     * Returns a transformation which, added with this transformation, equals
     * [IDENTITY].
     *
     * @return the opposite transformation
     */
    operator fun unaryMinus(): Transformation = Transformation(-dx, -dy)

    /**
     * Returns a transformation which adds the translation amounts on each axis.
     *
     * @param other the transformation that should be added to this
     * transformation
     * @return the sum of the two transformations
     */
    operator fun plus(other: Transformation): Transformation =
            Transformation(dx + other.dx, dy + other.dy)

    /**
     * Returns the sum between this transformation and the opposite of the
     * [other] transformation
     *
     * @param other the transformation that should be subtracted from this
     * transformation
     * @return the difference between this transformation and the [other]
     * transformation
     */
    operator fun minus(other: Transformation): Transformation =
            Transformation(dx - other.dx, dy - other.dy)
}
