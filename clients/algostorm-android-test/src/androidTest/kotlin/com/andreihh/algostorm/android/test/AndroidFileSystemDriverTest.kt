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
import com.andreihh.algostorm.core.drivers.io.File.Companion.fileOf
import com.andreihh.algostorm.core.drivers.io.FileSystem.Companion.loadResource
import com.andreihh.algostorm.core.drivers.io.InvalidResourceException
import com.andreihh.algostorm.core.drivers.io.Resource.Companion.resourceOf
import org.junit.After
import org.junit.Test
import java.io.FileNotFoundException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AndroidFileSystemDriverTest {
    data class RawResource(val content: String)

    private val context = InstrumentationRegistry.getTargetContext()
    private val fileSystemDriver = AndroidFileSystemDriver(context)

    private fun writeFile(path: String, content: String) {
        context.openFileOutput(path, MODE_PRIVATE).bufferedWriter().use {
            it.write(content)
        }
    }

    private fun readFile(path: String): String =
            context.openFileInput(path).bufferedReader().use {
                it.readText()
            }

    @Test fun testGetRawResource() {
        val path = "raw.json"
        val resource = resourceOf<RawResource>(path)
        val expected = RawResource("raw-resource")
        val actual = fileSystemDriver.loadResource(resource)
        assertEquals(expected, actual)
    }

    @Test fun testGetNonExistingResourceThrows() {
        val path = "tileset"
        val resource = resourceOf<RawResource>(path)
        assertFailsWith<InvalidResourceException> {
            fileSystemDriver.loadResource(resource)
        }
    }

    @Test fun testGetInvalidRawResourceThrows() {
        val path = "tileset.png"
        val resource = resourceOf<RawResource>(path)
        assertFailsWith<InvalidResourceException> {
            fileSystemDriver.loadResource(resource)
        }
    }

    @Test fun testOpenFileInput() {
        val path = "test-file-input.txt"
        val file = fileOf(path)
        val expected = "Hello, world!\n"
        writeFile(path, expected)
        val actual = fileSystemDriver.openFileInput(file).bufferedReader().use {
            it.readText()
        }
        assertEquals(expected, actual)
    }

    @Test fun testOpenNonExistingFileInputThrows() {
        val path = "non-existing.txt"
        val file = fileOf(path)
        assertFailsWith<FileNotFoundException> {
            fileSystemDriver.openFileInput(file)
        }
    }

    @Test fun testOpenFileOutput() {
        val path = "test-file-output.txt"
        val file = fileOf(path)
        val expected = "Hello, world!\n"
        fileSystemDriver.openFileOutput(file).bufferedWriter().use {
            it.write(expected)
        }
        val actual = readFile(path)
        assertEquals(expected, actual)
    }

    @After fun release() {
        fileSystemDriver.release()
    }
}
