/*
 * Copyright (c) 2016  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.aheidelbacher.algostorm.engine.driver

import java.io.FileNotFoundException
import java.io.InputStream

/**
 * A resource used by various drivers.
 *
 * @property path the absolute path of the resource
 * @throws IllegalArgumentException if [path] doesn't begin with `res:///`
 * @throws FileNotFoundException if this resource doesn't exist
 */
data class Resource(val path: String) {
    companion object {
        /** The schema used to identify resources. */
        const val SCHEMA: String = "res://"
    }

    init {
        require(path.startsWith(SCHEMA)) { "$this invalid resource schema!" }
        require(absolutePath.startsWith("/")) { "$this path is not absolute!" }
        Resource::class.java.getResource(absolutePath)
                ?: throw FileNotFoundException("$this doesn't exist!")
    }

    private val absolutePath: String
        get() = path.drop(SCHEMA.length)

    /**
     * Returns this resource as an input stream.
     *
     * @return the input stream
     */
    fun inputStream(): InputStream =
            Resource::class.java.getResourceAsStream(path.drop(SCHEMA.length))
}
