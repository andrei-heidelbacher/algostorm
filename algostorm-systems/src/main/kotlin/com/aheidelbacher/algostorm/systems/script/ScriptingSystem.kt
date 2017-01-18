/*
 * Copyright 2017 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.aheidelbacher.algostorm.systems.script

import com.aheidelbacher.algostorm.engine.script.ScriptEngine
import com.aheidelbacher.algostorm.event.Request
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

/**
 * A system that handles script execution requests.
 *
 * @property scriptEngine the engine used to execute scripts
 * @param scripts the scripts which will be loaded in the script engine at
 * construction time
 */
class ScriptingSystem(
        private val scriptEngine: ScriptEngine,
        scripts: List<KFunction<*>>
) : Subscriber {
    /**
     * A request to execute the script function with the given `name` and
     * arguments.
     *
     * @property name the name of script function which should be executed
     * @property args the parameters of the script
     */
    class RunScript private constructor(
            val name: String,
            val args: List<*>
    ) : Request<Unit>() {
        constructor(name: String, vararg args: Any?) : this(
                name = name,
                args = args.asList()
        )
    }

    /**
     * A request to execute the script function with the given `name` and
     * arguments, returning the result.
     *
     * @property name the name of the script function which should be executed
     * @property returnType the expected type of the result
     * @property args the parameters of the script
     */
    class InvokeScript private constructor(
            val name: String,
            val returnType: KClass<*>,
            val args: List<*>
    ) : Request<Any?>() {
        constructor(
                name: String,
                returnType: KClass<*>,
                vararg args: Any?
        ) : this(name, returnType, args.asList())
    }

    init {
        scripts.forEach { scriptEngine.loadScript(it) }
    }

    @Subscribe fun onRunScript(request: RunScript) {
        scriptEngine.runScript(request.name, *request.args.toTypedArray())
        request.complete(Unit)
    }

    @Subscribe fun onInvokeScript(request: InvokeScript) {
        request.complete(scriptEngine.invokeScript(
                request.name,
                request.returnType,
                *request.args.toTypedArray()
        ))
    }
}
