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

package com.aheidelbacher.algostorm.systems.state

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

/**
 * A path to a file.
 *
 * @param string the path of this file
 * @throws IllegalArgumentException if [path] is empty
 */
class File @JsonCreator constructor(string: String) {
    /** The relative or absolute path of this file. */
    val path: String = string

    init {
        require(path.isNotEmpty()) { "File path $path can't be empty!" }
    }

    /** Two files are equal if and only if they have the same file [path]. */
    override fun equals(other: Any?): Boolean =
            other is File && path == other.path

    override fun hashCode(): Int = path.hashCode()

    @JsonValue override fun toString(): String = path
}
