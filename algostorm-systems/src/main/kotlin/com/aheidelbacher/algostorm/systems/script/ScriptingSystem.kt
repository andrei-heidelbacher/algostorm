/*
 * Copyright (c) 2016  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
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
 * @param scriptProcedures the procedures which will be loaded in the script
 * engine at construction time
 * @param scriptFunctions the functions which will be loaded in the script
 * engine at construction time
 */
class ScriptingSystem(
        private val scriptEngine: ScriptEngine,
        scriptProcedures: List<KFunction<Unit>>,
        scriptFunctions: List<KFunction<*>>
) : Subscriber {
    /**
     * A request to execute the script procedure with the given `name` and
     * `arguments`.
     *
     * @property name the name of script procedure that should be executed
     * @property arguments the parameters of the script procedure
     */
    class InvokeProcedure private constructor(
            val name: String,
            val arguments: List<*>
    ) : Request<Unit>() {
        constructor(name: String, vararg arguments: Any?) : this(
                name = name,
                arguments = arguments.asList()
        )
    }

    /**
     * A request to execute the script function with the given `name` and
     * `arguments`, returning the result.
     *
     * @property name the name of the script function that should be executed
     * @property returnType the expected type of the result
     * @property arguments the parameters of the script function
     */
    class InvokeFunction private constructor(
            val name: String,
            val returnType: KClass<*>,
            val arguments: List<*>
    ) : Request<Any?>() {
        constructor(
                name: String,
                returnType: KClass<*>,
                vararg arguments: Any?
        ) : this(name, returnType, arguments.asList())
    }

    init {
        scriptProcedures.forEach { scriptEngine.loadProcedure(it) }
        scriptFunctions.forEach { scriptEngine.loadFunction(it) }
    }

    @Subscribe fun onInvokeProcedure(request: InvokeProcedure) {
        scriptEngine.invokeProcedure(
                request.name,
                *request.arguments.toTypedArray()
        )
        request.complete(Unit)
    }

    @Subscribe fun onInvokeFunction(request: InvokeFunction) {
        request.complete(scriptEngine.invokeFunction(
                request.name,
                request.returnType,
                *request.arguments.toTypedArray()
        ))
    }
}
