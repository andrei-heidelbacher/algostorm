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
import algostorm.ecs.EntitySystem
import algostorm.event.Publisher
import algostorm.event.Subscriber

/**
 * A system that handles script execution requests.
 *
 * Upon receiving a [RunScript] event, the [ScriptingEngine.runScript] method will be called. The
 * first script parameter is a [Context], and following are the parameters indicated in the
 * `RunScript` event.
 *
 * @property scriptingEngine the engine that will execute the script requests
 * @property context the context of the executed scripts, which should be available as the first
 * parameter to every executed script
 */
class ScriptingSystem(
    private val scriptingEngine: ScriptingEngine,
    private val entityManager: EntityManager,
    private val publisher: Publisher
) : EntitySystem {
  /**
   * The context of every script executed through a [RunScript] request. This should be passed as
   * the first argument to the executed scripts.
   *
   * @property entityManager a read-only view of the entity manager which handles the game entities
   * @property publisher a publisher which provides posting functionality to the game event bus
   */
  data class Context(val entityManager: EntityManager, val publisher: Publisher)

  private val context = Context(entityManager, publisher)
  private val scriptHandler = Subscriber(RunScript::class) { event ->
    scriptingEngine.runScript(event.scriptId, context, *event.args.toTypedArray())
  }

  /**
   * This system handles [RunScript] events.
   */
  override val handlers: List<Subscriber<*>> = listOf(scriptHandler)
}
