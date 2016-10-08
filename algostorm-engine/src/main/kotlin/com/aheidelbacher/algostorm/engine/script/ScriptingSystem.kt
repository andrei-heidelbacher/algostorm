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

package com.aheidelbacher.algostorm.engine.script

import com.aheidelbacher.algostorm.engine.script.ScriptEngine.Companion.invokeFunction
import com.aheidelbacher.algostorm.engine.state.File
import com.aheidelbacher.algostorm.event.Event
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber

import java.io.FileNotFoundException

import kotlin.reflect.KClass

/**
 * A system that handles script execution requests.
 *
 * @property scriptEngine the engine used to execute scripts
 * @param scriptSources the locations of the scripts which are loaded and
 * executed at construction time using the [ScriptEngine.eval] method
 * @throws FileNotFoundException if any of the given scripts doesn't exist
 */
class ScriptingSystem @Throws(FileNotFoundException::class) constructor(
        private val scriptEngine: ScriptEngine,
        scriptSources: List<File>
) : Subscriber {
    /**
     * An event which requests the execution of a script.
     *
     * @property functionName the name of script function that should be
     * executed
     * @property args the arguments of the script function
     */
    data class RunScript(
            val functionName: String,
            val args: List<*>
    ) : Event {
        constructor(functionName: String, vararg args: Any?) : this(
                functionName = functionName,
                args = args.asList()
        )
    }

    /**
     * An event which requests the execution of a script, attaching a callback
     * to the result.
     *
     * @property functionName the name of script function that should be
     * executed
     * @property returnType the expected type of the result
     * @property args the arguments of the script function
     * @property onResult the callback which will be called with the script
     * result
     */
    data class RunScriptWithResult(
            val functionName: String,
            val returnType: KClass<*>,
            val args: List<*>,
            val onResult: (Any?) -> Unit
    ) : Event {
        constructor(
                functionName: String,
                returnType: KClass<*>,
                vararg args: Any?,
                onResult: (Any?) -> Unit
        ) : this(
                functionName = functionName,
                returnType = returnType,
                args = args.asList(),
                onResult = onResult
        )
    }

    init {
        scriptSources.forEach { scriptEngine.eval(it.path) }
    }

    /**
     * Upon receiving a [RunScript], the [ScriptEngine.invokeFunction] method is
     * called with the supplied arguments. The returned result (if any) is
     * discarded.
     */
    @Subscribe fun onRunScript(event: RunScript) {
        scriptEngine.invokeFunction<Any>(
                event.functionName,
                *event.args.toTypedArray()
        )
    }

    @Subscribe fun onRunScriptWithResult(event: RunScriptWithResult) {
        event.onResult(scriptEngine.invokeFunction(
                event.functionName,
                event.returnType,
                *event.args.toTypedArray()
        ))
    }
}
