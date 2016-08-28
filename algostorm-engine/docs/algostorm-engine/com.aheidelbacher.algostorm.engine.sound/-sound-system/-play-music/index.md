[algostorm-engine](../../../index.md) / [com.aheidelbacher.algostorm.engine.sound](../../index.md) / [SoundSystem](../index.md) / [PlayMusic](.)

# PlayMusic

`data class PlayMusic : Event`

An event that requests a longer sound to be played on a dedicated stream.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PlayMusic(sound: String, loop: Boolean = false)`<br>An event that requests a longer sound to be played on a dedicated stream. |

### Properties

| Name | Summary |
|---|---|
| [loop](loop.md) | `val loop: Boolean`<br>whether the sound should be looped or not |
| [sound](sound.md) | `val sound: String`<br>the sound which should be played |
