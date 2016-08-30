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

package com.aheidelbacher.algostorm.test.engine.graphics2d

import com.aheidelbacher.algostorm.engine.graphics2d.Matrix

object MatrixUtil {
    const val TOLERANCE: Float = 1e-7F

    @JvmStatic fun Float.eq(other: Float): Boolean =
            Math.abs(this - other) < TOLERANCE

    @JvmStatic fun FloatArray.eq(other: FloatArray): Boolean =
            size == other.size && indices.all { get(it).equals(other[it]) }

    @JvmStatic fun Matrix.eq(other: Matrix): Boolean =
            getRawValues().eq(other.getRawValues())

    @JvmStatic fun matrixOf(values: FloatArray): Matrix {
        require(values.size == 9)
        val matrix = Matrix.identity()
        val rawValues = matrix.getRawValues()
        for (i in 0..8) {
            rawValues[i] = values[i]
        }
        return matrix
    }
}
