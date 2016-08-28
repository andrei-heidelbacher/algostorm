[algostorm-engine](../../../index.md) / [com.aheidelbacher.algostorm.engine.physics2d](../../index.md) / [PhysicsSystem](../index.md) / [TransformIntent](.)

# TransformIntent

`data class TransformIntent : Event`

An event which signals a transformation that should be applied on the
given object.

### Constructors

| [&lt;init&gt;](-init-.md) | `TransformIntent(objectId: Int, dx: Int, dy: Int, rotate: Float)`<br>An event which signals a transformation that should be applied on the
given object. |

### Properties

| [dx](dx.md) | `val dx: Int`<br>the translation amount on the x-axis in pixels |
| [dy](dy.md) | `val dy: Int`<br>the translation amount on the y-axis in pixels |
| [objectId](object-id.md) | `val objectId: Int`<br>the id of the object which should be transformed |
| [rotate](rotate.md) | `val rotate: Float`<br>the rotation amount in radians |

