/*
 * Copyright (c) 2016  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.aheidelbacher.algostorm.test.engine

import org.junit.Before
import org.junit.Ignore
import org.junit.Test

import com.aheidelbacher.algostorm.engine.Engine
import com.aheidelbacher.algostorm.engine.Engine.Status

import java.io.FileNotFoundException

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * An abstract test class for an [Engine].
 *
 * In order to test common functionality to all engines, you may implement this
 * class and provide a concrete engine instance to test.
 */
@Ignore
abstract class EngineTest {
    companion object {
        const val MAX_TIME_LIMIT: Long = 5000
    }

    private lateinit var engine: Engine

    /** Factory method to create engine instances. */
    protected abstract fun createEngine(): Engine

    @Before
    fun initializeEngine() {
        engine = createEngine()
    }

    @Test(timeout = MAX_TIME_LIMIT)
    fun testStartAndInstantStop() {
        assertEquals(Status.STOPPED, engine.status)
        assertFalse(engine.isShutdown)
        engine.start()
        assertEquals(Status.RUNNING, engine.status)
        engine.stop()
        assertEquals(Status.STOPPED, engine.status)
        assertFalse(engine.isShutdown)
    }

    @Test(timeout = MAX_TIME_LIMIT)
    fun testStartAndInstantShutdown() {
        assertFalse(engine.isShutdown)
        engine.start()
        engine.shutdown()
        assertTrue(engine.isShutdown)
    }

    @Test(expected = IllegalStateException::class, timeout = MAX_TIME_LIMIT)
    fun testStartTwiceShouldThrow() {
        engine.start()
        engine.start()
    }

    @Test(expected = IllegalStateException::class, timeout = MAX_TIME_LIMIT)
    fun testShutdownTwiceShouldThrow() {
        engine.shutdown()
        engine.shutdown()
    }

    @Test(timeout = MAX_TIME_LIMIT)
    fun testStopMultipleTimesShouldNotThrow() {
        engine.start()
        repeat(10) { engine.stop() }
    }

    @Test(timeout = MAX_TIME_LIMIT)
    fun testRunOneSecondThenShutdownShouldNotThrow() {
        engine.start()
        Thread.sleep(1000)
        engine.stop()
        engine.shutdown()
    }
}
