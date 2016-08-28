[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.sound](../index.md) / [SoundEngine](index.md) / [playMusic](.)

# playMusic

`abstract fun playMusic(sound: String, loop: Boolean = false): Unit`

Plays the given sound on a dedicated stream. This should be used to play
longer sounds. Only one stream is active at all times for this type of
sounds.

### Parameters

`sound` - the location of the sound which should be played

`loop` - whether the sound should be looped or not

### Exceptions

`IllegalArgumentException` - if the [sound](play-music.md#com.aheidelbacher.algostorm.engine.sound.SoundEngine$playMusic(kotlin.String, kotlin.Boolean)/sound) was not loaded

`IllegalStateException` - if this sound engine was released

**Return**
the id of the stream on which the sound is played, or `-1` if the
sound could not be played

