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

package com.aheidelbacher.algostorm.core.engine.graphics2d

import com.aheidelbacher.algostorm.core.engine.driver.Driver

/**
 * A driver that offers drawing services.
 *
 * Every change to the canvas should be performed after it was locked and the
 * changes should become visible after it was unlocked.
 *
 * After the canvas was locked, it must be unlocked.
 */
interface GraphicsDriver : Driver, Canvas {
    /** Whether the canvas is ready to be drawn onto. */
    val isCanvasReady: Boolean

    /**
     * Locks this driver's canvas and allows editing the canvas content.
     *
     * @throws IllegalStateException if the canvas is already locked
     */
    fun lockCanvas(): Unit

    /**
     * Unlocks this driver's canvas and posts all the changes made since the
     * canvas was locked.
     *
     * @throws IllegalStateException if the canvas is not locked
     */
    fun unlockAndPostCanvas(): Unit
}
