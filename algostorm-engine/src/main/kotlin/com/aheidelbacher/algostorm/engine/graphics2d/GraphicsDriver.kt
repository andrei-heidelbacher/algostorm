package com.aheidelbacher.algostorm.engine.graphics2d

import com.aheidelbacher.algostorm.engine.driver.Driver

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
