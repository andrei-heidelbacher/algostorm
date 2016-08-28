[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.sound](../index.md) / [SoundEngine](index.md) / [loadMusic](.)

# loadMusic

`abstract fun loadMusic(musicSound: String): Unit`

Loads the sound at the specified location, making it available to future
calls of [playMusic](play-music.md).

### Parameters

`musicSound` - the location of the sound which should be loaded

### Exceptions

`FileNotFoundException` - if the given sound doesnt exist

`IllegalStateException` - if this sound engine was released