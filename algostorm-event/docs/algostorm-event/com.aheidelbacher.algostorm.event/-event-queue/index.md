[algostorm-event](../../index.md) / [com.aheidelbacher.algostorm.event](../index.md) / [EventQueue](.)

# EventQueue

`class EventQueue : `[`EventBus`](../-event-bus/index.md)

An asynchronous implementation of an [EventBus](../-event-bus/index.md).

The [post](post.md) method adds the event to the event queue and it will be processed
only when [publishPosts](publish-posts.md) is called.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `EventQueue()`<br>An asynchronous implementation of an [EventBus](../-event-bus/index.md). |

### Functions

| Name | Summary |
|---|---|
| [post](post.md) | `fun <T : `[`Event`](../-event.md)`> post(event: T): Unit`<br>Posts the given [event](post.md#com.aheidelbacher.algostorm.event.EventQueue$post(com.aheidelbacher.algostorm.event.EventQueue.post.T)/event) and notifies all subscribers which subscribed at
this publisher for this `event` type. This should be an asynchronous
method and return before the event was handled by its subscribers. |
| [publish](publish.md) | `fun <T : `[`Event`](../-event.md)`> publish(event: T): Unit`<br>Immediately publishes the given event and blocks until it was handled by
all subscribers. This is a synchronous method and may not respect the
order of other posted events. |
| [publishPosts](publish-posts.md) | `fun publishPosts(): Unit`<br>Blocks until all posted events have been handled by their subscribers. |
| [subscribe](subscribe.md) | `fun subscribe(subscriber: `[`Subscriber`](../-subscriber.md)`): `[`Subscription`](../-subscription/index.md)<br>Registers the given [subscriber](subscribe.md#com.aheidelbacher.algostorm.event.EventQueue$subscribe(com.aheidelbacher.algostorm.event.Subscriber)/subscriber) and returns the associated subscription. |
