[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine](../index.md) / [Engine](index.md) / [stop](.)

# stop

`fun stop(): Unit`

Sets the engine [status](status.md) to [Status.STOPPING](-status/-s-t-o-p-p-i-n-g.md) and then joins the engine
thread to the current thread. If the join succeeds, the `status` will be
set to [Status.STOPPED](-status/-s-t-o-p-p-e-d.md).

If this engine attempts to stop itself, it will signal to stop processing
ticks, but will not join. As a consequence, subsequent calls to `status`
may return `Status.STOPPING`.

### Exceptions

`InterruptedException` - if the current thread is interrupted while
waiting for this engine to stop