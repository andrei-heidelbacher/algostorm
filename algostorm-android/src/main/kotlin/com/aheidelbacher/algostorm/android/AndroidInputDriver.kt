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

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.OnGestureListener
import android.view.MotionEvent
import android.view.View

import com.aheidelbacher.algostorm.engine.input.AbstractInputDriver

class AndroidInputDriver(
        context: Context,
        private val scale: Float
) : AbstractInputDriver(), View.OnTouchListener, GestureDetector.OnGestureListener {
    companion object {
        private const val TAG = "inputListener"
    }

    private val gestureDetector = GestureDetector(context, this)

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
    ): Boolean {
        val vx = velocityX.pxToDp.toInt()
        val vy = velocityY.pxToDp.toInt()
        println("Fling $vx, $vy")
        return true
    }

    override fun onLongPress(e: MotionEvent) {
        val x = e.x.pxToDp.toInt()
        val y = e.y.pxToDp.toInt()
        //onClick(e.x.pxToDp.toInt(), e.y.pxToDp.toInt())
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
        notify {
            onScroll(dx, dy)
        }
        println("Scroll $dx, $dy")
        return true
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        val x = e.x.pxToDp.toInt()
        val y = e.y.pxToDp.toInt()
        //onClick(e.x.pxToDp.toInt(), e.y.pxToDp.toInt())
        println("Single tap up")
        return true
    }

    override fun onShowPress(e: MotionEvent) {}

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        //v.performClick()
        /*val x = (event.x - v.left).pxToDp.toInt()
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
        }*/
        return true
    }
}
