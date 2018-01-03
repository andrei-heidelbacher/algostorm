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

data class File(val uri: String) {
    companion object {
        /** The schema used to identify file URIs. */
        const val SCHEMA: String = "user://"

        private val regex = Regex("$SCHEMA(/[^/]+)+")
    }

    init {
        require(uri.matches(regex)) { "Invalid file '$uri'!" }
    }

    /** The path of this file relative to the app's local directory. */
    val path: String get() = uri.removePrefix("$SCHEMA/")

    override fun toString(): String = uri
}
