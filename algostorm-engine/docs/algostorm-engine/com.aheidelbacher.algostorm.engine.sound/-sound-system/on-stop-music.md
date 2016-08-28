[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.sound](../index.md) / [SoundSystem](index.md) / [onStopMusic](.)

# onStopMusic

`@Subscribe fun onStopMusic(event: `[`StopMusic`](-stop-music.md)`): Unit`

After receiving a [StopMusic](-stop-music.md) event, the [SoundEngine.stopMusic](../-sound-engine/stop-music.md) method
is called.

### Parameters

`event` - the event which requests the dedicated stream for longer
sounds to be stopped