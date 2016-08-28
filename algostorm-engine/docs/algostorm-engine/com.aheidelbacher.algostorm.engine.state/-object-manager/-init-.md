[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.state](../index.md) / [ObjectManager](index.md) / [&lt;init&gt;](.)

# &lt;init&gt;

`ObjectManager(map: `[`Map`](../-map/index.md)`, name: String)`

A manager which offers easy creation, deletion and retrieval of objects from
a specified [Layer.ObjectGroup](../-layer/-object-group/index.md) of a given [Map](../-map/index.md).

At most one `ObjectManager` should be associated to an `ObjectGroup`. If an
`ObjectManager` is associated to an `ObjectGroup`, the objects should be
referenced only through the `ObjectManager`, otherwise the `ObjectManager`
state might be corrupted.

