[algostorm-engine](../index.md) / [com.aheidelbacher.algostorm.engine.time](.)

## Package com.aheidelbacher.algostorm.engine.time

### Types

| Name | Summary |
|---|---|
| [RegisterTimer](-register-timer/index.md) | `data class RegisterTimer : Event`<br>An event which requests the creation of a [timer](-register-timer/timer.md). |
| [Tick](-tick/index.md) | `data class Tick : Event`<br>An event which signals an atomic time unit has passed. |
| [TimeSystem](-time-system/index.md) | `class TimeSystem : Subscriber`<br>A system that triggers every registered [Timer](-timer/index.md) when it expires. |
| [Timeline](-timeline/index.md) | `interface Timeline`<br>A container for the timers in the game. |
| [Timer](-timer/index.md) | `data class Timer`<br>A timer which will post the associated [events](-timer/events.md) when [remainingTicks](-timer/remaining-ticks.md) ticks
have elapsed. |
