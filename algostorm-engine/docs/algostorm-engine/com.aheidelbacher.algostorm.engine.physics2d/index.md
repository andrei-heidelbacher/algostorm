[algostorm-engine](../index.md) / [com.aheidelbacher.algostorm.engine.physics2d](.)

## Package com.aheidelbacher.algostorm.engine.physics2d

### Types

| Name | Summary |
|---|---|
| [Collision](-collision/index.md) | `data class Collision : Event`<br>An event which signals that [sourceId](-collision/source-id.md) collided with [targetId](-collision/target-id.md). |
| [PhysicsSystem](-physics-system/index.md) | `class PhysicsSystem : Subscriber`<br>A system that handles [TransformIntent](-physics-system/-transform-intent/index.md) events and publishes [Transformed](-transformed/index.md)
and [Collision](-collision/index.md) events. |
| [Transformed](-transformed/index.md) | `data class Transformed : Event`<br>An event which signals that the given object has been transformed. |
