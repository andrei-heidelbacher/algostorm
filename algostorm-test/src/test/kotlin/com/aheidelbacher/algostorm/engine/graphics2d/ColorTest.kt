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

import com.aheidelbacher.algostorm.engine.graphics2d.Color.alpha
import com.aheidelbacher.algostorm.engine.graphics2d.Color.blue
import com.aheidelbacher.algostorm.engine.graphics2d.Color.green
import com.aheidelbacher.algostorm.engine.graphics2d.Color.red

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

    @Test
    fun testGetAlpha() {
        val color = 0x7D09A873
        val alpha = 0x7D
        assertEquals(alpha, color.alpha)
    }

    @Test
    fun testGetRed() {
        val color = 0xFFAA048a.toInt()
        val red = 0xAA
        assertEquals(red, color.red)
    }

    @Test
    fun testGetGreen() {
        val color = 0xFF01ACb2.toInt()
        val green = 0xAC
        assertEquals(green, color.green)
    }

    @Test
    fun testGetBlue() {
        val color = 0xFF01AAb2.toInt()
        val blue = 0xB2
        assertEquals(blue, color.blue)
    }

    @Test
    fun testInvoke() {
        val alpha = 0xFF
        val red = 0x7A
        val green = 0xB3
        val blue = 0xFa
        val color = 0xFF7Ab3fa.toInt()
        assertEquals(color, Color(alpha, red, green, blue))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvokeWithInvalidAlphaShouldThrow() {
        Color(256, 93, 38, 23)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvokeWithInvalidRedShouldThrow() {
        Color(255, -1, 38, 23)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvokeWithInvalidGreenShouldThrow() {
        Color(255, 82, 824568121, 23)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvokeWithInvalidBlueShouldThrow() {
        Color(255, 82, 254, -912348)
    }
}
