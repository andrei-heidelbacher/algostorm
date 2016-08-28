[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine](../index.md) / [Engine](index.md) / [onUpdate](.)

# onUpdate

`protected abstract fun onUpdate(): Unit`

This method is invoked at most once every [millisPerUpdate](millis-per-update.md) from this
engines thread while this engine is running. The call to this method is
synchronized with the state lock.

It is the entry point into the game logic.

