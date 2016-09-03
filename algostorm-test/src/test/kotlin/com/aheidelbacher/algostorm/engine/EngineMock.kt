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

package com.aheidelbacher.algostorm.engine

import com.aheidelbacher.algostorm.engine.serialization.Serializer

import java.io.OutputStream

class EngineMock : Engine(25) {
    private var i = 0
    private var registeredValues = mutableListOf<Int>()
    private var state = 0

    override fun clearState() {
        i = 0
        registeredValues.clear()
    }

    override fun onHandleInput() {
        require(state == 0) { "Invalid handle input call!" }
        state = 1
    }

    override fun onRender() {
        require(state == 2) { "Invalid render call!" }
        state = 0
    }

    override fun onUpdate() {
        require(state == 1) { "Invalid update call!" }
        state = 2
        registeredValues.add(i++)
    }

    override fun writeStateToStream(outputStream: OutputStream) {
        Serializer.writeValue(outputStream, registeredValues)
    }
}
