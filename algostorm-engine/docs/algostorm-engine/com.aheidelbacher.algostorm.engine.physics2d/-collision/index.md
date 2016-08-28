[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.physics2d](../index.md) / [Collision](.)

# Collision

`data class Collision : Event`

An event which signals that [sourceId](source-id.md) collided with [targetId](target-id.md).

Only the [PhysicsSystem](../-physics-system/index.md) should post this event.

### Constructors

| [&lt;init&gt;](-init-.md) | `Collision(sourceId: Int, targetId: Int)`<br>An event which signals that [sourceId](-init-.md#com.aheidelbacher.algostorm.engine.physics2d.Collision$<init>(kotlin.Int, kotlin.Int)/sourceId) collided with [targetId](-init-.md#com.aheidelbacher.algostorm.engine.physics2d.Collision$<init>(kotlin.Int, kotlin.Int)/targetId). |

### Properties

| [sourceId](source-id.md) | `val sourceId: Int`<br>the id of the object that triggered the collision |
| [targetId](target-id.md) | `val targetId: Int`<br>the id of the object that was collided |

