/*
 * Copyright 2017 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

@file:JvmName("Util")

package com.aheidelbacher.algostorm.test.engine.graphics2d

import com.aheidelbacher.algostorm.core.engine.graphics2d.Matrix

import org.junit.Assert.assertArrayEquals

const val TOLERANCE: Float = 1e-7F

fun Float.eq(other: Float): Boolean = Math.abs(this - other) < TOLERANCE

fun Matrix.eq(other: Matrix): Boolean =
        sx.eq(other.sx) && kx.eq(other.kx) && dx.eq(other.dx)
                && ky.eq(other.ky) && sy.eq(other.sy) && dy.eq(other.dy)

fun assertEquals(expected: Matrix, actual: Matrix, tolerance: Float) {
    assertArrayEquals(
            with(expected) { floatArrayOf(sx, kx, dx, ky, sy, dy) },
            with(actual) { floatArrayOf(sx, kx, dx, ky, sy, dy) },
            tolerance
    )
}

