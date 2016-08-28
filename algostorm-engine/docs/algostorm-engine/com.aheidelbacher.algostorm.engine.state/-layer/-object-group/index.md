[algostorm-engine](../../../index.md) / [com.aheidelbacher.algostorm.engine.state](../../index.md) / [Layer](../index.md) / [ObjectGroup](.)

# ObjectGroup

`class ObjectGroup : `[`Layer`](../index.md)

A layer which contains a set of [objects](objects.md).

### Types

| [DrawOrder](-draw-order/index.md) | `enum class DrawOrder`<br>The order in which objects are rendered. |

### Constructors

| [&lt;init&gt;](-init-.md) | `ObjectGroup(name: String, objects: MutableList<`[`Object`](../../-object/index.md)`>, drawOrder: `[`DrawOrder`](-draw-order/index.md)` = DrawOrder.TOP_DOWN, color: String? = null, visible: Boolean = true, opacity: Float = 1F, offsetX: Int = 0, offsetY: Int = 0, properties: MutableMap<String, Any> = hashMapOf())`<br>A layer which contains a set of [objects](-init-.md#com.aheidelbacher.algostorm.engine.state.Layer.ObjectGroup$<init>(kotlin.String, kotlin.collections.MutableList((com.aheidelbacher.algostorm.engine.state.Object)), com.aheidelbacher.algostorm.engine.state.Layer.ObjectGroup.DrawOrder, kotlin.String, kotlin.Boolean, kotlin.Float, kotlin.Int, kotlin.Int, kotlin.collections.MutableMap((kotlin.String, kotlin.Any)))/objects). |

### Properties

| [color](color.md) | `val color: String?`<br>the color with which objects that have their `gid` set to
`0` will be filled given in the "#AARRGGBB" format (base 16, case
insensitive) |
| [drawOrder](draw-order.md) | `val drawOrder: `[`DrawOrder`](-draw-order/index.md)<br>indicates the order in which the objects should be
rendered |
| [name](name.md) | `val name: String`<br>The name of this layer. Two layers are equal if and only if they have the
same name. |
| [objects](objects.md) | `val objects: MutableList<`[`Object`](../../-object/index.md)`>`<br>the set of objects contained by this layer |
| [offsetX](offset-x.md) | `val offsetX: Int`<br>The x-axis rendering offset in pixels. |
| [offsetY](offset-y.md) | `val offsetY: Int`<br>The y-axis rendering offset in pixels. |
| [opacity](opacity.md) | `var opacity: Float`<br>The opacity of this layer. Should be a value between `0` and `1`. |
| [properties](properties.md) | `val properties: MutableMap<String, Any>`<br>The properties of this layer. |
| [visible](visible.md) | `var visible: Boolean`<br>Whether this layer should be rendered or not. |

### Inherited Functions

| [equals](../equals.md) | `fun equals(other: Any?): Boolean` |
| [hashCode](../hash-code.md) | `fun hashCode(): Int` |

