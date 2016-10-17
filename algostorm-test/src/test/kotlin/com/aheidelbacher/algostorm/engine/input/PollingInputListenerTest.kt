package com.aheidelbacher.algostorm.engine.input

import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

import com.aheidelbacher.algostorm.test.engine.input.InputDriverMock

class PollingInputListenerTest {
    private val inputDriver = InputDriverMock()
    private val listener = PollingInputListener()

    @Before
    fun initializeInputDriver() {
        inputDriver.addListener(listener)
    }

    @Test
    fun testPollShouldNotifyAllInputsAndNothingMore() {
        inputDriver.touch(1, 1)
        inputDriver.key(2)
        var touched = false
        assertTrue(listener.poll(object : InputListener {
            override fun onTouch(x: Int, y: Int) {
                touched = true
            }

            override fun onKey(keyCode: Int) {
                fail()
            }

            override fun onScroll(dx: Int, dy: Int) {
                fail()
            }
        }))
        assertTrue(touched)
        var keyed = false
        assertTrue(listener.poll(object : InputListener {
            override fun onTouch(x: Int, y: Int) {
                fail()
            }

            override fun onKey(keyCode: Int) {
                keyed = true
            }

            override fun onScroll(dx: Int, dy: Int) {
                fail()
            }
        }))
        assertTrue(keyed)
        assertFalse(listener.poll(object : InputListener {
            override fun onTouch(x: Int, y: Int) {
                fail()
            }

            override fun onKey(keyCode: Int) {
                fail()
            }

            override fun onScroll(dx: Int, dy: Int) {
                fail()
            }
        }))
    }

    @Test
    fun testPollMostRecentShouldNotifyLastAndNothingMore() {
        inputDriver.touch(1, 1)
        inputDriver.key(2)
        var keyed = false
        listener.pollMostRecent(object : InputListener {
            override fun onTouch(x: Int, y: Int) {
                fail()
            }

            override fun onKey(keyCode: Int) {
                keyed = true
            }

            override fun onScroll(dx: Int, dy: Int) {
                fail()
            }
        })
        assertTrue(keyed)
        assertFalse(listener.poll(object : InputListener {
            override fun onTouch(x: Int, y: Int) {
                fail()
            }

            override fun onKey(keyCode: Int) {
                fail()
            }

            override fun onScroll(dx: Int, dy: Int) {
                fail()
            }
        }))
    }
}
