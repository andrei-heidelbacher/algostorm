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

import org.junit.Assert.assertArrayEquals
import org.junit.Test

import com.aheidelbacher.algostorm.test.engine.graphics2d.MatrixUtil.TOLERANCE

class MatrixTest {
    @Test
    fun testPostScale() {
        val sx = 5938F
        val sy = -9385F
        val expected = floatArrayOf(
                sx, 0F, 0F,
                0F, sy, 0F,
                0F, 0F, 1F
        )
        val actual = Matrix.identity().postScale(sx, sy).getRawValues()
        assertArrayEquals(expected, actual, TOLERANCE)
    }

    @Test
    fun testPreScale() {
        val sx = 5938F
        val sy = -9385F
        val expected = floatArrayOf(
                sx, 0F, 0F,
                0F, sy, 0F,
                0F, 0F, 1F
        )
        val actual = Matrix.identity().preScale(sx, sy).getRawValues()
        assertArrayEquals(expected, actual, TOLERANCE)
    }

    @Test
    fun testPostTranslate() {
        val dx = 9458F
        val dy = -3948F
        val expected = floatArrayOf(
                1F, 0F, dx,
                0F, 1F, dy,
                0F, 0F, 1F
        )
        val actual = Matrix.identity().postTranslate(dx, dy).getRawValues()
        assertArrayEquals(expected, actual, TOLERANCE)
    }

}
