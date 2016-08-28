[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.time](../index.md) / [Timer](.)

# Timer

`data class Timer`

A timer which will post the associated [events](events.md) when [remainingTicks](remaining-ticks.md) ticks
have elapsed.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Timer(remainingTicks: Int, event: Event)`<br>Builds a timer which has a single associated [event](-init-.md#com.aheidelbacher.algostorm.engine.time.Timer$<init>(kotlin.Int, com.aheidelbacher.algostorm.event.Event)/event).`Timer(remainingTicks: Int, events: List<Event>)`<br>A timer which will post the associated [events](-init-.md#com.aheidelbacher.algostorm.engine.time.Timer$<init>(kotlin.Int, kotlin.collections.List((com.aheidelbacher.algostorm.event.Event)))/events) when [remainingTicks](-init-.md#com.aheidelbacher.algostorm.engine.time.Timer$<init>(kotlin.Int, kotlin.collections.List((com.aheidelbacher.algostorm.event.Event)))/remainingTicks) ticks
have elapsed. |

### Properties

| Name | Summary |
|---|---|
| [events](events.md) | `val events: List<Event>`<br>the events which will be posted when the timer expires |
| [remainingTicks](remaining-ticks.md) | `val remainingTicks: Int`<br>the number of ticks that need to pass until the
timer expires |
