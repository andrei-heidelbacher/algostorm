[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.state](../index.md) / [TileSet](index.md) / [getTileProperties](.)

# getTileProperties

`fun getTileProperties(tileId: Int): Map<String, Any>`

Returns the properties associated to the given tile id.

### Parameters

`tileId` - the id of the requested tile

### Exceptions

`IndexOutOfBoundsException` - if the given [tileId](get-tile-properties.md#com.aheidelbacher.algostorm.engine.state.TileSet$getTileProperties(kotlin.Int)/tileId) is negative or if
it is greater than or equal to [tileCount](tile-count.md)

**Return**
the properties of the requested tile

