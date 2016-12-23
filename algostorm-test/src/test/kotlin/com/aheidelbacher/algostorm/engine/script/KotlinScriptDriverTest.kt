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

package com.aheidelbacher.algostorm.engine.script

import com.aheidelbacher.algostorm.test.engine.script.ScriptDriverTest
import kotlin.reflect.KFunction

class KotlinScriptDriverTest : ScriptDriverTest() {
    data class ScriptResultMock(val id: Int, val value: String)

    override fun createScriptDriver(): KotlinScriptDriver = KotlinScriptDriver()

    override val scriptProcedures: List<KFunction<Unit>> =
            listOf(::testProcedure)

    override val scriptFunctions: List<KFunction<*>> = listOf(
            ::testIntFunction,
            ::testStringFunction,
            ::testScriptResultMockFunction
    )

    override val procedureInvocations: Set<ProcedureInvocation> =
            setOf(ProcedureInvocation("testProcedure", "Hello!"))

    override val functionInvocations: Map<FunctionInvocation<*>, *> = mapOf(
            FunctionInvocation(
                    "testStringFunction",
                    String::class,
                    "Hello!"
            ) to "Hello!",
            FunctionInvocation(
                    "testIntFunction",
                    Int::class.javaObjectType.kotlin,
                    42
            ) to 42,
            FunctionInvocation(
                    "testScriptResultMockFunction",
                    ScriptResultMock::class,
                    ScriptResultMock(42, "Hello!")
            ) to ScriptResultMock(42, "Hello!")
    )
}
