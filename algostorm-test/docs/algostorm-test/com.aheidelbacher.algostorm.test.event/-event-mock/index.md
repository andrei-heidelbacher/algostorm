[algostorm-test](../../index.md) / [com.aheidelbacher.algostorm.test.event](../index.md) / [EventMock](.)

# EventMock

`data class EventMock : Event`

An event that should be used for testing purposes.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `EventMock(eventId: Int)`<br>An event that should be used for testing purposes. |

### Properties

| Name | Summary |
|---|---|
| [eventId](event-id.md) | `val eventId: Int`<br>the unique identifier of the event. Two events are equal if
and only if they have the same id. |
