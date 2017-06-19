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

package com.aheidelbacher.algostorm.android.test

import android.content.Context.MODE_PRIVATE
import android.support.test.InstrumentationRegistry

import com.aheidelbacher.algostorm.android.AndroidFileSystemDriver
import com.aheidelbacher.algostorm.core.drivers.io.File.Companion.fileOf
import com.aheidelbacher.algostorm.core.drivers.io.InvalidResourceException
import com.aheidelbacher.algostorm.core.drivers.io.Resource.Companion.resourceOf

import org.junit.After
import org.junit.Test

import java.io.FileNotFoundException

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AndroidFileSystemDriverTest {
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
        val path = "tileset.png"
        val resource = resourceOf(path)
        val expected = context.assets.open(path).use { it.readBytes() }
        val actual = fileSystemDriver.getRawResource(resource)
        assertEquals(expected.size, actual.size)
        for (i in expected.indices) {
            assertEquals(expected[i], actual[i])
        }
    }

    @Test fun testGetInvalidResourceThrows() {
        val path = "tileset"
        val resource = resourceOf(path)
        assertFailsWith<InvalidResourceException> {
            fileSystemDriver.getRawResource(resource)
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

    @Test fun testGetRawResourceAfterReleaseThrows() {
        val path = "resource.png"
        val resource = resourceOf(path)
        fileSystemDriver.release()
        assertFailsWith<IllegalStateException> {
            fileSystemDriver.getRawResource(resource)
        }
    }

    @Test fun testOpenFileInputAfterReleaseThrows() {
        val path = "file.txt"
        val file = fileOf(path)
        fileSystemDriver.release()
        assertFailsWith<IllegalStateException> {
            fileSystemDriver.openFileInput(file)
        }
    }

    @Test fun testOpenFileOutputAfterReleaseThrows() {
        val path = "file.txt"
        val file = fileOf(path)
        fileSystemDriver.release()
        assertFailsWith<IllegalStateException> {
            fileSystemDriver.openFileOutput(file)
        }
    }

    @After fun release() {
        fileSystemDriver.release()
    }
}
