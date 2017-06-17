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

package com.aheidelbacher.algostorm.systems.script

import org.junit.Before
import org.junit.Test

import com.aheidelbacher.algostorm.drivers.kts.KotlinScriptDriver
import com.aheidelbacher.algostorm.systems.script.ScriptingService.InvokeScript
import com.aheidelbacher.algostorm.systems.script.ScriptingService.RunScript

import kotlin.test.assertEquals

class ScriptingServiceTest {
    val driver = KotlinScriptDriver()
    val service = ScriptingService(driver)

    @Before fun loadScripts() {
        driver.loadScript(::testProcedure)
        driver.loadScript(::testFunction)
    }

    @Test fun testInvokeProcedure() {
        val event = RunScript(::testProcedure.name, "Hello!")
        service.onRunScript(event)
        assertEquals(Unit, event.get())
    }

    @Test fun testInvokeFunction() {
        val message = "Hello!"
        val event = InvokeScript(
                ::testFunction.name,
                String::class,
                message
        )
        service.onInvokeScript(event)
        assertEquals(message, event.get() as String)
    }
}