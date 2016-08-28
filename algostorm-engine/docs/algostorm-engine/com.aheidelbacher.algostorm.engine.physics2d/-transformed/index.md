[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.physics2d](../index.md) / [Transformed](.)

# Transformed

`data class Transformed : Event`

An event which signals that the given object has been transformed.

Only the [PhysicsSystem](../-physics-system/index.md) should post this event.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Transformed(objectId: Int, dx: Int, dy: Int, rotate: Float)`<br>An event which signals that the given object has been transformed. |

### Properties

| Name | Summary |
|---|---|
| [dx](dx.md) | `val dx: Int`<br>the amount the object translated on the x-axis in pixels |
| [dy](dy.md) | `val dy: Int`<br>the amount the object translated on the y-axis in pixels |
| [objectId](object-id.md) | `val objectId: Int`<br>the id of the transformed object |
| [rotate](rotate.md) | `val rotate: Float`<br>the amount the object rotated in radians |
