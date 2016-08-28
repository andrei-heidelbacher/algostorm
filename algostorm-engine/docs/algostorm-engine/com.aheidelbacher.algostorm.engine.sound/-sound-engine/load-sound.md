[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.sound](../index.md) / [SoundEngine](index.md) / [loadSound](.)

# loadSound

`abstract fun loadSound(sound: String): Unit`

Loads the sound at the specified location, making it available to future
calls of [playSound](play-sound.md).

### Parameters

`sound` - the location of the sound which should be loaded

### Exceptions

`FileNotFoundException` - if the given sound doesnt exist

`IllegalStateException` - if this sound engine was released