[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine](../index.md) / [Engine](index.md) / [serializeState](.)

# serializeState

`fun serializeState(outputStream: `[`OutputStream`](http://docs.oracle.com/javase/6/docs/api/java/io/OutputStream.html)`): Unit`

Acquires the state lock and calls the [writeStateToStream](write-state-to-stream.md) method.

### Parameters

`outputStream` - the stream to which the game state is written