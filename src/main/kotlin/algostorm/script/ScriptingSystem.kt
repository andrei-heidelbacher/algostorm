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
import algostorm.ecs.EntityManager
import algostorm.ecs.EntitySystem
import algostorm.event.Publisher
import algostorm.event.Subscriber

/**
 * A system that handles script execution requests.
 *
 * @property scriptContext the context of the executed scripts
 * @property scripts the script collection
 */
abstract class ScriptingSystem(
    private val scriptContext: Context,
    protected val scripts: AssetCollection<Script>
) : EntitySystem() {
  /**
   * The context of every executed script. This should be passed as the first argument to every
   * executed script.
   *
   * @property entityManager a read-only view of the entity manager which handles the game entities
   * @property publisher a publisher which provides posting functionality to the game event bus
   */
  data class Context(val entityManager: EntityManager, val publisher: Publisher)

  /**
   * This method should run the given script and return its result, or `null` if it doesn't return
   * anything.
   *
   * @param script the script that should be executed
   * @param context the context of the script which should be available as the first parameter to
   * the script function
   * @param args the remaining script parameters
   */
  abstract fun runScript(script: Script, context: Context, vararg args: Any?): Any?

  private val scriptHandler = Subscriber(RunScript::class) { event ->
    val script = scripts[event.scriptId] ?: error("Script id doesn't exist!")
    runScript(script, scriptContext, *event.args.toTypedArray())
  }

  /**
   * This system handles [RunScript] events.
   */
  override val handlers: List<Subscriber<*>> = listOf(scriptHandler)
}
