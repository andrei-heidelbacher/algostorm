[algostorm-event](../../index.md) / [com.aheidelbacher.algostorm.event](../index.md) / [Publisher](.)

# Publisher

`interface Publisher`

Provides functionality to post events and notify subscribers.

It should preserve the order of posted events (if an event A is posted before
an event B, then subscribers will be notified for A before they are notified
for B).

### Functions

| [post](post.md) | `abstract fun <T : `[`Event`](../-event.md)`> post(event: T): Unit`<br>Posts the given [event](post.md#com.aheidelbacher.algostorm.event.Publisher$post(com.aheidelbacher.algostorm.event.Publisher.post.T)/event) and notifies all subscribers which subscribed at
this publisher for this `event` type. This should be an asynchronous
method and return before the event was handled by its subscribers.`open fun post(events: List<`[`Event`](../-event.md)`>): Unit`<br>`open fun post(vararg events: `[`Event`](../-event.md)`): Unit`<br>Calls [post](post.md) for each given event. |
| [publish](publish.md) | `abstract fun <T : `[`Event`](../-event.md)`> publish(event: T): Unit`<br>Immediately publishes the given event and blocks until it was handled by
all subscribers. This is a synchronous method and may not respect the
order of other posted events. |

### Inheritors

| [EventBus](../-event-bus/index.md) | `interface EventBus : Publisher`<br>An event bus which allows a [Subscriber](../-subscriber.md) to [subscribe](../-event-bus/subscribe.md) and unsubscribe from
certain topics through the returned [Subscription](../-subscription/index.md) and allows to [post](post.md) an
[Event](../-event.md) to the bus and notify its subscribers. |

