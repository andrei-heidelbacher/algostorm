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

import org.junit.Test

import com.aheidelbacher.algostorm.engine.script.ScriptEngine.Companion.invokeFunction
import com.aheidelbacher.algostorm.test.engine.script.ScriptEngineTest

import java.io.File
import java.io.FileInputStream

import kotlin.concurrent.thread

class JavascriptEngineTest : ScriptEngineTest(
        scriptEngine = JavascriptDriver { FileInputStream(File(it)) },
        scriptPaths = listOf("src/test/resources/testScript.js"),
        voidFunctionName = "voidFunction",
        resultFunctionName = "getResult"
) {
    @Test
    fun testMultiThreading() {
        fun invokeScript() {
            val id = 5
            val value = "five"
            scriptEngine.invokeFunction<Any>(resultFunctionName, id, value)
        }

        invokeScript()
        kotlin.repeat(5) {
            thread { invokeScript() }.join()
        }
        invokeScript()
    }
}
