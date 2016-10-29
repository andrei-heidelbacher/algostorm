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

import com.aheidelbacher.algostorm.engine.script.KotlinScriptDriverTest.ScriptResultMock

fun testProcedure(message: String) {
    println(message)
}

fun testIntFunction(intValue: Int): Int {
    var value = intValue
    value += 1
    value -= 1
    return value
}

fun testStringFunction(stringValue: String): String {
    var value = stringValue
    value += ""
    return value
}

fun testScriptResultMockFunction(
        scriptResultMockValue: ScriptResultMock
): ScriptResultMock {
    val id = scriptResultMockValue.id
    val value = scriptResultMockValue.value
    return ScriptResultMock(id, value)
}
