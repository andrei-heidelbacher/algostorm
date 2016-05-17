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

/**
 * An object that can execute scripts.
 *
 * @property scripts the collection which contains all the scripts that can be executed
 */
abstract class ScriptEngine(private val scripts: AssetCollection<Script>) {
  /**
   * This method should run the given script and return its result.
   *
   * @param script the script that should be executed
   * @param args the script parameters
   * @return the script result, or `null` if it doesn't return anything.
   */
  protected abstract fun runScript(script: Script, vararg args: Any?): Any?

  /**
   * Executes the given script and returns its result.
   *
   * @param scriptId the id of the script that should be executed
   * @param args the script parameters
   * @return the script result, or `null` if it doesn't return anything
   */
  fun runScript(scriptId: Int, vararg args: Any?): Any? =
      runScript(scripts[scriptId] ?: error("Script id doesn't exist!!"), *args)
}
