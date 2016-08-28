[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.state](../index.md) / [ObjectManager](index.md) / [create](.)

# create

`fun create(name: String = "", type: String = "", x: Int, y: Int, width: Int, height: Int, gid: Long = 0L, rotation: Float = 0F, visible: Boolean = true, properties: MutableMap<String, Any> = hashMapOf()): `[`Object`](../-object/index.md)

Creates an object with the specified parameters, adds it to this object
group and returns it.

### Parameters

`name` - the name of this object

`type` - the type of this object

`x` - the x-axis coordinate of the top-left corner of this object in
pixels

`y` - the y-axis coordinate of the top-left corner of this object in
pixels

`width` - the width of this object in pixels

`height` - the height of this object in pixels

`gid` - the global id of the object tile. A value of `0` indicates the
empty tile (nothing to draw)

`rotation` - the rotation of this object around the top-left corner in
clock-wise degrees

`visible` - whether this object should be rendered or not

`properties` - the properties of this object

### Exceptions

`IllegalStateException` - if there are too many objects in this
object group

`IllegalArgumentException` - if [gid](create.md#com.aheidelbacher.algostorm.engine.state.ObjectManager$create(kotlin.String, kotlin.String, kotlin.Int, kotlin.Int, kotlin.Int, kotlin.Int, kotlin.Long, kotlin.Float, kotlin.Boolean, kotlin.collections.MutableMap((kotlin.String, kotlin.Any)))/gid) is negative or if [width](create.md#com.aheidelbacher.algostorm.engine.state.ObjectManager$create(kotlin.String, kotlin.String, kotlin.Int, kotlin.Int, kotlin.Int, kotlin.Int, kotlin.Long, kotlin.Float, kotlin.Boolean, kotlin.collections.MutableMap((kotlin.String, kotlin.Any)))/width) or
[height](create.md#com.aheidelbacher.algostorm.engine.state.ObjectManager$create(kotlin.String, kotlin.String, kotlin.Int, kotlin.Int, kotlin.Int, kotlin.Int, kotlin.Long, kotlin.Float, kotlin.Boolean, kotlin.collections.MutableMap((kotlin.String, kotlin.Any)))/height) are not positive

**Return**
the created object

