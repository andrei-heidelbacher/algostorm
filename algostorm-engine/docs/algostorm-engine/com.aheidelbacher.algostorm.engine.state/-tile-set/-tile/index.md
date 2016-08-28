[algostorm-engine](../../../index.md) / [com.aheidelbacher.algostorm.engine.state](../../index.md) / [TileSet](../index.md) / [Tile](.)

# Tile

`data class Tile`

An object containing meta-data associated to this tile.

### Types

| Name | Summary |
|---|---|
| [Frame](-frame/index.md) | `data class Frame`<br>A frame within an animation. |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Tile(animation: List<`[`Frame`](-frame/index.md)`>? = null)`<br>An object containing meta-data associated to this tile. |

### Properties

| Name | Summary |
|---|---|
| [animation](animation.md) | `val animation: List<`[`Frame`](-frame/index.md)`>?`<br>a list of frames representing an animation. Must be
`null` (indicating no animation) or must contain at least two frames. |

### Companion Object Properties

| Name | Summary |
|---|---|
| [isFlippedDiagonally](is-flipped-diagonally.md) | `val Long.isFlippedDiagonally: Boolean`<br>Whether this global tile id is flipped diagonally. |
| [isFlippedHorizontally](is-flipped-horizontally.md) | `val Long.isFlippedHorizontally: Boolean`<br>Whether this global tile id is flipped horizontally. |
| [isFlippedVertically](is-flipped-vertically.md) | `val Long.isFlippedVertically: Boolean`<br>Whether this global tile id is flipped vertically. |

### Companion Object Functions

| Name | Summary |
|---|---|
| [clearFlags](clear-flags.md) | `fun Long.clearFlags(): Int`<br>Clears all flag bits. |
| [flipDiagonally](flip-diagonally.md) | `fun Long.flipDiagonally(): Long`<br>Flips this global tile id diagonally. |
| [flipHorizontally](flip-horizontally.md) | `fun Long.flipHorizontally(): Long`<br>Flips this global tile id horizontally. |
| [flipVertically](flip-vertically.md) | `fun Long.flipVertically(): Long`<br>Flips this global tile id vertically. |
