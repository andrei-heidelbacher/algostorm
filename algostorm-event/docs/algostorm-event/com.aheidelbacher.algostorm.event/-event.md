[algostorm-event](../index.md) / [com.aheidelbacher.algostorm.event](index.md) / [Event](.)

# Event

`interface Event`

Base type of an event.

All events should be immutable data classes. The type of an event is denoted
by its kotlin class object.

Concrete events should not be generic, otherwise they may not be
serializable.

