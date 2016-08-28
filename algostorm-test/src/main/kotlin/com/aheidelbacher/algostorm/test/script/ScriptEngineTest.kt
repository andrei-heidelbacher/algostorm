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

package com.aheidelbacher.algostorm.test.script

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

import com.aheidelbacher.algostorm.engine.script.ScriptEngine
import com.aheidelbacher.algostorm.engine.script.ScriptEngine.Companion.invokeFunction

import kotlin.reflect.KClass

/**
 * An abstract test class for a [ScriptEngine].
 *
 * In order to test common functionality to all script engines, you may
 * implement this class and provide a concrete script engine instance to test.
 *
 * @property scriptEngine the script engine instance that should be tested
 * @property scriptPaths the paths to the scripts which will be evaluated before
 * any tests are run
 * @property voidFunctionName the name of a function which shouldn't receive any
 * parameters and should return void
 * @property resultFunctionName the name of a function which should receive an
 * integer `id` and a string `value` and return a [ScriptResult] with the same
 * `id` and `value` as the received parameters
 */
@Ignore
abstract class ScriptEngineTest(
        protected val scriptEngine: ScriptEngine,
        protected val scriptPaths: List<String>,
        protected val voidFunctionName: String,
        protected val resultFunctionName: String
) {
    /**
     * Utility method that delegates the call to the [scriptEngine].
     */
    protected fun invokeFunction(
            functionName: String,
            returnType: KClass<*>,
            vararg args: Any?
    ): Any? = scriptEngine.invokeFunction(functionName, returnType, *args)

    @Before
    fun evalScripts() {
        scriptPaths.forEach { scriptEngine.eval(it) }
    }

    @Test
    fun testVoidScript() {
        scriptEngine.invokeFunction<Any>(voidFunctionName)
    }

    @Test
    fun testResultScript() {
        val id = 9382
        val value = "result"
        val result = invokeFunction(
                resultFunctionName,
                ScriptResult::class,
                id,
                value
        )
        assertEquals(ScriptResult(id, value), result)
    }
}
