[algostorm-engine](../../../index.md) / [com.aheidelbacher.algostorm.engine.state](../../index.md) / [Layer](../index.md) / [TileLayer](.)

# TileLayer

`class TileLayer : `[`Layer`](../index.md)

A layer which consists of `width x height` tiles, where `width` and
`height` are the dimensions of the containing [Map](../../-map/index.md).

### Constructors

| [&lt;init&gt;](-init-.md) | `TileLayer(name: String, data: LongArray, visible: Boolean = true, opacity: Float = 1F, offsetX: Int = 0, offsetY: Int = 0, properties: MutableMap<String, Any> = hashMapOf())`<br>A layer which consists of `width x height` tiles, where `width` and
`height` are the dimensions of the containing [Map](../../-map/index.md). |

### Properties

| [data](data.md) | `val data: LongArray`<br>the global ids of the tiles on the containing map. Index
`i` of this array represents the tile with `x = i % width` and
`y = i / width`. |
| [name](name.md) | `val name: String`<br>The name of this layer. Two layers are equal if and only if they have the
same name. |
| [offsetX](offset-x.md) | `val offsetX: Int`<br>The x-axis rendering offset in pixels. |
| [offsetY](offset-y.md) | `val offsetY: Int`<br>The y-axis rendering offset in pixels. |
| [opacity](opacity.md) | `var opacity: Float`<br>The opacity of this layer. Should be a value between `0` and `1`. |
| [properties](properties.md) | `val properties: MutableMap<String, Any>`<br>The properties of this layer. |
| [visible](visible.md) | `var visible: Boolean`<br>Whether this layer should be rendered or not. |

### Inherited Functions

| [equals](../equals.md) | `fun equals(other: Any?): Boolean` |
| [hashCode](../hash-code.md) | `fun hashCode(): Int` |

