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

package com.aheidelbacher.algostorm.core.ecs

import org.junit.Test

import kotlin.test.assertEquals

class IdTest {
    @Test(expected = IllegalArgumentException::class)
    fun testZeroIdShouldThrow() {
        EntityRef.Id(0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testNegativeIdShouldThrow() {
        EntityRef.Id(-138)
    }

    @Test fun testPositiveIdShouldEqualGivenValue() {
        val value = 198
        val id = EntityRef.Id(value)
        assertEquals(value, id.value)
    }

    @Test fun testToStringReturnsValueString() {
        val value = 198
        val id = EntityRef.Id(value)
        assertEquals("$value", "$id")
    }
}
