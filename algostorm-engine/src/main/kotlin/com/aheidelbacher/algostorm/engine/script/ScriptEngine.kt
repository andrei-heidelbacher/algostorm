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

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

/**
 * An interpreter that can evaluate scripts and invoke named functions contained
 * in previously evaluated scripts.
 */
interface ScriptEngine {
    companion object {
        inline fun <reified T : Any> ScriptEngine.invokeFunction(
                name: String,
                vararg arguments: Any?
        ): T? = invokeFunction(name, T::class, *arguments)
    }

    /**
     * Loads the given procedure with the [KFunction.name] name, making it
     * available to future [invokeProcedure] calls.
     *
     * @param procedure the procedure which should be loaded
     */
    fun loadProcedure(procedure: KFunction<Unit>): Unit

    /**
     * Loads the given function with the [KFunction.name] name, making it
     * available to future [invokeFunction] calls.
     *
     * @param T the return type of the function
     * @param function the function which should be loaded.
     */
    fun <T> loadFunction(function: KFunction<T>): Unit

    /**
     * Executes the script procedure with the given `name` and the specified
     * `arguments`.
     *
     * @param name the name of the script procedure that should be executed
     * @param arguments the script procedure parameters
     * @throws IllegalArgumentException if the given `name` is not available to
     * this engine
     */
    fun invokeProcedure(name: String, vararg arguments: Any?): Unit

    /**
     * Executes the script function with the given `name` and the specified
     * `arguments` and returns its result.
     *
     * @param name the name of the script function that should be executed
     * @param returnType the expected type of the result
     * @param arguments the script function parameters
     * @return the script function result
     * @throws IllegalArgumentException if the given `name` is not available to
     * this engine
     * @throws ClassCastException if the result couldn't be converted to the
     * `returnType`
     */
    fun <T : Any> invokeFunction(
            name: String,
            returnType: KClass<T>,
            vararg arguments: Any?
    ): T?
}
