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

import org.junit.Assert.assertEquals
import org.junit.Test

import com.aheidelbacher.algostorm.engine.script.ScriptEngine.Companion.invokeFunction
import kotlin.concurrent.thread
import kotlin.reflect.KClass

class JavascriptEngineTest {
    companion object {
        const val FUNCTION_NAME: String = "getResult"
        val SCRIPT: String = """
            function getResult(id, value) {
                var Result = Packages.com.aheidelbacher.algostorm.engine.script.Result;
                return new Result(id, value);
            };
        """.trim()
    }

    private val engine = JavascriptEngine()

    @Test
    fun testLoadScript() {
        engine.eval(SCRIPT.byteInputStream())
    }

    @Test
    fun testGetResult() {
        val id = 5
        val value = "five"
        engine.eval(SCRIPT.byteInputStream())
        val result = engine.invokeFunction<Result>(FUNCTION_NAME, id, value)
        assertEquals(Result(id, value), result)
    }

    private fun invokeFunction(
            functionName: String,
            returnType: KClass<*>,
            vararg args: Any?
    ): Any? = engine.invokeFunction(functionName, returnType, *args)

    @Test
    fun testGetUntypedResult() {
        val id = 5
        val value = "five"
        engine.eval(SCRIPT.byteInputStream())
        val result = invokeFunction(FUNCTION_NAME, Result::class, id, value)
        assertEquals(Result(id, value), result)
    }

    @Test
    fun testMultiThreading() {
        fun invokeScript() {
            val id = 5
            val value = "five"
            engine.invokeFunction<Any>(FUNCTION_NAME, id, value)
        }

        engine.eval(SCRIPT.byteInputStream())
        invokeScript()
        kotlin.repeat(5) {
            thread { invokeScript() }.join()
        }
        invokeScript()
    }
}
