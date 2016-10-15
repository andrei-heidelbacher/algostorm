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

package com.aheidelbacher.algostorm.test.engine.script

import com.aheidelbacher.algostorm.engine.script.ScriptDriver
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

import com.aheidelbacher.algostorm.engine.script.ScriptEngine
import com.aheidelbacher.algostorm.engine.script.ScriptEngine.Companion.invokeFunction

import kotlin.reflect.KClass

/**
 * An abstract test class for a [ScriptDriver].
 *
 * In order to test common functionality to all script engines, you may
 * implement this class and provide a concrete script engine instance to test.
 */
@Ignore
abstract class ScriptDriverTest {
    protected lateinit var scriptDriver: ScriptDriver

    protected abstract fun createScriptDriver(): ScriptDriver

    /** The scripts which will be evaluated before any tests are run. */
    protected abstract val scriptPaths: List<String>

    /**
     * The name of a function which shouldn't receive any parameters and should
     * return void.
     */
    protected abstract val voidFunctionName: String

    /**
     * The name of a function which should receive an integer `id` and a string
     * `value` and return a [ScriptResult] with the same `id` and `value` as the
     * received parameters.
     */
    protected abstract val resultFunctionName: String

    /** Utility method that delegates the call to the [scriptDriver]. */
    protected fun invokeFunction(
            functionName: String,
            returnType: KClass<*>,
            vararg args: Any?
    ): Any? = scriptDriver.invokeFunction(functionName, returnType, *args)

    @Before
    fun evalScripts() {
        scriptDriver = createScriptDriver()
        scriptPaths.forEach { scriptDriver.eval(it) }
    }

    @Test
    fun testVoidScript() {
        scriptDriver.invokeFunction<Any>(voidFunctionName)
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
