[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.input](../index.md) / [AbstractInputSystem](.)

# AbstractInputSystem

`abstract class AbstractInputSystem<in T : Any> : Subscriber`

A system which handles user input.

### Parameters

`T` - the input type

### Types

| Name | Summary |
|---|---|
| [HandleInput](-handle-input.md) | `object HandleInput : Event`<br>An event which signals that user input should be processed. |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `AbstractInputSystem(inputReader: `[`InputReader`](../-input-reader/index.md)`<T>)`<br>A system which handles user input. |

### Functions

| Name | Summary |
|---|---|
| [handleInput](handle-input.md) | `abstract fun handleInput(input: T): Unit`<br>Handles the most recent input from the [inputReader](#). |
| [onHandleInput](on-handle-input.md) | `fun onHandleInput(event: `[`HandleInput`](-handle-input.md)`): Unit`<br>Upon receiving a [HandleInput](-handle-input.md) event, the [inputReader](#) is checked for
new input and the [handleInput](handle-input.md) method is called. |
