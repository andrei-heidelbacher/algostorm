[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.geometry2d](../index.md) / [Rectangle](index.md) / [intersects](.)

# intersects

`fun intersects(other: `[`Rectangle`](index.md)`): Boolean`

Returns whether the two rectangles intersect (that is, there is at least
one point `(x, y)` which is contained in both rectangles).

### Parameters

`other` - the rectangle with which the intersection is checked

**Return**
`true` if the two rectangles intersect, `false` otherwise

`fun intersects(x: Int, y: Int, width: Int, height: Int): Boolean`

Returns whether the two rectangles intersect (that is, there is at least
one point `(x, y)` which is contained in both rectangles).

### Parameters

`x` - the x-axis coordinate of the top-left corner of the other
rectangle

`y` - the y-axis coordinate of the top-left corner of the other
rectangle

`width` - the width of the other rectangle

`height` - the height of the other rectangle

### Exceptions

`IllegalArgumentException` - if [width](intersects.md#com.aheidelbacher.algostorm.engine.geometry2d.Rectangle$intersects(kotlin.Int, kotlin.Int, kotlin.Int, kotlin.Int)/width) or [height](intersects.md#com.aheidelbacher.algostorm.engine.geometry2d.Rectangle$intersects(kotlin.Int, kotlin.Int, kotlin.Int, kotlin.Int)/height) are not positive

**Return**
`true` if the two rectangles intersect, `false` otherwise

