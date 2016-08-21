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

package com.aheidelbacher.algostorm.engine.graphics2d

/**
 * Used to apply a sequence of transformations to a bitmap before drawing to the
 * canvas.
 *
 * @property values the underlying values of this matrix
 */
class Matrix private constructor(private val values: FloatArray) {
    companion object {
        fun identity(): Matrix = Matrix(floatArrayOf(
                1F, 0F, 0F,
                0F, 1F, 0F,
                0F, 0F, 1F
        ))

        fun translate(dx: Float, dy: Float): Matrix = Matrix(floatArrayOf(
                1F, 0F, dx,
                0F, 1F, dy,
                0F, 0F, 1F
        ))

        fun scale(sx: Float, sy: Float): Matrix = Matrix(floatArrayOf(
                sx, 0F, 0F,
                0F, sy, 0F,
                0F, 0F, 1F
        ))

        fun rotate(degrees: Float): Matrix {
            val radians = degrees * Math.PI / 180.0
            val cos = Math.cos(radians).toFloat()
            val sin = Math.sin(radians).toFloat()
            return Matrix(floatArrayOf(
                    cos, sin, 0F,
                    -sin, cos, 0F,
                    0F, 0F, 1F
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
        for (j in 0..SIZE - 1) {
            val a = values[j]
            val b = values[SIZE + j]
            val c = values[2 * SIZE + j]
            for (i in 0..SIZE - 1) {
                values[i * SIZE + j] = a * other.values[i * SIZE] +
                        b * other.values[i * SIZE + 1] +
                        c * other.values[i * SIZE + 2]
            }
        }
        return this
    }

    //fun preConcat(other: Matrix): Matrix = other.postConcat(this)

    fun postTranslate(dx: Float, dy: Float): Matrix {
        for (i in 0..SIZE - 1) {
            values[i] += values[2 * SIZE + i] * dx
            values[SIZE + i] += values[2 * SIZE + i] * dy
        }
        return this
    }

    //fun preTranslate(dx: Float, dy: Float): Matrix =
    //        translate(dx, dy).postConcat(this)

    fun postScale(sx: Float, sy: Float): Matrix {
        for (i in 0..SIZE - 1) {
            values[i] *= sx
            values[SIZE + i] *= sy
        }
        return this
    }

    //fun preScale(sx: Float, sy: Float): Matrix = scale(sx, sy).postConcat(this)

    fun postRotate(degrees: Float): Matrix {
        val radians = degrees * Math.PI / 180.0
        val cos = Math.cos(radians).toFloat()
        val sin = Math.sin(radians).toFloat()
        for (i in 0..SIZE - 1) {
            val a = values[i]
            val b = values[SIZE + i]
            values[i] = cos * a + sin * b
            values[SIZE + i] = -sin * a + cos * b
        }
        return this
    }

    fun postRotate(degrees: Float, px: Float, py: Float): Matrix =
            postTranslate(-px, -py).postRotate(degrees).postTranslate(px, py)

    //fun preRotate(degrees: Float): Matrix = rotate(degrees).postConcat(this)

    fun reset() {
        for (i in 0..SIZE * SIZE - 1) {
            values[i] = 0F
        }
        for (i in 0..SIZE - 1) {
            values[i * SIZE + i] = 1F
        }
    }

    operator fun get(index: Int): Float = values[index]

    operator fun get(row: Int, column: Int): Float = values[row * SIZE + column]

    fun getValues(): FloatArray = values.copyOf()

    fun getRawValues(): FloatArray = values
}
