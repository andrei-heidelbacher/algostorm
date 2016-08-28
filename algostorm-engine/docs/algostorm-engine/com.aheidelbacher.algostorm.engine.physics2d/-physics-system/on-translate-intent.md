[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.physics2d](../index.md) / [PhysicsSystem](index.md) / [onTranslateIntent](.)

# onTranslateIntent

`@Subscribe fun onTranslateIntent(event: `[`TransformIntent`](-transform-intent/index.md)`): Unit`

Upon receiving a [TransformIntent](-transform-intent/index.md) event, the the object is transformed
by the indicated amount. If the moved object is rigid and there are any
other rigid objects with their boxes overlapping the destination
location, the object is restored to its initial location and a
[Collision](../-collision/index.md) event is triggered with every overlapping object, having this
object as the source and each other object as the target.

### Parameters

`event` - the [TransformIntent](-transform-intent/index.md) event