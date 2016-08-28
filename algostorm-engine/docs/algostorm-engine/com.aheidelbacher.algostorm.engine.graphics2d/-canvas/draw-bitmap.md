[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.graphics2d](../index.md) / [Canvas](index.md) / [drawBitmap](.)

# drawBitmap

`abstract fun drawBitmap(image: String, x: Int, y: Int, width: Int, height: Int, matrix: `[`Matrix`](../-matrix/index.md)`, opacity: Float): Unit`

Draws the viewport projected on the indicated bitmap to the canvas using
the specified [matrix](draw-bitmap.md#com.aheidelbacher.algostorm.engine.graphics2d.Canvas$drawBitmap(kotlin.String, kotlin.Int, kotlin.Int, kotlin.Int, kotlin.Int, com.aheidelbacher.algostorm.engine.graphics2d.Matrix, kotlin.Float)/matrix).

### Parameters

`image` - the location of the bitmap

`x` - the x-axis coordinate in pixels of the top-left corner of the
bitmap viewport which should be rendered

`y` - the y-axis coordinate in pixels of the top-left corner of the
bitmap viewport which should be rendered

`width` - the width in pixels of the bitmap viewport which should be
rendered

`height` - the height in pixels of the bitmap viewport which should be
rendered

`matrix` - the matrix that should be applied to the viewport when
rendering. Initially, the viewport rectangle is considered to have the
top-left corner overlap with the top-left corner of the canvas.

`opacity` - the opacity of the image. Should be between `0` and `1`.

### Exceptions

`IllegalStateException` - if the canvas is not locked

`IllegalArgumentException` - if [opacity](draw-bitmap.md#com.aheidelbacher.algostorm.engine.graphics2d.Canvas$drawBitmap(kotlin.String, kotlin.Int, kotlin.Int, kotlin.Int, kotlin.Int, com.aheidelbacher.algostorm.engine.graphics2d.Matrix, kotlin.Float)/opacity) is not in the range `0..1`