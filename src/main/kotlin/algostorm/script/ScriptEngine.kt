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

import algostorm.assets.Script

/**
 * An object that can execute scripts.
 */
interface ScriptEngine {
  /**
   * This method should run the given script and return its result, or `null` if it doesn't return
   * anything.
   *
   * @param script the script that should be executed
   * @param context the context of the script which should be available as the first parameter to
   * the script function
   * @param args the remaining script parameters
   */
  fun runScript(script: Script, context: ScriptContext, vararg args: Any?): Any?
}
