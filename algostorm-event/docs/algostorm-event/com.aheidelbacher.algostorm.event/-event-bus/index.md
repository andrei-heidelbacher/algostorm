[algostorm-event](../../index.md) / [com.aheidelbacher.algostorm.event](../index.md) / [EventBus](.)

# EventBus

`interface EventBus : `[`Publisher`](../-publisher/index.md)

An event bus which allows a [Subscriber](../-subscriber.md) to [subscribe](subscribe.md) and unsubscribe from
certain topics through the returned [Subscription](../-subscription/index.md) and allows to [post](../-publisher/post.md) an
[Event](../-event.md) to the bus and notify its subscribers.

### Functions

| Name | Summary |
|---|---|
| [publishPosts](publish-posts.md) | `abstract fun publishPosts(): Unit`<br>Blocks until all posted events have been handled by their subscribers. |
| [subscribe](subscribe.md) | `abstract fun subscribe(subscriber: `[`Subscriber`](../-subscriber.md)`): `[`Subscription`](../-subscription/index.md)<br>Registers the given [subscriber](subscribe.md#com.aheidelbacher.algostorm.event.EventBus$subscribe(com.aheidelbacher.algostorm.event.Subscriber)/subscriber) and returns the associated subscription. |

### Inherited Functions

| Name | Summary |
|---|---|
| [post](../-publisher/post.md) | `abstract fun <T : `[`Event`](../-event.md)`> post(event: T): Unit`<br>Posts the given [event](../-publisher/post.md#com.aheidelbacher.algostorm.event.Publisher$post(com.aheidelbacher.algostorm.event.Publisher.post.T)/event) and notifies all subscribers which subscribed at
this publisher for this `event` type. This should be an asynchronous
method and return before the event was handled by its subscribers.`open fun post(events: List<`[`Event`](../-event.md)`>): Unit`<br>`open fun post(vararg events: `[`Event`](../-event.md)`): Unit`<br>Calls [post](../-publisher/post.md) for each given event. |
| [publish](../-publisher/publish.md) | `abstract fun <T : `[`Event`](../-event.md)`> publish(event: T): Unit`<br>Immediately publishes the given event and blocks until it was handled by
all subscribers. This is a synchronous method and may not respect the
order of other posted events. |

### Inheritors

| Name | Summary |
|---|---|
| [EventQueue](../-event-queue/index.md) | `class EventQueue : EventBus`<br>An asynchronous implementation of an EventBus. |
