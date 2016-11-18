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
 */
class Matrix(sx: Float, kx: Float, dx: Float, ky: Float, sy: Float, dy: Float) {
    companion object {
        /**
         * Returns the identity matrix.
         *
         * @return the identity matrix
         */
        fun identity(): Matrix = Matrix(
                1F, 0F, 0F,
                0F, 1F, 0F
        )
    }

    var sx: Float = sx
        private set

    var kx: Float = kx
        private set

    var dx: Float = dx
        private set

    var ky: Float = ky
        private set

    var sy: Float = sy
        private set

    var dy: Float = dy
        private set

    fun postConcat(other: Matrix): Matrix {
        val nsx = other.sx * sx + other.kx * ky
        val nkx = other.sx * kx + other.kx * sy
        val ndx = other.sx * dx + other.kx * dy + other.dx
        val nky = other.ky * sx + other.sy * ky
        val nsy = other.ky * kx + other.sy * sy
        val ndy = other.ky * dx + other.sy * dy + other.dy
        sx = nsx
        kx = nkx
        dx = ndx
        ky = nky
        sy = nsy
        dy = ndy
        return this
    }

    //fun preConcat(other: Matrix): Matrix = other.postConcat(this)

    fun postTranslate(dx: Float, dy: Float): Matrix {
        this.dx += dx
        this.dy += dy
        return this
    }

    //fun preTranslate(dx: Float, dy: Float): Matrix =
    //        translate(dx, dy).postConcat(this)

    fun postScale(sx: Float, sy: Float): Matrix {
        this.sx *= sx
        kx *= sx
        dx *= sx
        ky *= sy
        this.sy *= sy
        dy *= sy
        return this
    }

    fun preScale(sx: Float, sy: Float): Matrix {
        this.sx *= sx
        kx *= sy
        ky *= sx
        this.sy *= sy
        return this
    }

    fun postRotate(degrees: Float): Matrix {
        val radians = degrees * Math.PI / 180.0
        val cos = Math.cos(radians).toFloat()
        val sin = Math.sin(radians).toFloat()
        val nsx = cos * sx + sin * ky
        val nkx = cos * kx + sin * sy
        val ndx = cos * dx + sin * dy
        val nky = -sin * sx + cos * ky
        val nsy = -sin * kx + cos * sy
        val ndy = -sin * dx + cos * dy
        sx = nsx
        kx = nkx
        dx = ndx
        ky = nky
        sy = nsy
        dy = ndy
        return this
    }

    fun postRotate(degrees: Float, px: Float, py: Float): Matrix =
            postTranslate(-px, -py).postRotate(degrees).postTranslate(px, py)

    //fun preRotate(degrees: Float): Matrix = rotate(degrees).postConcat(this)

    fun reset() {
        sx = 1F
        kx = 0F
        dx = 0F
        ky = 0F
        sy = 1F
        dy = 0F
    }

    fun copy(): Matrix = Matrix(sx, kx, dx, ky, sy, dy)

    operator fun get(index: Int): Float = when (index) {
        0 -> sx
        1 -> kx
        2 -> dx
        3 -> ky
        4 -> sy
        5 -> dy
        6, 7 -> 0F
        8 -> 1F
        else -> throw IndexOutOfBoundsException(
                "$index must be in the range 0..8!"
        )
    }

    operator fun get(row: Int, column: Int): Float = get(row * 3 + column)

    override fun toString(): String =
            "[[$sx, $kx, $dx], [$ky, $sy, $dy], [0, 0, 1]]"
}
