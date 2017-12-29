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

package com.andreihh.algostorm.core.drivers.io

/**
 * A bundled resource used by various drivers.
 *
 * @param T the type of this resource
 * @property uri the URI of this resource
 * @throws IllegalArgumentException if `uri` doesn't match `^res://(/[^/]+)+$`
 */
data class Resource<T>(val uri: String) {
    companion object {
        /** The schema used to identify resource URIs. */
        const val SCHEMA: String = "res://"

        private val regex = Regex("$SCHEMA(/[^/]+)+")

        /**
         * Returns the resource located at the given `path`, relative to the
         * resources root directory.
         *
         * @throws IllegalArgumentException if the given `path` is invalid
         */
        fun <T> of(path: String): Resource<T> =
                Resource("$SCHEMA/$path")
    }

    init {
        require(uri.matches(regex)) { "Invalid resource '$uri'!" }
    }

    /** The path of this resource relative to the resources root directory. */
    val path: String
        get() = uri.removePrefix("$SCHEMA/")

    override fun toString(): String = uri
}
