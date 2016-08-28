[algostorm-event](../../index.md) / [com.aheidelbacher.algostorm.event](../index.md) / [EventQueue](index.md) / [publish](.)

# publish

`fun <T : `[`Event`](../-event.md)`> publish(event: T): Unit`

Overrides [Publisher.publish](../-publisher/publish.md)

Immediately publishes the given event and blocks until it was handled by
all subscribers. This is a synchronous method and may not respect the
order of other posted events.

### Parameters

`T` - the type of the event

`event` - the event which should be published