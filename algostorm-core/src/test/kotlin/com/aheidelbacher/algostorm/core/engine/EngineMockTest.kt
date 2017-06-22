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

package com.aheidelbacher.algostorm.core.engine

import org.junit.Test

import com.aheidelbacher.algostorm.test.engine.EngineTest

import java.io.ByteArrayOutputStream

import kotlin.test.assertEquals

class EngineMockTest : EngineTest() {
    override val engine = EngineMock()

    @Test fun testSerializeState() {
        engine.start()
        Thread.sleep(50)
        engine.stop(TIMEOUT)
        val bos = ByteArrayOutputStream()
        engine.serializeState(bos)
        val actual = bos.toByteArray().inputStream().read()
        assertEquals(engine.state, actual)
    }

    @Test fun testClearState() {
        engine.start()
        Thread.sleep(1000)
        engine.stop(TIMEOUT)
        engine.release()
        assertEquals(-1, engine.state)
    }
}
