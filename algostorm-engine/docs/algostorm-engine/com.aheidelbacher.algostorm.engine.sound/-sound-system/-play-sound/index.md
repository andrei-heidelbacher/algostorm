[algostorm-engine](../../../index.md) / [com.aheidelbacher.algostorm.engine.sound](../../index.md) / [SoundSystem](../index.md) / [PlaySound](.)

# PlaySound

`data class PlaySound : Event`

An event that requests a short sound to be played.

### Constructors

| [&lt;init&gt;](-init-.md) | `PlaySound(sound: String, loop: Boolean = false, onResult: (Int) -> Unit = null)`<br>An event that requests a short sound to be played. |

### Properties

| [loop](loop.md) | `val loop: Boolean`<br>whether the sound should be looped or not |
| [onResult](on-result.md) | `val onResult: (Int) -> Unit`<br>the callback which receives the returned stream id |
| [sound](sound.md) | `val sound: String`<br>the sound which should be played |

