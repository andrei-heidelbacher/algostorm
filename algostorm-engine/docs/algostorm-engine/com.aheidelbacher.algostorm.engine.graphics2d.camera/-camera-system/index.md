[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.graphics2d.camera](../index.md) / [CameraSystem](.)

# CameraSystem

`class CameraSystem : Subscriber`

### Types

| Name | Summary |
|---|---|
| [FocusOn](-focus-on/index.md) | `data class FocusOn : Event` |
| [Follow](-follow/index.md) | `data class Follow : Event` |
| [Scroll](-scroll/index.md) | `data class Scroll : Event` |
| [Unfollow](-unfollow.md) | `object Unfollow : Event` |
| [UpdateCamera](-update-camera.md) | `object UpdateCamera : Event` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `CameraSystem(camera: `[`Camera`](../-camera/index.md)`, objectManager: `[`ObjectManager`](../../com.aheidelbacher.algostorm.engine.state/-object-manager/index.md)`, publisher: Publisher, followedObjectId: Int? = null)` |

### Functions

| Name | Summary |
|---|---|
| [onFocusOn](on-focus-on.md) | `fun onFocusOn(event: `[`FocusOn`](-focus-on/index.md)`): Unit` |
| [onFollow](on-follow.md) | `fun onFollow(event: `[`Follow`](-follow/index.md)`): Unit` |
| [onScroll](on-scroll.md) | `fun onScroll(event: `[`Scroll`](-scroll/index.md)`): Unit` |
| [onUnfollow](on-unfollow.md) | `fun onUnfollow(event: `[`Unfollow`](-unfollow.md)`): Unit` |
| [onUpdate](on-update.md) | `fun onUpdate(event: `[`Update`](../../com.aheidelbacher.algostorm.engine/-update/index.md)`): Unit` |
| [onUpdateCamera](on-update-camera.md) | `fun onUpdateCamera(event: `[`UpdateCamera`](-update-camera.md)`): Unit` |
