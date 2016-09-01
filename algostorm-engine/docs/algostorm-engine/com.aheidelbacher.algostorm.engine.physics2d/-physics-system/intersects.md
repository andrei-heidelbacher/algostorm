[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.physics2d](../index.md) / [PhysicsSystem](index.md) / [intersects](.)

# intersects

`fun `[`Object`](../../com.aheidelbacher.algostorm.engine.tiled/-object/index.md)`.intersects(other: `[`Object`](../../com.aheidelbacher.algostorm.engine.tiled/-object/index.md)`): Boolean`

Returns whether the two objects intersect (that is, there exists a
pixel `(x, y)` such that it lies inside both objects).

### Parameters

`other` - the object with which the intersection is checked

**Return**
`true` if the two objects overlap, `false` otherwise

`fun `[`Object`](../../com.aheidelbacher.algostorm.engine.tiled/-object/index.md)`.intersects(x: Int, y: Int, width: Int, height: Int): Boolean`

Returns whether this object intersects with the specified rectangle
(that is, there exists a pixel `(x, y)` such that it lies inside this
object and inside the given rectangle).

### Parameters

`x` - the x-axis coordinate of the top-left corner of the
rectangle in pixels

`y` - the y-axis coordinate of the top-left corner of the
rectangle in pixels

`width` - the width of the rectangle in pixels

`height` - the height of the rectangle in pixels

### Exceptions

`IllegalArgumentException` - if the given [width](intersects.md#com.aheidelbacher.algostorm.engine.physics2d.PhysicsSystem.Companion$intersects(com.aheidelbacher.algostorm.engine.tiled.Object, kotlin.Int, kotlin.Int, kotlin.Int, kotlin.Int)/width) or [height](intersects.md#com.aheidelbacher.algostorm.engine.physics2d.PhysicsSystem.Companion$intersects(com.aheidelbacher.algostorm.engine.tiled.Object, kotlin.Int, kotlin.Int, kotlin.Int, kotlin.Int)/height) are
not positive

**Return**
`true` if the two objects overlap, `false` otherwise

