[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.sound](../index.md) / [SoundEngine](index.md) / [playSound](.)

# playSound

`abstract fun playSound(sound: String, loop: Boolean = false): Int`

Plays the given sound and returns the stream id on which the sound is
played. This should be used for short sounds (at most a few seconds).

### Parameters

`sound` - the location of the sound which should be played

`loop` - whether the sound should be looped or not

### Exceptions

`IllegalArgumentException` - if the [sound](play-sound.md#com.aheidelbacher.algostorm.engine.sound.SoundEngine$playSound(kotlin.String, kotlin.Boolean)/sound) was not loaded

`IllegalStateException` - if this sound engine was released

**Return**
the id of the stream on which the sound is played, or `-1` if the
sound could not be played

