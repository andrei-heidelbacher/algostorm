package com.aheidelbacher.algostorm.engine.input

import org.junit.Before
import org.junit.Test

import com.aheidelbacher.algostorm.test.engine.input.InputDriverMock
import com.aheidelbacher.algostorm.test.engine.input.InputListenerMock

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
        listener.poll(listenerMock)
        listenerMock.assertTouch(x, y)
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
