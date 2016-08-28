[algostorm-engine](../../../index.md) / [com.aheidelbacher.algostorm.engine.state](../../index.md) / [TileSet](../index.md) / [Tile](index.md) / [animation](.)

# animation

`val animation: List<`[`Frame`](-frame/index.md)`>?`

a list of frames representing an animation. Must be
`null` (indicating no animation) or must contain at least two frames.

### Property

`animation` - a list of frames representing an animation. Must be
`null` (indicating no animation) or must contain at least two frames.

### Exceptions

`IllegalArgumentException` - if animation is not `null` and
contains less than two frames