

### All Types

| Name | Summary |
|---|---|
| [com.aheidelbacher.algostorm.event.Event](../com.aheidelbacher.algostorm.event/-event.md) | Base type of an event. |
| [com.aheidelbacher.algostorm.event.EventBus](../com.aheidelbacher.algostorm.event/-event-bus/index.md) | An event bus which allows a [Subscriber](../com.aheidelbacher.algostorm.event/-subscriber.md) to [subscribe](../com.aheidelbacher.algostorm.event/-event-bus/subscribe.md) and unsubscribe from
certain topics through the returned [Subscription](../com.aheidelbacher.algostorm.event/-subscription/index.md) and allows to [post](../com.aheidelbacher.algostorm.event/-publisher/post.md) an
[Event](../com.aheidelbacher.algostorm.event/-event.md) to the bus and notify its subscribers. |
| [com.aheidelbacher.algostorm.event.EventQueue](../com.aheidelbacher.algostorm.event/-event-postQueue/index.md) | An asynchronous implementation of an [EventBus](../com.aheidelbacher.algostorm.event/-event-bus/index.md). |
| [com.aheidelbacher.algostorm.event.Publisher](../com.aheidelbacher.algostorm.event/-publisher/index.md) | Provides functionality to post events and notify subscribers. |
| [com.aheidelbacher.algostorm.event.Subscribe](../com.aheidelbacher.algostorm.event/-subscribe/index.md) | Annotation to mark a method of a [Subscriber](../com.aheidelbacher.algostorm.event/-subscriber.md) as an event handler. |
| [com.aheidelbacher.algostorm.event.Subscriber](../com.aheidelbacher.algostorm.event/-subscriber.md) | Marker interface for objects that wish to register [Event](../com.aheidelbacher.algostorm.event/-event.md) handling methods
to an [EventBus](../com.aheidelbacher.algostorm.event/-event-bus/index.md). |
| [com.aheidelbacher.algostorm.event.Subscription](../com.aheidelbacher.algostorm.event/-subscription/index.md) | Allows a [Subscriber](../com.aheidelbacher.algostorm.event/-subscriber.md) to cancel its subscription to an [EventBus](../com.aheidelbacher.algostorm.event/-event-bus/index.md). |
