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

package com.aheidelbacher.algostorm.core.drivers.client.graphics2d

import org.junit.Test

import kotlin.test.assertEquals

class ColorTest {
    @Test fun testColorDecode() {
        val blueCode = "#fF00Ff00"
        val blue = 0xFF00FF00.toInt()
        assertEquals(blue, Color(blueCode).color)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testMissingSharpShouldThrow() {
        Color("FF00FF01")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testReplacedSharpShouldThrow() {
        Color("%FF00FF01")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidLengthShouldThrow() {
        Color("#FF00FF000")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidCharactersShouldThrow() {
        Color("#FF00FF0G")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testMinusInvalidCharacterShouldThrow() {
        Color("#-F00FF00")
    }

    @Test fun testGetAlpha() {
        val color = Color("#7D09A873")
        val alpha = 0x7D
        assertEquals(alpha, color.a)
    }

    @Test fun testGetRed() {
        val color = Color("#FFAA048a")
        val red = 0xAA
        assertEquals(red, color.r)
    }

    @Test fun testGetGreen() {
        val color = Color("#FF01ACb2")
        val green = 0xAC
        assertEquals(green, color.g)
    }

    @Test fun testGetBlue() {
        val color = Color("#FF01AAb2")
        val blue = 0xB2
        assertEquals(blue, color.b)
    }

    @Test fun testEquals() {
        val argbRed = Color("#FFFF0000")
        val rgbRed = Color("#FF0000")
        assertEquals(argbRed, rgbRed)
    }

    @Test fun testToString() {
        val color = Color("#FF00FF")
        assertEquals("#ffff00ff", color.toString())
    }
}
