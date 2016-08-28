[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.state](../index.md) / [Map](index.md) / [getTileId](.)

# getTileId

`fun getTileId(gid: Long): Int`

Returns the local tile id of the given [gid](get-tile-id.md#com.aheidelbacher.algostorm.engine.state.Map$getTileId(kotlin.Long)/gid).

### Parameters

`gid` - the searched global tile id

### Exceptions

`IndexOutOfBoundsException` - if the given [gid](get-tile-id.md#com.aheidelbacher.algostorm.engine.state.Map$getTileId(kotlin.Long)/gid) is not positive or
is greater than the total number of tiles contained in the map tile sets

**Return**
the requested local tile id

