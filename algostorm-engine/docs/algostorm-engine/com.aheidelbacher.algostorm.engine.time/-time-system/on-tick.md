[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.time](../index.md) / [TimeSystem](index.md) / [onTick](.)

# onTick

`@Subscribe fun onTick(event: `[`Tick`](../-tick/index.md)`): Unit`

Upon receiving a [Tick](../-tick/index.md) event, it updates the [timeline](#) timers and
triggers the expired ones.

### Parameters

`event` - the event which signals a tick has elapsed