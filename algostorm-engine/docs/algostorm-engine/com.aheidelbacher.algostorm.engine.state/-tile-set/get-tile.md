[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.state](../index.md) / [TileSet](index.md) / [getTile](.)

# getTile

`fun getTile(tileId: Int): `[`Tile`](-tile/index.md)

Returns the [Tile](-tile/index.md) with the given tile id.

### Parameters

`tileId` - the id of the requested tile

### Exceptions

`IndexOutOfBoundsException` - if the given [tileId](get-tile.md#com.aheidelbacher.algostorm.engine.state.TileSet$getTile(kotlin.Int)/tileId) is negative or if
it is greater than or equal to [tileCount](tile-count.md)

**Return**
the requested tile data

