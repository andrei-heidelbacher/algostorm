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

package com.aheidelbacher.algostorm.script

import com.aheidelbacher.algostorm.event.Event

/**
 * An event which requests the execution of a script.
 *
 * @property scriptFunctionName the name of script function that should be
 * executed
 * @property args the arguments of the script function
 */
data class RunScript(
        val scriptFunctionName: String,
        val args: List<*>
) : Event {
    constructor(scriptFunctionName: String, vararg args: Any?) : this(
            scriptFunctionName = scriptFunctionName,
            args = args.asList()
    )
}
