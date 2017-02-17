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

package com.aheidelbacher.algostorm.core.engine.script

import kotlin.reflect.KClass

/**
 * An interpreter that can load scripts and invoke named functions contained in
 * previously loaded scripts.
 */
interface ScriptEngine {
    companion object {
        inline fun <reified T : Any> ScriptEngine.invokeScript(
                name: String,
                vararg args: Any?
        ): T? = invokeScript(name, T::class, *args)
    }

    /**
     * Executes the script function with the given `name` and the specified
     * arguments.
     *
     * @param name the name of the script function which should be executed
     * @param args the script parameters
     * @throws IllegalArgumentException if the given `name` is not available to
     * this engine
     */
    fun runScript(name: String, vararg args: Any?): Unit

    /**
     * Executes the script function with the given `name` and the specified
     * arguments and returns its result.
     *
     * @param name the name of the script function which should be executed
     * @param returnType the expected type of the result
     * @param args the script parameters
     * @return the script result
     * @throws IllegalArgumentException if the given `name` is not available to
     * this engine
     * @throws ClassCastException if the result couldn't be converted to the
     * `returnType`
     */
    fun <T : Any> invokeScript(
            name: String,
            returnType: KClass<T>,
            vararg args: Any?
    ): T?
}
