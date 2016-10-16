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

import com.aheidelbacher.algostorm.engine.script.JavascriptDriver
import com.aheidelbacher.algostorm.engine.serialization.JsonDriver
import com.aheidelbacher.algostorm.test.engine.audio.AudioDriverMock
import com.aheidelbacher.algostorm.test.engine.graphics2d.GraphicsDriverMock
import com.aheidelbacher.algostorm.test.engine.input.InputDriverMock

import java.io.OutputStream

class EngineMock : Engine(
        audioDriver = AudioDriverMock(),
        graphicsDriver = GraphicsDriverMock(320, 230),
        inputDriver = InputDriverMock(),
        scriptDriver = JavascriptDriver { getResourceStream(it) },
        serializationDriver = JsonDriver()
) {
    data class State(val values: List<Int>)

    private var i = 0
    private var registeredValues = mutableListOf<Int>()
    private var stage = 0
    val state: State = State(registeredValues)

    override var millisPerUpdate: Int = 25

    override fun onStart() {}

    override fun onStop() {}

    override fun onShutdown() {
        i = 0
        registeredValues.clear()
    }

    override fun onRender() {
        require(stage == 0) { "Invalid render call!" }
        stage = 1
    }

    override fun onHandleInput() {
        require(stage == 1) { "Invalid handle input call!" }
        stage = 2
    }

    override fun onUpdate() {
        require(stage == 2) { "Invalid update call!" }
        stage = 0
        registeredValues.add(i++)
    }

    @Volatile lateinit var serializedState: State
        private set

    override fun onSerializeState(outputStream: OutputStream) {
        serializedState = State(registeredValues.toList())
        serializationDriver.writeValue(outputStream, serializedState)
    }
}
