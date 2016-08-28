[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine](../index.md) / [Engine](index.md) / [onRender](.)

# onRender

`protected abstract fun onRender(): Unit`

This method is invoked right after [onUpdate](on-update.md) returns from this engines
thread while this engine is running. The call to this method is
synchronized with the state lock.

It is the entry point into the rendering logic.

