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

import com.aheidelbacher.algostorm.core.engine.driver.Resource
import com.aheidelbacher.algostorm.core.engine.graphics2d.Color
import com.aheidelbacher.algostorm.core.engine.graphics2d.GraphicsDriver
import com.aheidelbacher.algostorm.core.engine.graphics2d.Matrix

class GraphicsDriverStub : GraphicsDriver {
    override val height: Int = 0
    override val width: Int = 0
    override val isCanvasReady: Boolean = false
    override fun loadBitmap(resource: Resource) {}
    override fun clear() {}
    override fun drawBitmap(
            resource: Resource,
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            matrix: Matrix
    ) {}
    override fun drawColor(color: Color) {}
    override fun drawRectangle(
            color: Color,
            width: Int,
            height: Int,
            matrix: Matrix
    ) {}
    override fun lockCanvas() {}
    override fun unlockAndPostCanvas() {}
    override fun release() {}
}
