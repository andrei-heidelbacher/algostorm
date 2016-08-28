[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.state](../index.md) / [TileSet](.)

# TileSet

`class TileSet`

A tile set used for rendering. Tiles are indexed starting from `0`,
increasing from left to right and then from top to bottom.

Two tile sets are equal if and only if they have the same [name](name.md).

### Types

| Name | Summary |
|---|---|
| [Tile](-tile/index.md) | `data class Tile`<br>An object containing meta-data associated to this tile. |
| [TileOffset](-tile-offset/index.md) | `data class TileOffset`<br>Indicates an offset which should be applied when rendering any tile from
this tile set. |
| [Viewport](-viewport/index.md) | `data class Viewport`<br>A rectangle projected over the image at the specified location. |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `TileSet(name: String, tileWidth: Int, tileHeight: Int, image: String, imageWidth: Int, imageHeight: Int, margin: Int, spacing: Int, columns: Int, tileCount: Int, tileOffset: `[`TileOffset`](-tile-offset/index.md)` = TileOffset(0, 0), properties: Map<String, Any> = emptyMap(), tileProperties: Map<Int, Map<String, Any>> = emptyMap(), tiles: Map<Int, `[`Tile`](-tile/index.md)`> = emptyMap())`<br>A tile set used for rendering. Tiles are indexed starting from `0`,
increasing from left to right and then from top to bottom. |

### Properties

| Name | Summary |
|---|---|
| [columns](columns.md) | `val columns: Int`<br>the number of tiles per row |
| [image](image.md) | `val image: String`<br>the path to the image represented by this tile set |
| [imageHeight](image-height.md) | `val imageHeight: Int`<br>the height of the image in pixels |
| [imageWidth](image-width.md) | `val imageWidth: Int`<br>the width of the image in pixels |
| [margin](margin.md) | `val margin: Int`<br>the margin in pixels |
| [name](name.md) | `val name: String`<br>the name of this tile set |
| [properties](properties.md) | `val properties: Map<String, Any>`<br>the properties of this tile set |
| [spacing](spacing.md) | `val spacing: Int`<br>the spacing between adjacent tiles in pixels |
| [tileCount](tile-count.md) | `val tileCount: Int`<br>the number of tiles present in this tile set |
| [tileHeight](tile-height.md) | `val tileHeight: Int`<br>the height of each tile in this tile set in pixels |
| [tileOffset](tile-offset.md) | `val tileOffset: `[`TileOffset`](-tile-offset/index.md)<br>the rendering offset which should be applied |
| [tileWidth](tile-width.md) | `val tileWidth: Int`<br>the width of each tile in this tile set in pixels |

### Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | `fun equals(other: Any?): Boolean` |
| [getTile](get-tile.md) | `fun getTile(tileId: Int): `[`Tile`](-tile/index.md)<br>Returns the [Tile](-tile/index.md) with the given tile id. |
| [getTileProperties](get-tile-properties.md) | `fun getTileProperties(tileId: Int): Map<String, Any>`<br>Returns the properties associated to the given tile id. |
| [getViewport](get-viewport.md) | `fun getViewport(tileId: Int): `[`Viewport`](-viewport/index.md)<br>Returns a viewport corresponding to the given tile id, by applying the
appropriate margin and spacing offsets. |
| [hashCode](hash-code.md) | `fun hashCode(): Int` |
