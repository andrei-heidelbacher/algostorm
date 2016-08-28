[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine](../index.md) / [Engine](index.md) / [onHandleInput](.)

# onHandleInput

`protected abstract fun onHandleInput(): Unit`

This method is invoked right before [onUpdate](on-update.md) is called from this
engines thread while this engine is running. The call to this method is
synchronized with the state lock.

It is the entry point into the input-handling logic.

