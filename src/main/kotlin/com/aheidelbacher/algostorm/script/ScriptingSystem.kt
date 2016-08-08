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

package com.aheidelbacher.algostorm.script

import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber
import com.aheidelbacher.algostorm.script.ScriptEngine.Companion.invokeFunction

import java.io.File

/**
 * A system that handles script execution requests.
 *
 * @property scriptEngine the engine used to execute scripts
 * @param scriptsDirectory the folder containing the scripts which are loaded
 * and executed at construction time using the [ScriptEngine.eval] method
 * @throws IllegalArgumentException if the given `scriptsDirectory` is not a
 * directory
 */
class ScriptingSystem(
        private val scriptEngine: ScriptEngine,
        scriptsDirectory: File
) : Subscriber {
    init {
        require(scriptsDirectory.isDirectory) {
            "Given file ${scriptsDirectory.absolutePath} is not a directory!"
        }
        scriptsDirectory.listFiles().forEach {
            scriptEngine.eval(it.inputStream())
        }
    }

    /**
     * Upon receiving a [RunScript], the [ScriptEngine.invokeFunction] method is
     * called with the supplied arguments. The returned result (if any) is
     * discarded.
     */
    @Subscribe fun handleRunScript(event: RunScript) {
        scriptEngine.invokeFunction<Any>(
                event.scriptFunctionName,
                *event.args.toTypedArray()
        )
    }
}
