[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.time](../index.md) / [RegisterTimer](.)

# RegisterTimer

`data class RegisterTimer : Event`

An event which requests the creation of a [timer](timer.md).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `RegisterTimer(entityId: Int, timer: `[`Timer`](../-timer/index.md)`)`<br>An event which requests the creation of a [timer](-init-.md#com.aheidelbacher.algostorm.engine.time.RegisterTimer$<init>(kotlin.Int, com.aheidelbacher.algostorm.engine.time.Timer)/timer). |

### Properties

| Name | Summary |
|---|---|
| [entityId](entity-id.md) | `val entityId: Int` |
| [timer](timer.md) | `val timer: `[`Timer`](../-timer/index.md)<br>the timer that must be registered |
