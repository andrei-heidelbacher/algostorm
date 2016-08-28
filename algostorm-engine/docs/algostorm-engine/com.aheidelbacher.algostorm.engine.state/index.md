[algostorm-engine](../index.md) / [com.aheidelbacher.algostorm.engine.state](.)

## Package com.aheidelbacher.algostorm.engine.state

### Types

| Name | Summary |
|---|---|
| [Layer](-layer/index.md) | `sealed class Layer`<br>An abstract layer in the game world. |
| [Map](-map/index.md) | `class Map`<br>A map which contains all the game state. |
| [Object](-object/index.md) | `class Object`<br>A physical and renderable object in the game. Two objects are equal if and
only if they have the same [id](-object/id.md). |
| [ObjectManager](-object-manager/index.md) | `class ObjectManager`<br>A manager which offers easy creation, deletion and retrieval of objects from
a specified [Layer.ObjectGroup](-layer/-object-group/index.md) of a given [Map](-map/index.md). |
| [TileSet](-tile-set/index.md) | `class TileSet`<br>A tile set used for rendering. Tiles are indexed starting from `0`,
increasing from left to right and then from top to bottom. |
