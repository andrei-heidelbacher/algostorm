[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.state](../index.md) / [Map](index.md) / [getTileSet](.)

# getTileSet

`fun getTileSet(gid: Long): `[`TileSet`](../-tile-set/index.md)

Returns the tile set which contains the given [gid](get-tile-set.md#com.aheidelbacher.algostorm.engine.state.Map$getTileSet(kotlin.Long)/gid).

### Parameters

`gid` - the searched global tile id

### Exceptions

`IndexOutOfBoundsException` - if the given [gid](get-tile-set.md#com.aheidelbacher.algostorm.engine.state.Map$getTileSet(kotlin.Long)/gid) is not positive or
is greater than the total number of tiles contained in the map tile sets

**Return**
the requested tile set

