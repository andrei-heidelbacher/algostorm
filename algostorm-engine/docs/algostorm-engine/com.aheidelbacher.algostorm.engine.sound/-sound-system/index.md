[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.sound](../index.md) / [SoundSystem](.)

# SoundSystem

`class SoundSystem : Subscriber`

A system which handles playing and stopping sounds.

### Types

| Name | Summary |
|---|---|
| [PlayMusic](-play-music/index.md) | `data class PlayMusic : Event`<br>An event that requests a longer sound to be played on a dedicated stream. |
| [PlaySound](-play-sound/index.md) | `data class PlaySound : Event`<br>An event that requests a short sound to be played. |
| [StopMusic](-stop-music.md) | `object StopMusic : Event`<br>An event which signals that the sound played on the dedicated stream for
longer sounds should be stopped (sounds played with [PlayMusic](-play-music/index.md) will be
stopped). |
| [StopStream](-stop-stream/index.md) | `data class StopStream : Event`<br>An event which signals that the sound played on the given [streamId](-stop-stream/stream-id.md)
should be stopped. |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SoundSystem(soundEngine: `[`SoundEngine`](../-sound-engine/index.md)`, musicSounds: List<String>, sounds: List<String>)`<br>A system which handles playing and stopping sounds. |

### Functions

| Name | Summary |
|---|---|
| [onPlayMusic](on-play-music.md) | `fun onPlayMusic(event: `[`PlayMusic`](-play-music/index.md)`): Unit`<br>After receiving a [PlayMusic](-play-music/index.md) event, the [SoundEngine.playMusic](../-sound-engine/play-music.md) method
is called. |
| [onPlaySoundEffect](on-play-sound-effect.md) | `fun onPlaySoundEffect(event: `[`PlaySound`](-play-sound/index.md)`): Unit`<br>After receiving a [PlaySound](-play-sound/index.md) event, the [SoundEngine.playSound](../-sound-engine/play-sound.md) method
is called. |
| [onStopMusic](on-stop-music.md) | `fun onStopMusic(event: `[`StopMusic`](-stop-music.md)`): Unit`<br>After receiving a [StopMusic](-stop-music.md) event, the [SoundEngine.stopMusic](../-sound-engine/stop-music.md) method
is called. |
| [onStopStream](on-stop-stream.md) | `fun onStopStream(event: `[`StopStream`](-stop-stream/index.md)`): Unit`<br>After receiving a [StopStream](-stop-stream/index.md) event, the [SoundEngine.stopStream](../-sound-engine/stop-stream.md) method
is called. |
