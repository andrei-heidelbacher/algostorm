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

import org.junit.Assert.assertEquals
import org.junit.Test

class ColorTest {
    @Test
    fun testColorDecode() {
        val blueCode = "#fF00Ff00"
        val blue = 0xFF00FF00.toInt()
        assertEquals(blue, Color.fromHtmlARGB8888(blueCode))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testMissingSharpShouldThrow() {
        Color.fromHtmlARGB8888("FF00FF01")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testReplacedSharpShouldThrow() {
        Color.fromHtmlARGB8888("%FF00FF01")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidLengthShouldThrow() {
        Color.fromHtmlARGB8888("#FF00FF000")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidCharactersShouldThrow() {
        Color.fromHtmlARGB8888("#FF00FF0G")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testMinusInvalidCharacterShouldThrow() {
        Color.fromHtmlARGB8888("#-F00FF00")
    }
}
