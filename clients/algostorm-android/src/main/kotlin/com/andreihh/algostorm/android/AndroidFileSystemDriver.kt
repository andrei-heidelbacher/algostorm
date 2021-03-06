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

package com.andreihh.algostorm.android

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.andreihh.algostorm.core.drivers.io.File
import com.andreihh.algostorm.core.drivers.io.FileSystemDriver
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.OutputStream

class AndroidFileSystemDriver(private val context: Context) : FileSystemDriver {
    override fun openInput(file: File): InputStream? = try {
        context.openFileInput(file.path)
    } catch (e: FileNotFoundException) {
        null
    }

    override fun openOutput(file: File): OutputStream =
        context.openFileOutput(file.path, MODE_PRIVATE)

    override fun delete(file: File): Boolean = context.deleteFile(file.path)

    override fun release() {}
}
