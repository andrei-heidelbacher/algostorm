[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.geometry2d](../index.md) / [Rectangle](.)

# Rectangle

`data class Rectangle`

A rectangle which covers the area `[x, x + width - 1] x [y, y + height - 1]`.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Rectangle(x: Int, y: Int, width: Int, height: Int)`<br>A rectangle which covers the area `[x, x + width - 1] x [y, y + height - 1]`. |

### Properties

| Name | Summary |
|---|---|
| [centerX](center-x.md) | `val centerX: Int`<br>The x-axis coordinate of the center of the rectangle. |
| [centerY](center-y.md) | `val centerY: Int`<br>The y-axis coordinate of the center of the rectangle. |
| [height](height.md) | `val height: Int`<br>the height of the rectangle |
| [width](width.md) | `val width: Int`<br>the width of the rectangle |
| [x](x.md) | `val x: Int`<br>the x-axis coordinate of the upper-left corner of the rectangle |
| [y](y.md) | `val y: Int`<br>the y-axis coordinate of the upper-left corner of the rectangle |

### Functions

| Name | Summary |
|---|---|
| [contains](contains.md) | `fun contains(x: Int, y: Int): Boolean`<br>`operator fun contains(point: `[`Point`](../-point/index.md)`): Boolean`<br>Checks if the given point is inside this rectangle. |
| [intersects](intersects.md) | `fun intersects(other: Rectangle): Boolean`<br>`fun intersects(x: Int, y: Int, width: Int, height: Int): Boolean`<br>Returns whether the two rectangles intersect (that is, there is at least
one point `(x, y)` which is contained in both rectangles). |
| [translate](translate.md) | `fun translate(dx: Int, dy: Int): Rectangle`<br>Returns a copy of this rectangle with the top-left corner translated by
the given amount. |
