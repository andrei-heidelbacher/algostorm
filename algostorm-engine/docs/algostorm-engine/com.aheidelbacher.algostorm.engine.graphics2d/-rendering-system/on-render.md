[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.graphics2d](../index.md) / [RenderingSystem](index.md) / [onRender](.)

# onRender

`@Subscribe fun onRender(event: `[`Render`](-render/index.md)`): Unit`

When a [Render](-render/index.md) event is received, the following method calls occur:
[Canvas.lock](../-canvas/lock.md), followed by [Canvas.clear](../-canvas/clear.md), followed by
[Canvas.drawBitmap](../-canvas/draw-bitmap.md) for every tile, image and renderable object in the
game, followed by [Canvas.unlockAndPost](../-canvas/unlock-and-post.md).

### Parameters

`event` - the rendering request