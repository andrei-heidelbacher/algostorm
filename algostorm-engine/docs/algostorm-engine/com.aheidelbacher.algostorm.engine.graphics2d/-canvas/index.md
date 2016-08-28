[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.graphics2d](../index.md) / [Canvas](.)

# Canvas

`interface Canvas`

A canvas that allows `draw` calls.

Every change to the canvas should be performed after it was locked and the
changes should become visible after it was unlocked.

After the canvas was locked, it must be unlocked.

### Properties

| Name | Summary |
|---|---|
| [height](height.md) | `abstract val height: Int`<br>The height of this canvas in pixels. |
| [width](width.md) | `abstract val width: Int`<br>The width of this canvas in pixels. |

### Functions

| Name | Summary |
|---|---|
| [clear](clear.md) | `abstract fun clear(): Unit`<br>Clears the canvas. |
| [drawBitmap](draw-bitmap.md) | `abstract fun drawBitmap(image: String, x: Int, y: Int, width: Int, height: Int, matrix: `[`Matrix`](../-matrix/index.md)`, opacity: Float): Unit`<br>Draws the viewport projected on the indicated bitmap to the canvas using
the specified [matrix](draw-bitmap.md#com.aheidelbacher.algostorm.engine.graphics2d.Canvas$drawBitmap(kotlin.String, kotlin.Int, kotlin.Int, kotlin.Int, kotlin.Int, com.aheidelbacher.algostorm.engine.graphics2d.Matrix, kotlin.Float)/matrix). |
| [drawColor](draw-color.md) | `abstract fun drawColor(color: Int): Unit`<br>Fills the entire canvas with the given color. |
| [drawRectangle](draw-rectangle.md) | `abstract fun drawRectangle(color: Int, width: Int, height: Int, matrix: `[`Matrix`](../-matrix/index.md)`, opacity: Float): Unit`<br>Draws the viewport projected on the indicated bitmap to the canvas using
the specified [matrix](draw-rectangle.md#com.aheidelbacher.algostorm.engine.graphics2d.Canvas$drawRectangle(kotlin.Int, kotlin.Int, kotlin.Int, com.aheidelbacher.algostorm.engine.graphics2d.Matrix, kotlin.Float)/matrix). |
| [loadBitmap](load-bitmap.md) | `abstract fun loadBitmap(image: String): Unit`<br>Loads the image at the given location. |
| [lock](lock.md) | `abstract fun lock(): Unit`<br>Locks this canvas and allows editing the canvas content. |
| [unloadBitmaps](unload-bitmaps.md) | `abstract fun unloadBitmaps(): Unit`<br>Releases all loaded bitmap resources, making them unavailable to future
[drawBitmap](draw-bitmap.md) calls, unless explicitly loaded again. |
| [unlockAndPost](unlock-and-post.md) | `abstract fun unlockAndPost(): Unit`<br>Unlocks this canvas and posts all the changes made since the canvas was
locked. |
