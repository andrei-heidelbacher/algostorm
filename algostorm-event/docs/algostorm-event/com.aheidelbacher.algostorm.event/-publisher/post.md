[algostorm-event](../../index.md) / [com.aheidelbacher.algostorm.event](../index.md) / [Publisher](index.md) / [post](.)

# post

`abstract fun <T : `[`Event`](../-event.md)`> post(event: T): Unit`

Posts the given [event](post.md#com.aheidelbacher.algostorm.event.Publisher$post(com.aheidelbacher.algostorm.event.Publisher.post.T)/event) and notifies all subscribers which subscribed at
this publisher for this `event` type. This should be an asynchronous
method and return before the event was handled by its subscribers.

### Parameters

`T` - the type of the event

`event` - the event that should be posted

`open fun post(events: List<`[`Event`](../-event.md)`>): Unit`
`open fun post(vararg events: `[`Event`](../-event.md)`): Unit`

Calls post for each given event.

### Parameters

`events` - the events that should be posted