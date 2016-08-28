[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine](../index.md) / [Engine](index.md) / [writeStateToStream](.)

# writeStateToStream

`protected abstract fun writeStateToStream(outputStream: `[`OutputStream`](http://docs.oracle.com/javase/6/docs/api/java/io/OutputStream.html)`): Unit`

Retrieves the current game state and serializes it to the given stream.
The call to this method is synchronized with the state lock.

### Parameters

`outputStream` - the stream to which the game state is written