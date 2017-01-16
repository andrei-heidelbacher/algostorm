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

package com.aheidelbacher.algostorm.engine.script

import org.junit.Test

import com.aheidelbacher.algostorm.engine.script.ScriptEngine.Companion.invokeScript
import com.aheidelbacher.algostorm.test.engine.script.ResultMock
import com.aheidelbacher.algostorm.test.engine.script.ScriptDriverTest

import kotlin.reflect.KFunction
import kotlin.test.assertEquals

class KotlinScriptDriverTest : ScriptDriverTest() {
    override val driver = KotlinScriptDriver()
    override val scripts: List<KFunction<*>> = listOf(
            ::procedure,
            ::intFunction,
            ::stringFunction,
            ::resultMockFunction
    )

    val value = 42
    val message = "Hello!"

    override val runs = setOf(Run(::procedure.name, message))
    override val invocations: Map<Invocation<*>, *> = mapOf(
            Invocation(
                    ::stringFunction.name,
                    String::class,
                    message
            ) to message,
            Invocation(
                    ::intFunction.name,
                    Int::class.javaObjectType.kotlin,
                    value
            ) to value,
            Invocation(
                    ::resultMockFunction.name,
                    ResultMock::class,
                    ResultMock(value, message)
            ) to ResultMock(value, message)
    )

    @Test fun testInlineInvokeStringScript() {
        val result = driver.invokeScript<String>(::stringFunction.name, message)
        assertEquals(message, result)
    }

    @Test fun testInlineInvokeIntScript() {
        val result = driver.invokeScript<Int>(::intFunction.name, value)
        assertEquals(value, result)
    }
}
