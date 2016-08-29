[algostorm-event](../index.md) / [com.aheidelbacher.algostorm.event](.)

## Package com.aheidelbacher.algostorm.event

### Types

| Name | Summary |
|---|---|
| [Event](-event.md) | `interface Event`<br>Base type of an event. |
| [EventBus](-event-bus/index.md) | `interface EventBus : `[`Publisher`](-publisher/index.md)<br>An event bus which allows a [Subscriber](-subscriber.md) to [subscribe](-event-bus/subscribe.md) and unsubscribe from
certain topics through the returned [Subscription](-subscription/index.md) and allows to [post](-publisher/post.md) an
[Event](-event.md) to the bus and notify its subscribers. |
| [EventQueue](-event-queue/index.md) | `class EventQueue : `[`EventBus`](-event-bus/index.md)<br>An asynchronous implementation of an [EventBus](-event-bus/index.md). |
| [Publisher](-publisher/index.md) | `interface Publisher`<br>Provides functionality to post events and notify subscribers. |
| [Subscriber](-subscriber.md) | `interface Subscriber`<br>Marker interface for objects that wish to register [Event](-event.md) handling methods
to an [EventBus](-event-bus/index.md). |
| [Subscription](-subscription/index.md) | `interface Subscription`<br>Allows a [Subscriber](-subscriber.md) to cancel its subscription to an [EventBus](-event-bus/index.md). |

### Annotations

| Name | Summary |
|---|---|
| [Subscribe](-subscribe/index.md) | `annotation class Subscribe`<br>Annotation to mark a method of a [Subscriber](-subscriber.md) as an event handler. |
