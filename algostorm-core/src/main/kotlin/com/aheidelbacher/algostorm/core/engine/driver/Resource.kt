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

package com.aheidelbacher.algostorm.core.engine.driver

import java.io.FileNotFoundException
import java.io.InputStream

/**
 * A resource used by various drivers.
 *
 * @property uri the URI of the resource
 * @throws IllegalArgumentException if `uri` doesn't begin with `res:///`
 * @throws FileNotFoundException if this resource doesn't exist
 */
data class Resource(val uri: String) {
    companion object {
        /** The schema used to identify resources. */
        const val SCHEMA: String = "res://"

        /**
         * Returns the resource at the given `path`.
         *
         * @throws IllegalArgumentException if the given `path` is not absolute
         * @throws FileNotFoundException if this resource doesn't exist
         */
        fun resourceOf(path: String): Resource = Resource("$SCHEMA$path")
    }

    init {
        require(uri.startsWith(SCHEMA)) { "$this invalid resource schema!" }
        require(path.startsWith("/")) { "$this path is not absolute!" }
        Resource::class.java.getResource(path)
                ?: throw FileNotFoundException("$this doesn't exist!")
    }

    private val path: String
        get() = uri.drop(SCHEMA.length)

    /** Returns this resource as an input stream. */
    fun inputStream(): InputStream =
            Resource::class.java.getResourceAsStream(uri.drop(SCHEMA.length))

    /** Returns the [uri] of this resource. */
    override fun toString(): String = uri
}
