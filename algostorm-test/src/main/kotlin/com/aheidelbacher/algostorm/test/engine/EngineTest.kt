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

package com.aheidelbacher.algostorm.test.engine

import org.junit.Before
import org.junit.Ignore
import org.junit.Test

import com.aheidelbacher.algostorm.core.engine.Engine
import com.aheidelbacher.algostorm.core.engine.Engine.Status

import kotlin.test.assertEquals

/**
 * An abstract test class for an [Engine].
 *
 * In order to test common functionality to all engines, you may implement this
 * class and provide a concrete engine instance to test.
 */
@Ignore
abstract class EngineTest {
    companion object {
        /** The timeout used for stopping and shutting down the engine. */
        const val TIMEOUT: Int = 500
    }

    /** The engine instance that should be tested. */
    protected abstract val engine: Engine

    @Before fun init() {
        engine.init(null)
    }

    @Test fun testStartAndInstantStop() {
        assertEquals(Status.STOPPED, engine.status)
        engine.start()
        assertEquals(Status.RUNNING, engine.status)
        engine.stop(TIMEOUT)
        assertEquals(Status.STOPPED, engine.status)
    }

    @Test fun testStartAndInstantShutdown() {
        engine.start()
        engine.stop(TIMEOUT)
        engine.shutdown()
    }

    @Test(expected = IllegalStateException::class)
    fun testStartTwiceShouldThrow() {
        engine.start()
        engine.start()
    }

    @Test(expected = IllegalStateException::class)
    fun testShutdownTwiceShouldThrow() {
        engine.stop(TIMEOUT)
        engine.shutdown()
        engine.shutdown()
    }

    @Test(expected = IllegalStateException::class)
    fun testStopMultipleTimesShouldThrow() {
        engine.start()
        engine.stop(TIMEOUT)
        engine.stop(TIMEOUT)
    }

    @Test fun testRunOneSecondThenShutdownShouldNotThrow() {
        engine.start()
        Thread.sleep(1000)
        engine.stop(TIMEOUT)
        engine.shutdown()
    }
}
