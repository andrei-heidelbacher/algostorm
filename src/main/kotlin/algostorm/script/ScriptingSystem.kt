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

import algostorm.ecs.EntityManager
import algostorm.event.Publisher
import algostorm.event.Subscribe
import algostorm.event.Subscriber

/**
 * A system that handles script execution requests.
 *
 * Upon receiving a [RunScript] event, the [ScriptingEngine.runScript] method
 * will be called. The [Context] is made available to executed scripts as a
 * variable named [CONTEXT] by calling the [ScriptingEngine.put] method before
 * the execution of the script.
 *
 * @property scriptingEngine the engine that will execute the script requests
 * @property scriptSet the collection that maps script ids to scrip URIs
 * @property entityManager the entity manager which will be provided in the
 * context of every executed script
 * @property properties the properties of the game
 * @property publisher the publisher which will be provided in the context of
 * every executed script
 */
class ScriptingSystem(
        private val scriptingEngine: ScriptingEngine,
        private val scriptSet: Map<Int, String>,
        private val entityManager: EntityManager,
        private val properties: Map<String, Any>,
        private val publisher: Publisher
) : Subscriber {
    companion object {
        /**
         * The name of the [Context] variable available to executed scripts.
         */
        const val CONTEXT: String = "context"
    }

    /**
     * The context of every script executed through a [RunScript] request.
     *
     * @property entityManager a read-only view of the entity manager which
     * handles the game entities
     * @property properties a read-only view of the game properties
     * @property publisher a publisher which provides posting functionality to
     * the game event bus
     */
    data class Context(
            val entityManager: EntityManager,
            val properties: Map<String, Any>,
            val publisher: Publisher
    )

    private val context = Context(entityManager, properties, publisher)

    @Subscribe fun handleRunScript(event: RunScript) {
        val scriptUri = scriptSet[event.scriptId] ?: error("Missing script id!")
        val args = event.args.toTypedArray()
        scriptingEngine.put(CONTEXT, context)
        scriptingEngine.runScript(scriptUri, context, *args)
    }
}
