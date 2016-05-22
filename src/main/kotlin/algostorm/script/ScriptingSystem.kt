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

import algostorm.ecs.EntitySystem
import algostorm.event.Subscriber

/**
 * A system that handles script execution requests.
 *
 * @property scriptEngine the engine that will execute the script requests
 * @property context the context of the executed scripts, which should be available as the first
 * parameter to every executed script
 */
class ScriptingSystem(
    private val scriptEngine: ScriptEngine,
    private val context: ScriptContext
) : EntitySystem {
  private val scriptHandler = Subscriber(RunScript::class) { event ->
    scriptEngine.runScript(event.scriptId, context, *event.args.toTypedArray())
  }

  /**
   * This system handles [RunScript] events.
   */
  final override val handlers: List<Subscriber<*>> = listOf(scriptHandler)
}
