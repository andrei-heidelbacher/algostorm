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

import algostorm.assets.AssetCollection
import algostorm.assets.Script
import algostorm.ecs.EntitySystem
import algostorm.event.Subscriber

/**
 * A system that handles script execution requests.
 *
 * @property context the context of the executed scripts
 * @property scripts the script collection
 */
abstract class ScriptingSystem(
    private val context: ScriptContext,
    private val scripts: AssetCollection<Script>
) : EntitySystem(), ScriptEngine {
  private val scriptHandler = Subscriber(RunScript::class) { event ->
    val script = scripts[event.scriptId] ?: error("Script id doesn't exist!")
    runScript(script, context, *event.args.toTypedArray())
  }

  /**
   * This system handles [RunScript] events.
   */
  final override val handlers: List<Subscriber<*>> = listOf(scriptHandler)
}
