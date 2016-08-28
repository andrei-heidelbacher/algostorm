[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.time](../index.md) / [TimeSystem](.)

# TimeSystem

`class TimeSystem : Subscriber`

A system that triggers every registered [Timer](../-timer/index.md) when it expires.

### Constructors

| [&lt;init&gt;](-init-.md) | `TimeSystem(timeline: `[`Timeline`](../-timeline/index.md)`, publisher: Publisher)`<br>A system that triggers every registered [Timer](../-timer/index.md) when it expires. |

### Functions

| [onRegisterTimer](on-register-timer.md) | `fun onRegisterTimer(event: `[`RegisterTimer`](../-register-timer/index.md)`): Unit`<br>Upon receiving a [RegisterTimer](../-register-timer/index.md) event, it adds it to the [timeline](#). |
| [onTick](on-tick.md) | `fun onTick(event: `[`Tick`](../-tick/index.md)`): Unit`<br>Upon receiving a [Tick](../-tick/index.md) event, it updates the [timeline](#) timers and
triggers the expired ones. |

