package com.aheidelbacher.algostorm.engine.graphics

import com.aheidelbacher.algostorm.engine.driver.Driver

interface GraphicsDriver : Driver, Canvas {
    /**
     * Locks this canvas and allows editing the canvas content.
     *
     * @throws IllegalStateException if the canvas is already locked
     */
    fun lock(): Unit

    /**
     * Unlocks this canvas and posts all the changes made since the canvas was
     * locked.
     *
     * @throws IllegalStateException if the canvas is not locked
     */
    fun unlockAndPost(): Unit
}
