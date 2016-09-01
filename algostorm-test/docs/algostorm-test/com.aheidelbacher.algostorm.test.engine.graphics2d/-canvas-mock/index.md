[algostorm-test](../../index.md) / [com.aheidelbacher.algostorm.test.engine.graphics2d](../index.md) / [CanvasMock](.)

# CanvasMock

`class CanvasMock : Canvas`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `CanvasMock()` |

### Properties

| Name | Summary |
|---|---|
| [height](height.md) | `val height: Int` |
| [width](width.md) | `val width: Int` |

### Functions

| Name | Summary |
|---|---|
| [clear](clear.md) | `fun clear(): Unit` |
| [drawBitmap](draw-bitmap.md) | `fun drawBitmap(image: String, x: Int, y: Int, width: Int, height: Int, matrix: Matrix, opacity: Float): Unit` |
| [drawColor](draw-color.md) | `fun drawColor(color: Int): Unit` |
| [drawRectangle](draw-rectangle.md) | `fun drawRectangle(color: Int, width: Int, height: Int, matrix: Matrix, opacity: Float): Unit` |
| [loadBitmap](load-bitmap.md) | `fun loadBitmap(image: String): Unit` |
| [lock](lock.md) | `fun lock(): Unit` |
| [unloadBitmaps](unload-bitmaps.md) | `fun unloadBitmaps(): Unit` |
| [unlockAndPost](unlock-and-post.md) | `fun unlockAndPost(): Unit` |
| [verifyBitmap](verify-bitmap.md) | `fun verifyBitmap(image: String, x: Int, y: Int, width: Int, height: Int, matrix: Matrix, opacity: Float): Unit` |
| [verifyClear](verify-clear.md) | `fun verifyClear(): Unit` |
| [verifyColor](verify-color.md) | `fun verifyColor(color: Int): Unit` |
| [verifyEmptyDrawQueue](verify-empty-draw-queue.md) | `fun verifyEmptyDrawQueue(): Unit` |
| [verifyRectangle](verify-rectangle.md) | `fun verifyRectangle(color: Int, width: Int, height: Int, matrix: Matrix, opacity: Float): Unit` |
