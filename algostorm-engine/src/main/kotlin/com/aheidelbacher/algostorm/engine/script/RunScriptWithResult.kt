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

import com.aheidelbacher.algostorm.event.Event
import kotlin.reflect.KClass

data class RunScriptWithResult(
        val functionName: String,
        val returnType: KClass<*>,
        val args: List<*>,
        val onResult: (Any?) -> Unit
) : Event {
    constructor(
            functionName: String,
            returnType: KClass<*>,
            vararg args: Any?,
            onResult: (Any?) -> Unit
    ) : this(
            functionName = functionName,
            returnType = returnType,
            args = args.asList(),
            onResult = onResult
    )
}
