[algostorm-test](../../index.md) / [com.aheidelbacher.algostorm.test.event](../index.md) / [EventBusTest](.)

# EventBusTest

`abstract class EventBusTest`

An abstract test class for an [EventBus](#).

In order to test common functionality to all event buses, you may implement
this class and provide a concrete event bus instance to test.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `EventBusTest(eventBus: EventBus)`<br>An abstract test class for an [EventBus](#). |

### Properties

| Name | Summary |
|---|---|
| [eventBus](event-bus.md) | `val eventBus: EventBus`<br>the event bus instance that should be tested |

### Functions

| Name | Summary |
|---|---|
| [privateHandlerShouldBeIgnored](private-handler-should-be-ignored.md) | `fun privateHandlerShouldBeIgnored(): Unit` |
| [protectedHandlerShouldBeIgnored](protected-handler-should-be-ignored.md) | `fun protectedHandlerShouldBeIgnored(): Unit` |
| [publishPostsShouldNotifySubscribers](publish-posts-should-notify-subscribers.md) | `fun publishPostsShouldNotifySubscribers(): Unit` |
| [publishShouldNotifySubscribers](publish-should-notify-subscribers.md) | `fun publishShouldNotifySubscribers(): Unit` |
| [subscribeMultipleParametersReceivedShouldThrow](subscribe-multiple-parameters-received-should-throw.md) | `fun subscribeMultipleParametersReceivedShouldThrow(): Unit` |
| [subscribeNonEventShouldThrow](subscribe-non-event-should-throw.md) | `fun subscribeNonEventShouldThrow(): Unit` |
| [subscribeReturningNonVoidShouldThrow](subscribe-returning-non-void-should-throw.md) | `fun subscribeReturningNonVoidShouldThrow(): Unit` |
