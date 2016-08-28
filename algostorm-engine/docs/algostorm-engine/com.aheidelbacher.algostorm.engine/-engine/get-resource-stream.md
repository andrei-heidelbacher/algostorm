[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine](../index.md) / [Engine](index.md) / [getResourceStream](.)

# getResourceStream

`@JvmStatic fun getResourceStream(name: String): `[`InputStream`](http://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)

Returns the resource file with the given name using the [Engine](index.md)
class [Class.getResource](http://docs.oracle.com/javase/6/docs/api/java/lang/Class.html#getResource(java.lang.String)) method.

### Parameters

`name` - the name of the requested resource

### Exceptions

`FileNotFoundException` - if the given resource doesnt exist

**Return**
the requested resource as a stream

