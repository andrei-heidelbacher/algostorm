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

package com.aheidelbacher.algostorm.test.engine

import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

import com.aheidelbacher.algostorm.engine.Engine
import com.aheidelbacher.algostorm.engine.Engine.Status

/**
 * An abstract test class for an [Engine].
 *
 * In order to test common functionality to all engines, you may implement this
 * class and provide a concrete engine instance to test.
 *
 * @property engine the engine instance that should be tested
 */
@Ignore
abstract class EngineTest {
    companion object {
        const val MAX_TIME_LIMIT: Long = 5000
        const val FPS_TOLERANCE: Double = 0.1
    }

    protected abstract val engine: Engine

    protected abstract fun getElapsedFrames(): Int

    @Test(timeout = MAX_TIME_LIMIT)
    fun testStartAndInstantStop() {
        assertEquals(Status.STOPPED, engine.status)
        assertEquals(false, engine.isShutdown)
        engine.start()
        assertEquals(Status.RUNNING, engine.status)
        engine.stop()
        assertEquals(Status.STOPPED, engine.status)
        assertEquals(false, engine.isShutdown)
    }

    @Test(timeout = MAX_TIME_LIMIT)
    fun testStartAndInstantShutdown() {
        assertEquals(false, engine.isShutdown)
        engine.start()
        engine.shutdown()
        assertEquals(true, engine.isShutdown)
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
    fun testRunOneSecondShouldNotThrow() {
        engine.start()
        Thread.sleep(1000)
        engine.stop()
    }

    @Test(timeout = MAX_TIME_LIMIT + 10000)
    fun testAverageFpsShouldEqualTargetFps() {
        engine.start()
        Thread.sleep(10000)
        engine.stop()
        val targetFps = 1000.0 / engine.millisPerUpdate
        val fps = 1.0 * getElapsedFrames() / 10.0
        assertEquals(1.0, fps / targetFps, FPS_TOLERANCE)
    }
}
