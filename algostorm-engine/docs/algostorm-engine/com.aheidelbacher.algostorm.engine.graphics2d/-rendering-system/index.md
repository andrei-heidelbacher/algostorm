[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.graphics2d](../index.md) / [RenderingSystem](.)

# RenderingSystem

`class RenderingSystem : Subscriber`

A system which handles the rendering of all objects in the game to the screen
canvas.

Every method call to the [canvas](#) is made from the private engine thread.

### Types

| Name | Summary |
|---|---|
| [Render](-render/index.md) | `data class Render : Event`<br>An event which requests the rendering of the entire game state to the
screen. |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `RenderingSystem(map: `[`Map`](../../com.aheidelbacher.algostorm.engine.state/-map/index.md)`, canvas: `[`Canvas`](../-canvas/index.md)`)`<br>A system which handles the rendering of all objects in the game to the screen
canvas. |

### Functions

| Name | Summary |
|---|---|
| [onRender](on-render.md) | `fun onRender(event: `[`Render`](-render/index.md)`): Unit`<br>When a [Render](-render/index.md) event is received, the following method calls occur:
[Canvas.lock](../-canvas/lock.md), followed by [Canvas.clear](../-canvas/clear.md), followed by
[Canvas.drawBitmap](../-canvas/draw-bitmap.md) for every tile, image and renderable object in the
game, followed by [Canvas.unlockAndPost](../-canvas/unlock-and-post.md). |
| [onUpdate](on-update.md) | `fun onUpdate(event: `[`Update`](../../com.aheidelbacher.algostorm.engine/-update/index.md)`): Unit`<br>When an [Update](../../com.aheidelbacher.algostorm.engine/-update/index.md) event is received, the [currentTimeMillis](#) is increased. |

### Companion Object Functions

| Name | Summary |
|---|---|
| [getViewport](get-viewport.md) | `fun `[`Map`](../../com.aheidelbacher.algostorm.engine.state/-map/index.md)`.getViewport(gid: Long, currentTimeMillis: Long): `[`Viewport`](../../com.aheidelbacher.algostorm.engine.state/-tile-set/-viewport/index.md) |
| [isVisible](is-visible.md) | `fun isVisible(camera: `[`Rectangle`](../../com.aheidelbacher.algostorm.engine.geometry2d/-rectangle/index.md)`, gid: Long, x: Int, y: Int, width: Int, height: Int): Boolean`<br>`fun isVisible(camera: `[`Rectangle`](../../com.aheidelbacher.algostorm.engine.geometry2d/-rectangle/index.md)`, obj: `[`Object`](../../com.aheidelbacher.algostorm.engine.state/-object/index.md)`, color: Int?): Boolean` |
