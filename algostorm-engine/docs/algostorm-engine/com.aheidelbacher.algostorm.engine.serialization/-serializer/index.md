[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.serialization](../index.md) / [Serializer](.)

# Serializer

`object Serializer`

Serialization and deserialization utility methods.

### Properties

| Name | Summary |
|---|---|
| [FORMAT](-f-o-r-m-a-t.md) | `const val FORMAT: String`<br>The serialization format. |
| [objectMapper](object-mapper.md) | `val objectMapper: <ERROR CLASS>`<br>The object that handles serialization and deserialization. |

### Functions

| Name | Summary |
|---|---|
| [readValue](read-value.md) | `fun <T : Any> readValue(src: `[`InputStream`](http://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`, type: KClass<T>): T`<br>`fun <T : Any> readValue(src: `[`InputStream`](http://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`): T` |
| [writeValue](write-value.md) | `fun writeValue(out: `[`OutputStream`](http://docs.oracle.com/javase/6/docs/api/java/io/OutputStream.html)`, value: Any): Unit` |
