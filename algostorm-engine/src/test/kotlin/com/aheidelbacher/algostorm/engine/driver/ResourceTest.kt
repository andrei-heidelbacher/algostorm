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

package com.aheidelbacher.algostorm.engine.driver

import org.junit.Test

import com.aheidelbacher.algostorm.engine.driver.Resource.Companion.SCHEMA

import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

import kotlin.test.assertEquals

class ResourceTest {
    @Test(expected = IllegalArgumentException::class)
    fun testInvalidSchemaShouldThrow() {
        Resource("res:/path")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testRelativePathShouldThrow() {
        Resource("${SCHEMA}path")
    }

    @Test(expected = FileNotFoundException::class)
    fun testInvalidPathShouldThrow() {
        Resource("$SCHEMA/path/")
    }

    @Test(expected = FileNotFoundException::class)
    fun testNonExistentInputStreamShouldThrow() {
        Resource("$SCHEMA/nonExistent.txt")
    }

    @Test fun testInputStreamShouldParseResource() {
        Resource("$SCHEMA/resource.txt").inputStream().use { src ->
            ByteArrayOutputStream().use { dst ->
                src.copyTo(dst)
                assertEquals("resource.txt\n", dst.toString())
            }
        }
    }
}
