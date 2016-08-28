[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.input](../index.md) / [InputReader](.)

# InputReader

`interface InputReader<out T : Any>`

Allows reading input.

### Parameters

`T` - the input type

### Functions

| [readInput](read-input.md) | `abstract fun readInput(): T?`<br>Retrieves the most recent input and resets the last input to `null`. |

### Inheritors

| [InputSocket](../-input-socket/index.md) | `class InputSocket<T : Any> : InputReader<T>, `[`InputWriter`](../-input-writer/index.md)`<T>`<br>Thread-safe input socket which allows setting and retrieving inputs. |

