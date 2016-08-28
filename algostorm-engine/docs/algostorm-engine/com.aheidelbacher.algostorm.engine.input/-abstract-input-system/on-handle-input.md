[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.input](../index.md) / [AbstractInputSystem](index.md) / [onHandleInput](.)

# onHandleInput

`@Subscribe fun onHandleInput(event: `[`HandleInput`](-handle-input.md)`): Unit`

Upon receiving a [HandleInput](-handle-input.md) event, the [inputReader](#) is checked for
new input and the [handleInput](handle-input.md) method is called.

### Parameters

`event` - the [HandleInput](-handle-input.md) event.