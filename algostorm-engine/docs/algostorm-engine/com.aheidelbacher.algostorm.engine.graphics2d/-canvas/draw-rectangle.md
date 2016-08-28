[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.graphics2d](../index.md) / [Canvas](index.md) / [drawRectangle](.)

# drawRectangle

`abstract fun drawRectangle(color: Int, width: Int, height: Int, matrix: `[`Matrix`](../-matrix/index.md)`, opacity: Float): Unit`

Draws the viewport projected on the indicated bitmap to the canvas using
the specified [matrix](draw-rectangle.md#com.aheidelbacher.algostorm.engine.graphics2d.Canvas$drawRectangle(kotlin.Int, kotlin.Int, kotlin.Int, com.aheidelbacher.algostorm.engine.graphics2d.Matrix, kotlin.Float)/matrix).

### Parameters

`color` - the color with which the rectangle should be filled in
ARGB8888 format

`width` - the width in pixels of the rectangle which should be
rendered

`height` - the height in pixels of the rectangle which should be
rendered

`matrix` - the matrix that should be applied to the rectangle when
rendering. Initially, the rectangle is considered to have the top-left
corner overlap with the top-left corner of the canvas.

`opacity` - the opacity of the image. Should be between `0` and `1`.

### Exceptions

`IllegalStateException` - if the canvas is not locked

`IllegalArgumentException` - if [opacity](draw-rectangle.md#com.aheidelbacher.algostorm.engine.graphics2d.Canvas$drawRectangle(kotlin.Int, kotlin.Int, kotlin.Int, com.aheidelbacher.algostorm.engine.graphics2d.Matrix, kotlin.Float)/opacity) is not in the range `0..1`