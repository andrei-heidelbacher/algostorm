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

package com.aheidelbacher.algostorm.graphics2d

class Matrix private constructor(private val values: FloatArray) {
    companion object {
        val IDENTITY: Matrix = Matrix(floatArrayOf(
                1.0F, 0.0F, 0.0F,
                0.0F, 1.0F, 0.0F,
                0.0F, 0.0F, 1.0F
        ))

        fun translate(dx: Float, dy: Float): Matrix = Matrix(floatArrayOf(
                1.0F, 0.0F, dx,
                0.0F, 1.0F, dy,
                0.0F, 0.0F, 1.0F
        ))

        fun scale(sx: Float, sy: Float): Matrix = Matrix(floatArrayOf(
                sx, 0.0F, 0.0F,
                0.0F, sy, 0.0F,
                0.0F, 0.0F, 1.0F
        ))

        fun rotate(degrees: Float): Matrix {
            val radians = degrees * Math.PI / 180.0
            val cos = Math.cos(radians).toFloat()
            val sin = Math.sin(radians).toFloat()
            return Matrix(floatArrayOf(
                    cos, sin, 0.0F,
                    -sin, cos, 0.0F,
                    0.0F, 0.0F, 1.0F
            ))
        }

        private const val SIZE = 3

        const val SCALE_X: Int = 0

        const val SCALE_Y: Int = 4

        const val SKEW_X: Int = 1

        const val SKEW_Y: Int = 3

        const val TRANSLATE_X: Int = 2

        const val TRANSLATE_Y: Int = 5

        const val PERSPECTIVE_0: Int = 6

        const val PERSPECTIVE_1: Int = 7

        const val PERSPECTIVE_2: Int = 8
    }

    init {
        require(values.size == SIZE * SIZE) {
            "Matrix value array is of incorrect size!"
        }
    }

    fun postConcat(other: Matrix): Matrix {
        val result = floatArrayOf(
                0.0F, 0.0F, 0.0F,
                0.0F, 0.0F, 0.0F,
                0.0F, 0.0F, 0.0F
        )
        for (i in 0..SIZE - 1)
            for (j in 0..SIZE - 1)
                for (k in 0..SIZE - 1)
                    result[i * SIZE + j] +=
                            values[i * SIZE + k] * other.values[k * SIZE + j]
        return Matrix(result)
    }

    fun preConcat(other: Matrix): Matrix = other.postConcat(this)

    fun postTranslate(dx: Float, dy: Float): Matrix =
            postConcat(translate(dx, dy))

    fun preTranslate(dx: Float, dy: Float): Matrix =
            translate(dx, dy).postConcat(this)

    fun postScale(sx: Float, sy: Float): Matrix = postConcat(scale(sx, sy))

    fun preScale(sx: Float, sy: Float): Matrix = scale(sx, sy).postConcat(this)

    fun postRotate(degrees: Float): Matrix = postConcat(rotate(degrees))

    fun preRotate(degrees: Float): Matrix = rotate(degrees).postConcat(this)

    fun getValues(): FloatArray = values.copyOf()
}
