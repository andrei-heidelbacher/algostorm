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

package com.aheidelbacher.algostorm.test.engine.script

import org.junit.Before
import org.junit.Ignore
import org.junit.Test

import com.aheidelbacher.algostorm.core.drivers.script.ScriptDriver
import com.aheidelbacher.algostorm.test.engine.driver.DriverTest

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.test.assertEquals

/**
 * An abstract test class for a [ScriptDriver].
 *
 * In order to test common functionality to all script drivers, you may
 * implement this class and provide a concrete script driver instance to test.
 */
@Ignore
abstract class ScriptDriverTest : DriverTest() {
    data class Run(val name: String, val args: List<*>) {
        constructor(name: String, vararg args: Any?) : this(name, args.asList())
    }

    data class Invocation<T : Any>(
            val name: String,
            val returnType: KClass<T>,
            val args: List<*>
    ) {
        constructor(
                name: String,
                returnType: KClass<T>,
                vararg args: Any?
        ) : this(name, returnType, args.asList())
    }

    override abstract val driver: ScriptDriver

    /** The scripts which will be loaded before any tests are run. */
    protected abstract val scripts: List<KFunction<*>>

    /** The script runs which will be tested. */
    protected abstract val runs: Set<Run>

    /** The script invocations which will be tested.*/
    protected abstract val invocations: Map<Invocation<*>, *>

    @Before fun loadScripts() {
        scripts.forEach { driver.loadScript(it) }
    }

    @Test fun testRunScript() {
        runs.forEach {
            driver.runScript(it.name, *it.args.toTypedArray())
        }
    }

    @Test fun testInvokeScript() {
        invocations.forEach {
            assertEquals(
                    expected = it.value,
                    actual = driver.invokeScript(
                            it.key.name,
                            it.key.returnType,
                            *it.key.args.toTypedArray()
                    )
            )
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun testRunUnloadedScriptThrows() {
        val name = "*invalidScript"
        driver.runScript(name)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvokeUnloadedScriptThrows() {
        val name = "*invalidScript"
        driver.invokeScript(name, Any::class)
    }
}
