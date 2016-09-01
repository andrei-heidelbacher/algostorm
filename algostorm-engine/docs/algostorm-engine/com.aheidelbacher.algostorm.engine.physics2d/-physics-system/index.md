[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.physics2d](../index.md) / [PhysicsSystem](.)

# PhysicsSystem

`class PhysicsSystem : Subscriber`

A system that handles [TransformIntent](-transform-intent/index.md) events and publishes [Transformed](../-transformed/index.md)
and [Collision](../-collision/index.md) events.

### Types

| Name | Summary |
|---|---|
| [TransformIntent](-transform-intent/index.md) | `data class TransformIntent : Event`<br>An event which signals a transformation that should be applied on the
given object. |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PhysicsSystem(objectManager: `[`ObjectManager`](../../com.aheidelbacher.algostorm.engine.tiled/-object-manager/index.md)`, publisher: Publisher)`<br>A system that handles [TransformIntent](-transform-intent/index.md) events and publishes [Transformed](../-transformed/index.md)
and [Collision](../-collision/index.md) events. |

### Functions

| Name | Summary |
|---|---|
| [onTranslateIntent](on-translate-intent.md) | `fun onTranslateIntent(event: `[`TransformIntent`](-transform-intent/index.md)`): Unit`<br>Upon receiving a [TransformIntent](-transform-intent/index.md) event, the the object is transformed
by the indicated amount. If the moved object is rigid and there are any
other rigid objects with their boxes overlapping the destination
location, the object is restored to its initial location and a
[Collision](../-collision/index.md) event is triggered with every overlapping object, having this
object as the source and each other object as the target. |

### Companion Object Properties

| Name | Summary |
|---|---|
| [IS_RIGID](-i-s_-r-i-g-i-d.md) | `const val IS_RIGID: String`<br>The name of the rigid property. It is of type [Boolean](#). |
| [isRigid](is-rigid.md) | `val `[`Object`](../../com.aheidelbacher.algostorm.engine.tiled/-object/index.md)`.isRigid: Boolean`<br>Returns `true` if this object contains the rigid property and it is set
to `true`, `false` otherwise. |

### Companion Object Functions

| Name | Summary |
|---|---|
| [intersects](intersects.md) | `fun `[`Object`](../../com.aheidelbacher.algostorm.engine.tiled/-object/index.md)`.intersects(other: `[`Object`](../../com.aheidelbacher.algostorm.engine.tiled/-object/index.md)`): Boolean`<br>Returns whether the two objects intersect (that is, there exists a
pixel `(x, y)` such that it lies inside both objects).`fun `[`Object`](../../com.aheidelbacher.algostorm.engine.tiled/-object/index.md)`.intersects(x: Int, y: Int, width: Int, height: Int): Boolean`<br>Returns whether this object intersects with the specified rectangle
(that is, there exists a pixel `(x, y)` such that it lies inside this
object and inside the given rectangle). |
| [transform](transform.md) | `fun `[`Object`](../../com.aheidelbacher.algostorm.engine.tiled/-object/index.md)`.transform(dx: Int, dy: Int, rotate: Float): Unit`<br>Transforms this object with the given amounts. |
