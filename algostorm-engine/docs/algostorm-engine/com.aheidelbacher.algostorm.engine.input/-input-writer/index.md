[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.input](../index.md) / [InputWriter](.)

# InputWriter

`interface InputWriter<in T : Any>`

Allows writing input.

### Parameters

`T` - the input type

### Functions

| Name | Summary |
|---|---|
| [writeInput](write-input.md) | `abstract fun writeInput(input: T?): Unit`<br>Writes the given input. |

### Inheritors

| Name | Summary |
|---|---|
| [InputSocket](../-input-socket/index.md) | `class InputSocket<T : Any> : `[`InputReader`](../-input-reader/index.md)`<T>, InputWriter<T>`<br>Thread-safe input socket which allows setting and retrieving inputs. |
