[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine](../index.md) / [Engine](index.md) / [start](.)

# start

`fun start(): Unit`

Sets the [status](status.md) to [Status.RUNNING](-status/-r-u-n-n-i-n-g.md) and starts the engine thread. The
engine `status` must be [Status.STOPPED](-status/-s-t-o-p-p-e-d.md) at the time of calling.

The engine thread automatically sets the `status` to `Status.STOPPED`
after terminating (either normally or exceptionally).

While this engine is running, at most once every [millisPerUpdate](millis-per-update.md)
milliseconds, it will invoke, in order, the following methods:
[onHandleInput](on-handle-input.md), [onUpdate](on-update.md) and [onRender](on-render.md). These calls are synchronized
with the state lock.

Time is measured using [measureNanoTime](#). If, at any point, the measured
time is negative, the engine thread throws an [IllegalStateException](http://docs.oracle.com/javase/6/docs/api/java/lang/IllegalStateException.html) and
terminates.

### Exceptions

`IllegalStateException` - if the `status` is not `Status.STOPPED` or
if [isShutdown](is-shutdown.md) is `true`