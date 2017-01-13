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

package com.aheidelbacher.algostorm.engine

import org.junit.Test

import com.aheidelbacher.algostorm.engine.serialization.Deserializer.Companion.readValue
import com.aheidelbacher.algostorm.engine.serialization.JsonDriver
import com.aheidelbacher.algostorm.test.engine.EngineTest

import java.io.ByteArrayOutputStream

import kotlin.test.assertEquals

class EngineMockTest : EngineTest() {
    override val engine = EngineMock()

    @Test(timeout = MAX_TIME_LIMIT)
    fun testSerializeState() {
        engine.start()
        repeat(100) {
            val bos = ByteArrayOutputStream()
            engine.serializeState(bos)
            val state = JsonDriver().readValue<EngineMock.State>(
                    src = bos.toByteArray().inputStream()
            )
            assertEquals(engine.serializedState, state)
        }
        engine.stop()
    }

    @Test(timeout = MAX_TIME_LIMIT)
    fun testClearState() {
        engine.start()
        Thread.sleep(1000)
        engine.shutdown()
        assertEquals(0, engine.state.values.size)
    }
}
