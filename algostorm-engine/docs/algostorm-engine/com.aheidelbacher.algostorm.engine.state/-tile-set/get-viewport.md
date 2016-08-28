[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.state](../index.md) / [TileSet](index.md) / [getViewport](.)

# getViewport

`fun getViewport(tileId: Int): `[`Viewport`](-viewport/index.md)

Returns a viewport corresponding to the given tile id, by applying the
appropriate margin and spacing offsets.

### Parameters

`tileId` - the id of the requested tile

### Exceptions

`IndexOutOfBoundsException` - if the given [tileId](get-viewport.md#com.aheidelbacher.algostorm.engine.state.TileSet$getViewport(kotlin.Int)/tileId) is negative or if
it is greater than or equal to [tileCount](tile-count.md)

**Return**
the viewport associated to the requested tile

