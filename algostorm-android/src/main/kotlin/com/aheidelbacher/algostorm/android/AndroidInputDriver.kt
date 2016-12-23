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

package com.aheidelbacher.algostorm.android

import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.OnGestureListener
import android.view.MotionEvent
import android.view.View

import com.aheidelbacher.algostorm.engine.input.AbstractInputDriver

class AndroidInputDriver(
        private val scale: Float
) : AbstractInputDriver(), View.OnTouchListener, GestureDetector.OnGestureListener {
    companion object {
        private const val TAG = "inputListener"
    }

    //private val gestureDetector = GestureDetector(context, this)

    private val Float.pxToDp: Float
        get() = this / scale

    private val Int.pxToDp: Int
        get() = (this.toFloat() / scale).toInt()


    override fun onDown(e: MotionEvent): Boolean = true

    override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
    ): Boolean = false

    override fun onLongPress(e: MotionEvent) {
        //onClick(e.x.pxToDp.toInt(), e.y.pxToDp.toInt())
    }

    override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
    ): Boolean {
        notify {
            onScroll(distanceX.pxToDp.toInt(), distanceY.pxToDp.toInt())
        }
        println("Hello")
        return true
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        //onClick(e.x.pxToDp.toInt(), e.y.pxToDp.toInt())
        return true
    }

    override fun onShowPress(e: MotionEvent) {}

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        //v.performClick()
        val x = (event.x - v.left).pxToDp.toInt()
        val y = (event.y - v.top).pxToDp.toInt()
        println("Touched at $x, $y!")
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                notify {
                    val dx = x - v.width.pxToDp / 2
                    val dy = y - v.height.pxToDp / 2
                    println("Touched at $dx, $dy!")
                    onTouch(x - v.width.pxToDp / 2, y - v.height.pxToDp / 2)
                }
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
            }
            MotionEvent.ACTION_CANCEL -> {
            }
        }
        return false
    }
}
