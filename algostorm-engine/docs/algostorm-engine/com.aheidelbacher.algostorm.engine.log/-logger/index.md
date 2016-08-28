[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.log](../index.md) / [Logger](.)

# Logger

`interface Logger`

An object which provides logging facilities.

### Functions

| Name | Summary |
|---|---|
| [log](log.md) | `abstract fun log(event: Event): Unit`<br>Logs the given event. |

### Companion Object Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | `operator fun invoke(eventFilter: (Event) -> Boolean = null): Logger`<br>Returns a logger that writes all filtered events to [System.out](#). If
no filter is given, all events are logged. |
