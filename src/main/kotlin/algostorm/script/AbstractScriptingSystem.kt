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

import algostorm.event.Subscribe
import algostorm.event.Subscriber

/**
 * A system that handles script execution requests.
 *
 * @property scriptSet the collection that maps script ids to scrip URIs
 */
abstract class AbstractScriptingSystem(
        private val scriptSet: Map<Int, String>
) : Subscriber {
    /**
     * Makes the given [value] available as a variable with the name equal to
     * the given [key] to all executed scripts.
     *
     * @property key the name of the variable that will be made available
     * @property value the object that will be made available
     */
    protected abstract fun put(key: String, value: Any?): Unit

    /**
     * Executes the script at the given [scriptUri] with the specified arguments
     * and returns its result.
     *
     * @param scriptUri the location of the script that should be executed
     * @param args the script parameters
     * @return the script result, or `null` if it doesn't return anything.
     */
    protected abstract fun runScript(scriptUri: String, vararg args: Any?): Any?

    /**
     * Upon receiving a [RunScript], the [runScript] method is called.
     */
    @Subscribe fun handleRunScript(event: RunScript) {
        val scriptUri = scriptSet[event.scriptId] ?: error("Missing script id!")
        runScript(scriptUri, *event.args.toTypedArray())
    }
}
