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

import java.io.File
import java.io.FileInputStream

import kotlin.concurrent.thread
import kotlin.reflect.KClass

class JavascriptEngineTest {
    companion object {
        const val FUNCTION_NAME: String = "getResult"
        val SCRIPT: String = File("src/test/resources/testScript.js").toString()
        val VOID_SCRIPT: String =
                File("src/test/resources/testVoidScript.js").toString()
    }

    private val engine = JavascriptEngine { FileInputStream(File(it)) }

    private fun invokeFunction(
            functionName: String,
            returnType: KClass<*>,
            vararg args: Any?
    ): Any? = engine.invokeFunction(functionName, returnType, *args)

    @Test
    fun testLoadScript() {
        engine.eval(SCRIPT)
    }

    @Test
    fun testGetResult() {
        engine.eval(SCRIPT)
        val id = 5
        val value = "five"
        val result = engine.invokeFunction<Result>(FUNCTION_NAME, id, value)
        assertEquals(Result(id, value), result)
    }


    @Test
    fun testGetUntypedResult() {
        engine.eval(SCRIPT)
        val id = 5
        val value = "five"
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

        engine.eval(SCRIPT)
        invokeScript()
        kotlin.repeat(5) {
            thread { invokeScript() }.join()
        }
        invokeScript()
    }

    @Test
    fun testVoidFunctionReturnsUndefined() {
        engine.eval(VOID_SCRIPT)
        val id = 5
        val value = "five"
        val result = engine.invokeFunction<Any>(FUNCTION_NAME, id, value)
        assertEquals("undefined", result)
    }
}
