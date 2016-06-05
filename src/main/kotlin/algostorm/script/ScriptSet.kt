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

package algostorm.script

/**
 * A container that maps script ids to script URIs. This object should be saved
 * as a property of the game.
 *
 * @property scripts the underlying map of this container
 */
data class ScriptSet(private val scripts: Map<Int, String>) {
    /**
     * Returns the URI of the given [scriptId].
     *
     * @param scriptId the id of the requested script
     * @return the URI of the requested script, or `null` if the given id
     * doesn't exist in this container
     */
    operator fun get(scriptId: Int): String? = scripts[scriptId]
}
