[algostorm-event](../../index.md) / [com.aheidelbacher.algostorm.event](../index.md) / [EventQueue](index.md) / [post](.)

# post

`fun <T : `[`Event`](../-event.md)`> post(event: T): Unit`

Overrides [Publisher.post](../-publisher/post.md)

Posts the given [event](post.md#com.aheidelbacher.algostorm.event.EventQueue$post(com.aheidelbacher.algostorm.event.EventQueue.post.T)/event) and notifies all subscribers which subscribed at
this publisher for this `event` type. This should be an asynchronous
method and return before the event was handled by its subscribers.

### Parameters

`T` - the type of the event

`event` - the event that should be posted