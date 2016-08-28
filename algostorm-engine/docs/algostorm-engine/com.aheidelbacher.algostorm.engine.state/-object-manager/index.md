[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.state](../index.md) / [ObjectManager](.)

# ObjectManager

`class ObjectManager`

A manager which offers easy creation, deletion and retrieval of objects from
a specified [Layer.ObjectGroup](../-layer/-object-group/index.md) of a given [Map](../-map/index.md).

At most one `ObjectManager` should be associated to an `ObjectGroup`. If an
`ObjectManager` is associated to an `ObjectGroup`, the objects should be
referenced only through the `ObjectManager`, otherwise the `ObjectManager`
state might be corrupted.

### Constructors

| [&lt;init&gt;](-init-.md) | `ObjectManager(map: `[`Map`](../-map/index.md)`, name: String)`<br>A manager which offers easy creation, deletion and retrieval of objects from
a specified [Layer.ObjectGroup](../-layer/-object-group/index.md) of a given [Map](../-map/index.md). |

### Properties

| [objects](objects.md) | `val objects: List<`[`Object`](../-object/index.md)`>`<br>A lazy view of all the objects in the associated object group. |

### Functions

| [contains](contains.md) | `operator fun contains(objectId: Int): Boolean`<br>Checks whether this object group contains an object with the given id. |
| [create](create.md) | `fun create(name: String = "", type: String = "", x: Int, y: Int, width: Int, height: Int, gid: Long = 0L, rotation: Float = 0F, visible: Boolean = true, properties: MutableMap<String, Any> = hashMapOf()): `[`Object`](../-object/index.md)<br>Creates an object with the specified parameters, adds it to this object
group and returns it. |
| [delete](delete.md) | `fun delete(objectId: Int): Boolean`<br>Deletes the object with the given id. |
| [get](get.md) | `operator fun get(objectId: Int): `[`Object`](../-object/index.md)`?`<br>Returns the object with the given id. |

