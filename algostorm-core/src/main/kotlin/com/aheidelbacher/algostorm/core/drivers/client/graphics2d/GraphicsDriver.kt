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

package com.aheidelbacher.algostorm.core.drivers.client.graphics2d

import com.aheidelbacher.algostorm.core.drivers.Driver
import com.aheidelbacher.algostorm.core.drivers.io.Resource
import com.aheidelbacher.algostorm.core.drivers.io.InvalidResourceException

/**
 * A driver that offers drawing services.
 *
 * Every change to the canvas should be performed after it was locked and the
 * changes should become visible after it was unlocked.
 *
 * After the canvas was locked, it must be unlocked.
 */
interface GraphicsDriver : Driver, Canvas {
    /**
     * Synchronously loads the given `bitmap`, making it available to future
     * calls of [drawBitmap].
     *
     * If the same bitmap is loaded multiple times, this method has no effect.
     *
     * @param bitmap the bitmap resource which should be loaded
     * @throws InvalidResourceException if any error occurs when parsing and
     * loading the `bitmap`
     */
    fun loadBitmap(bitmap: Resource<Bitmap>)

    /** Whether the canvas is ready to be drawn onto. */
    val isCanvasReady: Boolean

    /**
     * Locks this driver's canvas and allows editing the canvas content.
     *
     * @throws IllegalStateException if the canvas is already locked
     */
    fun lockCanvas()

    /**
     * Unlocks this driver's canvas and posts all the changes made since the
     * canvas was locked.
     *
     * @throws IllegalStateException if the canvas is not locked
     */
    fun unlockAndPostCanvas()
}
