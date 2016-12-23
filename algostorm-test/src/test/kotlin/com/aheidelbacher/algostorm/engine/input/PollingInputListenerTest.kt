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

package com.aheidelbacher.algostorm.engine.input

import org.junit.Before
import org.junit.Test

import com.aheidelbacher.algostorm.test.engine.input.InputDriverMock
import com.aheidelbacher.algostorm.test.engine.input.InputListenerMock

import kotlin.test.assertTrue

class PollingInputListenerTest {
    private val inputDriverMock = InputDriverMock()
    private val listener = PollingInputListener()
    private val listenerMock = InputListenerMock()

    @Before
    fun initializeInputDriver() {
        inputDriverMock.addListener(listener)
    }

    @Test
    fun testPollShouldNotifyAllInputsAndNothingMore() {
        val x = 1
        val y = 1
        val keyCode = 2
        inputDriverMock.touch(x, y)
        inputDriverMock.key(keyCode)
        assertTrue(listener.poll(listenerMock))
        listenerMock.assertTouch(x, y)
        assertTrue(listener.poll(listenerMock))
        listenerMock.assertKey(keyCode)
        listenerMock.assertEmptyInputQueue()
    }

    @Test
    fun testPollMostRecentShouldNotifyLastAndNothingMore() {
        val x = 1
        val y = 1
        val keyCode = 2
        inputDriverMock.touch(x, y)
        inputDriverMock.key(keyCode)
        listener.pollMostRecent(listenerMock)
        listenerMock.assertKey(keyCode)
        listenerMock.assertEmptyInputQueue()
        listener.poll(listenerMock)
        listenerMock.assertEmptyInputQueue()
    }
}
