[algostorm-engine](../../../index.md) / [com.aheidelbacher.algostorm.engine.sound](../../index.md) / [SoundSystem](../index.md) / [StopStream](.)

# StopStream

`data class StopStream : Event`

An event which signals that the sound played on the given [streamId](stream-id.md)
should be stopped.

### Constructors

| [&lt;init&gt;](-init-.md) | `StopStream(streamId: Int)`<br>An event which signals that the sound played on the given [streamId](-init-.md#com.aheidelbacher.algostorm.engine.sound.SoundSystem.StopStream$<init>(kotlin.Int)/streamId)
should be stopped. |

### Properties

| [streamId](stream-id.md) | `val streamId: Int`<br>the id of the stream which should be stopped |

