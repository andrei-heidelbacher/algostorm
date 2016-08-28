[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.log](../index.md) / [Logger](index.md) / [invoke](.)

# invoke

`operator fun invoke(eventFilter: (Event) -> Boolean = null): `[`Logger`](index.md)

Returns a logger that writes all filtered events to [System.out](#). If
no filter is given, all events are logged.

### Parameters

`eventFilter` - the filter which should return `true` for all
logged events

**Return**
the system console logger

