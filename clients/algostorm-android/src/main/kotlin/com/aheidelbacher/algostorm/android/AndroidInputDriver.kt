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

package com.aheidelbacher.algostorm.android

import android.content.Context
import android.os.Handler
import android.view.GestureDetector
import android.view.GestureDetector.OnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.aheidelbacher.algostorm.core.drivers.client.input.Input
import com.aheidelbacher.algostorm.core.drivers.client.input.InputDriver
import com.aheidelbacher.algostorm.core.drivers.client.input.InputDriver.GestureInterpreter
import com.aheidelbacher.algostorm.core.drivers.client.input.InputSocket

class AndroidInputDriver(
        private val context: Context
) : InputDriver, OnTouchListener, OnGestureListener {
    private val inputSocket = InputSocket()
    private val scale = context.resources.displayMetrics.density
    @Volatile private var gestureDetector: GestureDetector? = null
    @Volatile private var gestureInterpreter: GestureInterpreter? = null

    private val Float.pxToDp: Float
        get() = this / scale

    override fun onDown(e: MotionEvent): Boolean = true

    override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
    ): Boolean {
        val vx = velocityX.pxToDp.toInt()
        val vy = velocityY.pxToDp.toInt()
        println("Fling $vx, $vy")
        return true
    }

    override fun onLongPress(e: MotionEvent) {
        val x = e.x.pxToDp.toInt()
        val y = e.y.pxToDp.toInt()
        println("Long press $x, $y")
    }

    override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
    ): Boolean {
        val dx = distanceX.pxToDp.toInt()
        val dy = distanceY.pxToDp.toInt()
        gestureInterpreter?.onScroll(dx, dy)?.let(this::write)
        return true
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        val x = e.x.pxToDp.toInt()
        val y = e.y.pxToDp.toInt()
        gestureInterpreter?.onTouch(x, y)?.let(this::write)
        return true
    }

    override fun onShowPress(e: MotionEvent) {}

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        gestureDetector?.onTouchEvent(event)
        return true
    }

    override fun read(): Input? = inputSocket.read()

    override fun write(input: Input) {
        inputSocket.write(input)
    }

    override fun setGestureInterpreter(interpreter: GestureInterpreter?) {
        if (interpreter != null) {
            val handler = Handler(context.mainLooper)
            gestureDetector = GestureDetector(context, this, handler)
            gestureInterpreter = interpreter
        } else {
            gestureDetector = null
            gestureInterpreter = null
        }
    }

    override fun release() {
        inputSocket.clear()
        gestureDetector = null
        gestureInterpreter = null
    }
}
