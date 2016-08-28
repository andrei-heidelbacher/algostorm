[algostorm-event](../../index.md) / [com.aheidelbacher.algostorm.event](../index.md) / [Publisher](index.md) / [publish](.)

# publish

`abstract fun <T : `[`Event`](../-event.md)`> publish(event: T): Unit`

Immediately publishes the given event and blocks until it was handled by
all subscribers. This is a synchronous method and may not respect the
order of other posted events.

### Parameters

`T` - the type of the event

`event` - the event which should be published