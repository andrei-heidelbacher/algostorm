/*
 * Copyright (c) 2017  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andreihh.algostorm.core.engine

import com.andreihh.algostorm.test.drivers.audio.AudioDriverStub
import com.andreihh.algostorm.test.drivers.graphics2d.GraphicsDriverStub
import com.andreihh.algostorm.test.drivers.input.InputDriverStub
import com.andreihh.algostorm.test.drivers.io.FileSystemDriverStub
import com.andreihh.algostorm.test.engine.EngineTest
import org.junit.Test
import kotlin.test.assertEquals

class EngineMockTest : EngineTest() {
    override val engine = EngineFactory.create(
            audioDriver = AudioDriverStub(),
            graphicsDriver = GraphicsDriverStub(),
            inputDriver = InputDriverStub(),
            fileSystemDriver = FileSystemDriverStub()
    ) as EngineMock

    @Test fun testClearState() {
        engine.start()
        Thread.sleep(1000)
        engine.stop(TIMEOUT)
        engine.release()
        assertEquals(-1, engine.state)
    }
}
