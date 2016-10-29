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

import org.junit.Before
import org.junit.Ignore
import org.junit.Test

import com.aheidelbacher.algostorm.engine.script.ScriptDriver

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.test.assertEquals

/**
 * An abstract test class for a [ScriptDriver].
 *
 * In order to test common functionality to all script engines, you may
 * implement this class and provide a concrete script driver instance to test.
 */
@Ignore
abstract class ScriptDriverTest {
    data class ProcedureInvocation(val name: String, val args: List<*>) {
        constructor(name: String, vararg args: Any?) : this(name, args.asList())
    }

    data class FunctionInvocation<T : Any>(
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

    protected abstract fun createScriptDriver(): ScriptDriver

    /**
     * The script driver which will be initialized using the
     * [createScriptDriver] method.
     */
    protected lateinit var scriptDriver: ScriptDriver
        private set

    /**
     * The script procedures which will be loaded evaluated before any tests
     * are run.
     */
    protected abstract val scriptProcedures: List<KFunction<Unit>>

    /**
     * The script functions which will be loaded evaluated before any tests are
     * run.
     */
    protected abstract val scriptFunctions: List<KFunction<*>>

    /** The procedures which will be tested. */
    protected abstract val procedureInvocations: Set<ProcedureInvocation>

    /** The functions which will be tested. */
    protected abstract val functionInvocations: Map<FunctionInvocation<*>, *>

    @Before
    fun evalScripts() {
        scriptDriver = createScriptDriver()
        scriptProcedures.forEach { scriptDriver.loadProcedure(it) }
        scriptFunctions.forEach { scriptDriver.loadFunction(it) }
    }

    @Test
    fun testProcedures() {
        procedureInvocations.forEach {
            scriptDriver.invokeProcedure(it.name, *it.args.toTypedArray())
        }
    }

    @Test
    fun testFunctions() {
        functionInvocations.forEach {
            assertEquals(
                    expected = it.value,
                    actual = scriptDriver.invokeFunction(
                            it.key.name,
                            it.key.returnType,
                            *it.key.args.toTypedArray()
                    )
            )
        }
    }

    @Test
    fun testRelease() {
        scriptDriver.release()
    }
}
