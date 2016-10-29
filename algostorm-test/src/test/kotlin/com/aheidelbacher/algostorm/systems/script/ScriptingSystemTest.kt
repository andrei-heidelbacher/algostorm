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

import org.junit.Assert.assertEquals
import org.junit.Test

import com.aheidelbacher.algostorm.engine.script.KotlinScriptDriver
import com.aheidelbacher.algostorm.engine.script.testProcedure
import com.aheidelbacher.algostorm.engine.script.testStringFunction
import com.aheidelbacher.algostorm.systems.script.ScriptingSystem.InvokeFunction
import com.aheidelbacher.algostorm.systems.script.ScriptingSystem.InvokeProcedure

class ScriptingSystemTest {
    val system = ScriptingSystem(
            KotlinScriptDriver(),
            listOf(::testProcedure),
            listOf(::testStringFunction)
    )

    @Test
    fun testInvokeProcedure() {
        val event = InvokeProcedure("testProcedure", "Hello!")
        system.onInvokeProcedure(event)
    }

    @Test
    fun testInvokeFunction() {
        val message = "Hello!"
        var result: String? = null
        val event = InvokeFunction(
                "testStringFunction",
                String::class,
                message
        ) { result = it as String }
        system.onInvokeFunction(event)
        assertEquals(message, result)
    }
}
