[algostorm-engine](../index.md) / [com.aheidelbacher.algostorm.engine.geometry2d](.)

## Package com.aheidelbacher.algostorm.engine.geometry2d

### Types

| Name | Summary |
|---|---|
| [Circle](-circle/index.md) | `data class Circle` |
| [Point](-point/index.md) | `data class Point` |
| [Rectangle](-rectangle/index.md) | `data class Rectangle`<br>A rectangle which covers the area `[x, x + width - 1] x [y, y + height - 1]`. |

### Functions

| Name | Summary |
|---|---|
| [distance](distance.md) | `fun distance(x0: Int, y0: Int, x1: Int, y1: Int): Float` |
| [intersects](intersects.md) | `fun intersects(x: Int, y: Int, width: Int, height: Int, otherX: Int, otherY: Int, otherWidth: Int, otherHeight: Int): Boolean` |
| [squareDistance](square-distance.md) | `fun squareDistance(x0: Int, y0: Int, x1: Int, y1: Int): Long` |
