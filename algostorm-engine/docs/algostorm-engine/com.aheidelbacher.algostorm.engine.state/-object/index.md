[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.state](../index.md) / [Object](.)

# Object

`class Object`

A physical and renderable object in the game. Two objects are equal if and
only if they have the same [id](id.md).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Object(id: Int, name: String = "", type: String = "", x: Int, y: Int, width: Int, height: Int, gid: Long = 0L, rotation: Float = 0F, visible: Boolean = true, properties: MutableMap<String, Any> = hashMapOf())`<br>A physical and renderable object in the game. Two objects are equal if and
only if they have the same [id](-init-.md#com.aheidelbacher.algostorm.engine.state.Object$<init>(kotlin.Int, kotlin.String, kotlin.String, kotlin.Int, kotlin.Int, kotlin.Int, kotlin.Int, kotlin.Long, kotlin.Float, kotlin.Boolean, kotlin.collections.MutableMap((kotlin.String, kotlin.Any)))/id). |

### Properties

| Name | Summary |
|---|---|
| [gid](gid.md) | `var gid: Long`<br>The global id of the object tile. A value of `0` indicates the empty tile
(nothing to draw). |
| [height](height.md) | `var height: Int`<br>The height of this object in pixels. Must be positive. |
| [id](id.md) | `val id: Int`<br>the unique identifier of this object |
| [name](name.md) | `val name: String`<br>the name of this object |
| [properties](properties.md) | `val properties: MutableMap<String, Any>`<br>the properties of this object |
| [rotation](rotation.md) | `var rotation: Float`<br>the clock-wise rotation of this object around the top-left
corner in degrees |
| [type](type.md) | `val type: String`<br>the type of this object |
| [visible](visible.md) | `var visible: Boolean`<br>whether this object should be rendered or not |
| [width](width.md) | `var width: Int`<br>The width of this object in pixels. |
| [x](x.md) | `var x: Int`<br>the x-axis coordinate of the top-left corner of this object in
pixels |
| [y](y.md) | `var y: Int`<br>the y-axis coordinate of the top-left corner of this object in
pixels |

### Functions

| Name | Summary |
|---|---|
| [contains](contains.md) | `operator fun contains(propertyName: String): Boolean`<br>Checks whether this object contains a property with the given name. |
| [equals](equals.md) | `fun equals(other: Any?): Boolean` |
| [get](get.md) | `operator fun get(propertyName: String): Any?`<br>Returns the property with the given name. |
| [hashCode](hash-code.md) | `fun hashCode(): Int` |
| [remove](remove.md) | `fun remove(propertyName: String): Unit`<br>Removes the property with the given name. |
| [set](set.md) | `operator fun <T : Any> set(propertyName: String, value: T): Unit`<br>Sets the value of the property with the given name. |

### Companion Object Functions

| Name | Summary |
|---|---|
| [validateGid](validate-gid.md) | `fun validateGid(id: Int, gid: Long): Unit` |
| [validateHeight](validate-height.md) | `fun validateHeight(id: Int, height: Int): Unit` |
| [validateWidth](validate-width.md) | `fun validateWidth(id: Int, width: Int): Unit` |

### Companion Object Extension Properties

| Name | Summary |
|---|---|
| [isRigid](../../com.aheidelbacher.algostorm.engine.physics2d/-physics-system/is-rigid.md) | `val Object.isRigid: Boolean`<br>Returns `true` if this object contains the rigid property and it is set
to `true`, `false` otherwise. |

### Companion Object Extension Functions

| Name | Summary |
|---|---|
| [intersects](../../com.aheidelbacher.algostorm.engine.physics2d/-physics-system/intersects.md) | `fun Object.intersects(other: Object): Boolean`<br>Returns whether the two objects intersect (that is, there exists a
pixel `(x, y)` such that it lies inside both objects).`fun Object.intersects(x: Int, y: Int, width: Int, height: Int): Boolean`<br>Returns whether this object intersects with the specified rectangle
(that is, there exists a pixel `(x, y)` such that it lies inside this
object and inside the given rectangle). |
| [transform](../../com.aheidelbacher.algostorm.engine.physics2d/-physics-system/transform.md) | `fun Object.transform(dx: Int, dy: Int, rotate: Float): Unit`<br>Transforms this object with the given amounts. |
