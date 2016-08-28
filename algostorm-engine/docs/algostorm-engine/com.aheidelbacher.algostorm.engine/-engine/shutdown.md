[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine](../index.md) / [Engine](index.md) / [shutdown](.)

# shutdown

`fun shutdown(): Unit`

[Stops](stop.md) and [clears](clear-state.md) this engine, unsubscribes all its
systems from the event bus and sets the [isShutdown](is-shutdown.md) flag to `true`.

### Exceptions

`IllegalStateException` - if the engine is already shutdown

`InterruptedException` - if the current thread is interrupted while
waiting for this engine to stop