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

import com.aheidelbacher.algostorm.core.engine.driver.Driver

import java.io.IOException

import kotlin.reflect.KFunction

/** A driver that offers scripting services. */
interface ScriptDriver : Driver, ScriptEngine {
    /**
     * Loads the given script with the [KFunction.name] name, making it
     * available to future [runScript] and [invokeScript] calls.
     *
     * If the same script is loaded multiple times, this method has no effect.
     *
     * @param script the script which should be loaded
     * @throws IOException if any error occurs when parsing and loading the
     * script
     */
    @Throws(IOException::class)
    fun loadScript(script: KFunction<*>): Unit
}
