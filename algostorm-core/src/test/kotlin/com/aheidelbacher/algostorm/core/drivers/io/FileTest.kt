/*
 * Copyright (c) 2017  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aheidelbacher.algostorm.core.drivers.io

import com.aheidelbacher.algostorm.core.drivers.io.File.Companion.SCHEMA
import com.aheidelbacher.algostorm.core.drivers.io.File.Companion.fileOf

import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FileTest {
    @Test fun `test invalid schema throws`() {
        assertFailsWith<IllegalArgumentException> {
            File("user:/path")
        }
    }

    @Test fun `test relative path throws`() {
        assertFailsWith<IllegalArgumentException> {
            File("${SCHEMA}path")
        }
    }

    @Test fun `test directory path throws`() {
        assertFailsWith<IllegalArgumentException> {
            File("$SCHEMA/path/")
        }
    }

    @Test fun `test multiple file separators throws`() {
        assertFailsWith<IllegalArgumentException> {
            File("$SCHEMA/path//file")
        }
    }

    @Test fun `test fileOf relative path builds correct uri`() {
        val path = "resource.txt"
        assertEquals(
                expected = File("$SCHEMA/$path"),
                actual = fileOf(path)
        )
    }
}
