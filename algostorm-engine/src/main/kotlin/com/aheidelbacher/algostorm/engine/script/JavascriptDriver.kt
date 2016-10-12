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

import org.mozilla.javascript.Context
import org.mozilla.javascript.ContextFactory
import org.mozilla.javascript.Function
import org.mozilla.javascript.ScriptableObject

import java.io.FileNotFoundException
import java.io.InputStream
import java.io.InputStreamReader

import kotlin.reflect.KClass

/**
 * An interpreter of Javascript files using Mozilla Rhino.
 *
 * @property loader the object used to load scripts into input streams
 */
class JavascriptDriver(
        private var loader: ((String) -> InputStream)?
) : ScriptDriver {
    private companion object {
        inline fun <T> executeWithContext(block: Context.() -> T): T = try {
            ContextFactory.getGlobal().enterContext().apply {
                optimizationLevel = -1
            }.block()
        } finally {
            Context.exit()
        }
    }

    private var isReleased = false
    private var scope: ScriptableObject? = executeWithContext {
        initStandardObjects()
    }

    @Throws(FileNotFoundException::class)
    override fun eval(scriptSource: String) {
        check(!isReleased) {
            "Can't evaluate script after scripting engine is released!"
        }
        loader?.invoke(scriptSource)?.let(::InputStreamReader)?.use { reader ->
            executeWithContext {
                evaluateReader(scope, reader, scriptSource, 1, null)
            }
        }
    }

    override fun <T : Any> invokeFunction(
            functionName: String,
            returnType: KClass<T>,
            vararg args: Any?
    ): T? {
        check(!isReleased) {
            "Can't invoke script function after scripting engine is released!"
        }
        return returnType.java.cast(Context.jsToJava(
                executeWithContext {
                    val function = scope?.get(functionName, scope) as Function?
                    function?.call(this, scope, scope, args)
                },
                returnType.java
        ))
    }

    override fun release() {
        isReleased = true
        loader = null
        scope = null
    }
}
