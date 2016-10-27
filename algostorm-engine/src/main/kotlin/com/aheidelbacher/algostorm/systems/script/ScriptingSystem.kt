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

package com.aheidelbacher.algostorm.systems.script

import com.aheidelbacher.algostorm.engine.script.ScriptEngine
import com.aheidelbacher.algostorm.event.Event
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber
import com.aheidelbacher.algostorm.state.File

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
     * An event which requests the execution of the script procedure with the
     * given name and arguments.
     *
     * @property name the name of script procedure that should be executed
     * @property args the arguments of the script procedure
     */
    data class InvokeProcedure private constructor(
            val name: String,
            val args: List<*>
    ) : Event {
        constructor(name: String, vararg args: Any?) : this(name, args.asList())
    }

    /**
     * An event which requests the execution of the script function with the
     * given name and arguments, attaching a callback to receive the result.
     *
     * @property name the name of script function that should be executed
     * @property returnType the expected type of the result
     * @property args the arguments of the script function
     * @property onResult the callback which will be called with the result
     */
    data class InvokeFunction private constructor(
            val name: String,
            val returnType: KClass<*>,
            val args: List<*>,
            val onResult: (Any?) -> Unit
    ) {
        constructor(
                name: String,
                returnType: KClass<*>,
                vararg args: Any?,
                onResult: (Any?) -> Unit
        ) : this(name, returnType, args.asList(), onResult)
    }

    init {
        scriptSources.forEach { scriptEngine.eval(it.path) }
    }

    @Subscribe fun onInvokeProcedure(event: InvokeProcedure) {
        scriptEngine.invokeProcedure(event.name, *event.args.toTypedArray())
    }

    @Subscribe fun onInvokeFunction(event: InvokeFunction) {
        event.onResult(scriptEngine.invokeFunction(
                event.name,
                event.returnType,
                *event.args.toTypedArray()
        ))
    }
}
