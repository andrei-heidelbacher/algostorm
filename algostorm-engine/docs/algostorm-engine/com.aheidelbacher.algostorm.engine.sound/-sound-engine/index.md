[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.sound](../index.md) / [SoundEngine](.)

# SoundEngine

`interface SoundEngine`

An object that can play multiple sounds at once.

### Functions

| Name | Summary |
|---|---|
| [loadMusic](load-music.md) | `abstract fun loadMusic(musicSound: String): Unit`<br>Loads the sound at the specified location, making it available to future
calls of [playMusic](play-music.md). |
| [loadSound](load-sound.md) | `abstract fun loadSound(sound: String): Unit`<br>Loads the sound at the specified location, making it available to future
calls of [playSound](play-sound.md). |
| [playMusic](play-music.md) | `abstract fun playMusic(sound: String, loop: Boolean = false): Unit`<br>Plays the given sound on a dedicated stream. This should be used to play
longer sounds. Only one stream is active at all times for this type of
sounds. |
| [playSound](play-sound.md) | `abstract fun playSound(sound: String, loop: Boolean = false): Int`<br>Plays the given sound and returns the stream id on which the sound is
played. This should be used for short sounds (at most a few seconds). |
| [release](release.md) | `abstract fun release(): Unit`<br>Release all resources associated with this sound engine. |
| [stopMusic](stop-music.md) | `abstract fun stopMusic(): Unit`<br>Stops the stream dedicated to longer sounds. This will stop any sounds
played with [playMusic](play-music.md). |
| [stopStream](stop-stream.md) | `abstract fun stopStream(streamId: Int): Unit`<br>Stops the stream with the given id. |
