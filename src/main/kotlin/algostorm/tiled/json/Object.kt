/*
 * Copyright 2016 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package algostorm.tiled.json

class Object(
        val id: Int? = null,
        val name: String? = null,
        val type: String? = null,
        val properties: MutableMap<String, Any> = hashMapOf()
) {
    override fun equals(other: Any?): Boolean = other is Object &&
            if (id != null) id == other.id
            else super.equals(other)

    override fun hashCode(): Int = id ?: super.hashCode()
}
