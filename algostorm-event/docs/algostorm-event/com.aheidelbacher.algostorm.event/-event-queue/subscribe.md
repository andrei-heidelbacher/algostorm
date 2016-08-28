[algostorm-event](../../index.md) / [com.aheidelbacher.algostorm.event](../index.md) / [EventQueue](index.md) / [subscribe](.)

# subscribe

`fun subscribe(subscriber: `[`Subscriber`](../-subscriber.md)`): `[`Subscription`](../-subscription/index.md)

Overrides [EventBus.subscribe](../-event-bus/subscribe.md)

Registers the given [subscriber](subscribe.md#com.aheidelbacher.algostorm.event.EventQueue$subscribe(com.aheidelbacher.algostorm.event.Subscriber)/subscriber) and returns the associated subscription.

### Parameters

`subscriber` - the object that subscribes for events to this event
bus

### Exceptions

`IllegalArgumentException` - if the subscriber contains an annotated
event handler that does not conform to the [Subscribe](../-subscribe/index.md) contract. However,
if any non-public or static method is annotated, it will be ignored
instead of throwing an exception.

**Return**
the subscription which allows the subscriber to unsubscribe and
stop listening for events which are posted to this event bus

