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

import com.aheidelbacher.algostorm.core.engine.script.ScriptDriver

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class ScriptDriverStub : ScriptDriver {
    override fun loadScript(script: KFunction<*>) {}
    override fun runScript(name: String, vararg args: Any?) {}
    override fun <T : Any> invokeScript(
            name: String,
            returnType: KClass<T>,
            vararg args: Any?
    ): T? = null
    override fun release() {}
}
