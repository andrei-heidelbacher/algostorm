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

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

/** A driver that loads and executes kotlin script functions. */
class KotlinScriptDriver : ScriptDriver {
    private val scripts = hashMapOf<String, KFunction<*>>()

    override fun loadScript(script: KFunction<*>) {
        scripts[script.name] = script
    }

    override fun runScript(name: String, vararg args: Any?) {
        requireNotNull(scripts[name]).call(*args)
    }

    override fun <T : Any> invokeScript(
            name: String,
            returnType: KClass<T>,
            vararg args: Any?
    ): T? = returnType.java.cast(requireNotNull(scripts[name]).call(*args))

    override fun release() {
        scripts.clear()
    }
}
