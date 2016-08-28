[algostorm-engine](../index.md) / [com.aheidelbacher.algostorm.engine](.)

## Package com.aheidelbacher.algostorm.engine

### Types

| Name | Summary |
|---|---|
| [Engine](-engine/index.md) | `abstract class Engine`<br>An asynchronous engine that runs the game loop on its own private thread. |
| [Update](-update/index.md) | `data class Update : Event`<br>An event which signals that all game logic should advance by an atomic time
unit. It is recommended to not change the game state after this event has
been processed by its subscribers. |
