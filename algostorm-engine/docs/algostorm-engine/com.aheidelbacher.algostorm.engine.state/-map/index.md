[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.state](../index.md) / [Map](.)

# Map

`class Map`

A map which contains all the game state.

The x-axis is increasing from left to right and the y-axis is increasing from
top to bottom.

### Types

| [Orientation](-orientation/index.md) | `enum class Orientation`<br>The orientation of the map. |
| [RenderOrder](-render-order/index.md) | `enum class RenderOrder`<br>The rendering order of tiles. |

### Constructors

| [&lt;init&gt;](-init-.md) | `Map(width: Int, height: Int, tileWidth: Int, tileHeight: Int, orientation: `[`Orientation`](-orientation/index.md)`, renderOrder: `[`RenderOrder`](-render-order/index.md)` = RenderOrder.RIGHT_DOWN, tileSets: List<`[`TileSet`](../-tile-set/index.md)`>, layers: List<`[`Layer`](../-layer/index.md)`>, properties: MutableMap<String, Any> = hashMapOf(), backgroundColor: String? = null, version: Float = 1F, nextObjectId: Int)`<br>A map which contains all the game state. |

### Properties

| [backgroundColor](background-color.md) | `val backgroundColor: String?`<br>the color of the map background given in the
"#AARRGGBB" format (base 16, case insensitive) |
| [height](height.md) | `val height: Int`<br>the height of the map in tiles |
| [layers](layers.md) | `val layers: List<`[`Layer`](../-layer/index.md)`>`<br>the layers of the game |
| [orientation](orientation.md) | `val orientation: `[`Orientation`](-orientation/index.md)<br>the orientation of the map |
| [properties](properties.md) | `val properties: MutableMap<String, Any>`<br>the properties of this map |
| [renderOrder](render-order.md) | `val renderOrder: `[`RenderOrder`](-render-order/index.md)<br>the order in which objects and tiles are rendered |
| [tileHeight](tile-height.md) | `val tileHeight: Int`<br>the height of a tile in pixels |
| [tileSets](tile-sets.md) | `val tileSets: List<`[`TileSet`](../-tile-set/index.md)`>`<br>the tile sets used for rendering |
| [tileWidth](tile-width.md) | `val tileWidth: Int`<br>the width of a tile in pixels |
| [version](version.md) | `val version: Float`<br>the version of this map |
| [width](width.md) | `val width: Int`<br>the width of the map in tiles |

### Functions

| [getNextObjectId](get-next-object-id.md) | `fun getNextObjectId(): Int`<br>Returns the next available object id and increments [nextObjectId](#). |
| [getTileId](get-tile-id.md) | `fun getTileId(gid: Long): Int`<br>Returns the local tile id of the given [gid](get-tile-id.md#com.aheidelbacher.algostorm.engine.state.Map$getTileId(kotlin.Long)/gid). |
| [getTileSet](get-tile-set.md) | `fun getTileSet(gid: Long): `[`TileSet`](../-tile-set/index.md)<br>Returns the tile set which contains the given [gid](get-tile-set.md#com.aheidelbacher.algostorm.engine.state.Map$getTileSet(kotlin.Long)/gid). |

### Companion Object Functions

| [validateColor](validate-color.md) | `fun validateColor(color: String?): Unit`<br>Validates the given color. |

### Companion Object Extension Functions

| [getCamera](../../com.aheidelbacher.algostorm.engine.graphics2d.camera/-camera/get-camera.md) | `fun Map.getCamera(): `[`Camera`](../../com.aheidelbacher.algostorm.engine.graphics2d.camera/-camera/index.md) |
| [getViewport](../../com.aheidelbacher.algostorm.engine.graphics2d/-rendering-system/get-viewport.md) | `fun Map.getViewport(gid: Long, currentTimeMillis: Long): `[`Viewport`](../-tile-set/-viewport/index.md) |

