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

package com.aheidelbacher.algostorm.android

import android.content.Context
import android.content.Context.MODE_PRIVATE

import com.aheidelbacher.algostorm.core.drivers.io.File
import com.aheidelbacher.algostorm.core.drivers.io.FileSystemDriver
import com.aheidelbacher.algostorm.core.drivers.io.InvalidResourceException
import com.aheidelbacher.algostorm.core.drivers.io.Resource
import com.aheidelbacher.algostorm.core.drivers.serialization.JsonDriver
import java.io.FileNotFoundException

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

import kotlin.reflect.KClass

class AndroidFileSystemDriver(private val context: Context) : FileSystemDriver {
    override fun <T : Any> loadResource(
            resource: Resource<T>, type: KClass<T>
    ): T = try {
        context.assets.open(resource.path).use { src ->
            JsonDriver.deserialize(src, type)
        }
    } catch (e: IOException) {
        throw InvalidResourceException(e)
    }

    override fun openFileInput(file: File): InputStream =
            context.openFileInput(file.path)

    override fun openFileOutput(file: File): OutputStream =
            context.openFileOutput(file.path, MODE_PRIVATE)
                    ?: throw FileNotFoundException("'$file' doesn't exist!")

    override fun release() {}
}
