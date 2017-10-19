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

package com.aheidelbacher.algostorm.test.engine.graphics2d

import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.Bitmap
import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.Color
import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.GraphicsDriver
import com.aheidelbacher.algostorm.core.drivers.io.Resource

class GraphicsDriverStub : GraphicsDriver {
    override val height: Int = 0
    override val width: Int = 0
    override val isCanvasReady: Boolean = false
    override fun loadBitmap(bitmap: Resource<Bitmap>) {}
    override fun lockCanvas() {}
    override fun save() {}
    override fun translate(dx: Float, dy: Float) {}
    override fun scale(sx: Float, sy: Float) {}
    override fun rotate(degrees: Float) {}
    override fun restore() {}
    override fun drawBitmap(
            bitmap: Resource<Bitmap>,
            sx: Int,
            sy: Int,
            sw: Int,
            sh: Int,
            dx: Int,
            dy: Int,
            dw: Int,
            dh: Int
    ) {}
    override fun drawColor(color: Color) {}
    override fun drawRectangle(color: Color, x: Int, y: Int, w: Int, h: Int) {}
    override fun unlockAndPostCanvas() {}
    override fun release() {}
}
