[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.time](../index.md) / [Timer](index.md) / [&lt;init&gt;](.)

# &lt;init&gt;

`Timer(remainingTicks: Int, event: Event)`

Builds a timer which has a single associated [event](-init-.md#com.aheidelbacher.algostorm.engine.time.Timer$<init>(kotlin.Int, com.aheidelbacher.algostorm.event.Event)/event).

### Parameters

`remainingTicks` - the number of ticks that need to pass until the
timer expires

`event` - the event which will be posted when the timer expires

### Exceptions

`IllegalArgumentException` - if [remainingTicks](-init-.md#com.aheidelbacher.algostorm.engine.time.Timer$<init>(kotlin.Int, com.aheidelbacher.algostorm.event.Event)/remainingTicks) is negative

`Timer(remainingTicks: Int, events: List<Event>)`

A timer which will post the associated [events](-init-.md#com.aheidelbacher.algostorm.engine.time.Timer$<init>(kotlin.Int, kotlin.collections.List((com.aheidelbacher.algostorm.event.Event)))/events) when [remainingTicks](-init-.md#com.aheidelbacher.algostorm.engine.time.Timer$<init>(kotlin.Int, kotlin.collections.List((com.aheidelbacher.algostorm.event.Event)))/remainingTicks) ticks
have elapsed.

