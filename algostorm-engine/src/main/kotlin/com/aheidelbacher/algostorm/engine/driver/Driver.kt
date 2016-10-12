package com.aheidelbacher.algostorm.engine.driver

/**
 * A core component of the engine which offers specialized, lower level
 * services.
 */
interface Driver {
    /**
     * Releases all resources acquired by this driver.
     *
     * This method should be idempotent. Calling any service after this driver
     * was released may have unexpected behavior.
     */
    fun release(): Unit
}
