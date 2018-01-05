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

package com.andreihh.algostorm.android.test

import android.content.Context.MODE_PRIVATE
import android.support.test.InstrumentationRegistry
import com.andreihh.algostorm.android.AndroidFileSystemDriver
import com.andreihh.algostorm.core.drivers.io.File
import org.junit.After
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AndroidFileSystemDriverTest {
    private val context = InstrumentationRegistry.getTargetContext()
    private val fileSystemDriver = AndroidFileSystemDriver(context)

    private val String.local: String get() = removePrefix("user:///")

    private fun writeFile(path: String, content: String) {
        context.openFileOutput(path.local, MODE_PRIVATE)
            .bufferedWriter()
            .use { it.write(content) }
    }

    private fun readFile(path: String): String =
        context.openFileInput(path.local)
            .bufferedReader()
            .use { it.readText() }

    @Test fun testOpenInput() {
        val path = "user:///test-file-input.txt"
        val file = File(path)
        val expected = "Hello, world!\n"
        writeFile(path, expected)
        val actual = fileSystemDriver.openInput(file)
            ?.bufferedReader()
            ?.use { it.readText() }
        assertEquals(expected, actual)
    }

    @Test fun testOpenNonExistingFileInputThrows() {
        val path = "user:///non-existing.txt"
        val file = File(path)
        assertNull(fileSystemDriver.openInput(file))
    }

    @Test fun testOpenOutput() {
        val path = "user:///test-file-output.txt"
        val file = File(path)
        val expected = "Hello, world!\n"
        fileSystemDriver.openOutput(file)
            .bufferedWriter()
            .use { it.write(expected) }
        val actual = readFile(path)
        assertEquals(expected, actual)
    }

    @After fun release() {
        fileSystemDriver.release()
    }
}
