[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.state](../index.md) / [Layer](.)

# Layer

`sealed class Layer`

An abstract layer in the game world.

### Types

| Name | Summary |
|---|---|
| [ImageLayer](-image-layer/index.md) | `class ImageLayer : Layer`<br>A layer which consists of a single [image](-image-layer/image.md). |
| [ObjectGroup](-object-group/index.md) | `class ObjectGroup : Layer`<br>A layer which contains a set of [objects](-object-group/objects.md). |
| [TileLayer](-tile-layer/index.md) | `class TileLayer : Layer`<br>A layer which consists of `width x height` tiles, where `width` and
`height` are the dimensions of the containing [Map](../-map/index.md). |

### Properties

| Name | Summary |
|---|---|
| [name](name.md) | `abstract val name: String`<br>The name of this layer. Two layers are equal if and only if they have the
same name. |
| [offsetX](offset-x.md) | `abstract val offsetX: Int`<br>The x-axis rendering offset in pixels. |
| [offsetY](offset-y.md) | `abstract val offsetY: Int`<br>The y-axis rendering offset in pixels. |
| [opacity](opacity.md) | `abstract var opacity: Float`<br>The opacity of this layer. Should be a value between `0` and `1`. |
| [properties](properties.md) | `abstract val properties: MutableMap<String, Any>`<br>The properties of this layer. |
| [visible](visible.md) | `abstract var visible: Boolean`<br>Whether this layer should be rendered or not. |

### Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | `fun equals(other: Any?): Boolean` |
| [hashCode](hash-code.md) | `fun hashCode(): Int` |

### Inheritors

| Name | Summary |
|---|---|
| [ImageLayer](-image-layer/index.md) | `class ImageLayer : Layer`<br>A layer which consists of a single [image](-image-layer/image.md). |
| [ObjectGroup](-object-group/index.md) | `class ObjectGroup : Layer`<br>A layer which contains a set of [objects](-object-group/objects.md). |
| [TileLayer](-tile-layer/index.md) | `class TileLayer : Layer`<br>A layer which consists of `width x height` tiles, where `width` and
`height` are the dimensions of the containing [Map](../-map/index.md). |
