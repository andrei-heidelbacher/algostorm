[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.input](../index.md) / [InputSocket](.)

# InputSocket

`class InputSocket<T : Any> : `[`InputReader`](../-input-reader/index.md)`<T>, `[`InputWriter`](../-input-writer/index.md)`<T>`

Thread-safe input socket which allows setting and retrieving inputs.

### Parameters

`T` - the user lastInput type

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `InputSocket()`<br>Thread-safe input socket which allows setting and retrieving inputs. |

### Functions

| Name | Summary |
|---|---|
| [readInput](read-input.md) | `fun readInput(): T?`<br>Retrieves the most recent input and resets the last input to `null`. |
| [writeInput](write-input.md) | `fun writeInput(input: T?): Unit`<br>Writes the given input. |
