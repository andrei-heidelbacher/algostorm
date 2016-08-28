[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine](../index.md) / [Update](.)

# Update

`data class Update : Event`

An event which signals that all game logic should advance by an atomic time
unit. It is recommended to not change the game state after this event has
been processed by its subscribers.

### Constructors

| [&lt;init&gt;](-init-.md) | `Update(elapsedMillis: Int)`<br>An event which signals that all game logic should advance by an atomic time
unit. It is recommended to not change the game state after this event has
been processed by its subscribers. |

### Properties

| [elapsedMillis](elapsed-millis.md) | `val elapsedMillis: Int`<br>the number of milliseconds of an atomic time unit |

