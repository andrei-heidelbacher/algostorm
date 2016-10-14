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
 * @property loader the mapper used to load script paths into input streams
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

    private var scope: ScriptableObject? = executeWithContext {
        initStandardObjects()
    }

    @Throws(FileNotFoundException::class)
    override fun eval(scriptSource: String) {
        loader?.invoke(scriptSource)?.let(::InputStreamReader)?.use { reader ->
            executeWithContext {
                evaluateReader(scope, reader, scriptSource, 1, null)
            }
        } ?: error("Can't evaluate script after releasing the script driver!")
    }

    override fun <T : Any> invokeFunction(
            functionName: String,
            returnType: KClass<T>,
            vararg args: Any?
    ): T? {
        val function = scope?.get(functionName, scope) as Function? ?: error(
                "Can't invoke function after releasing the script driver!"
        )
        return returnType.java.cast(Context.jsToJava(
                executeWithContext { function.call(this, scope, scope, args) },
                returnType.java
        ))
    }

    /**
     * Releases all resources acquired by this driver.
     *
     * Invoking any method after this driver was released will throw an
     * [IllegalStateException].
     */
    override fun release() {
        loader = null
        scope = null
    }
}
