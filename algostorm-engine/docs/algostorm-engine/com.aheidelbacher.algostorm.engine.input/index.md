[algostorm-engine](../index.md) / [com.aheidelbacher.algostorm.engine.input](.)

## Package com.aheidelbacher.algostorm.engine.input

### Types

| [AbstractInputSystem](-abstract-input-system/index.md) | `abstract class AbstractInputSystem<in T : Any> : Subscriber`<br>A system which handles user input. |
| [InputReader](-input-reader/index.md) | `interface InputReader<out T : Any>`<br>Allows reading input. |
| [InputSocket](-input-socket/index.md) | `class InputSocket<T : Any> : `[`InputReader`](-input-reader/index.md)`<T>, `[`InputWriter`](-input-writer/index.md)`<T>`<br>Thread-safe input socket which allows setting and retrieving inputs. |
| [InputWriter](-input-writer/index.md) | `interface InputWriter<in T : Any>`<br>Allows writing input. |

