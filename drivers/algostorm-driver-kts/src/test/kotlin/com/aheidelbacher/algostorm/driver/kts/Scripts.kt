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

package com.aheidelbacher.algostorm.driver.kts

import com.aheidelbacher.algostorm.test.engine.script.ResultMock

fun procedure(message: String) {
    println(message)
}

fun intFunction(value: Int): Int = value + 1 - 1

fun stringFunction(message: String): String = message + ""

fun resultMockFunction(result: ResultMock): ResultMock =
        ResultMock(result.id, result.message)
