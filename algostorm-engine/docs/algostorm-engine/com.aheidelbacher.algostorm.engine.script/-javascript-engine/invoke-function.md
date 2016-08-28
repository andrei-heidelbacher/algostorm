[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.script](../index.md) / [JavascriptEngine](index.md) / [invokeFunction](.)

# invokeFunction

`fun <T : Any> invokeFunction(functionName: String, returnType: KClass<T>, vararg args: Any?): T?`

Overrides [ScriptEngine.invokeFunction](../-script-engine/invoke-function.md)

Executes the script function with the given [functionName](invoke-function.md#com.aheidelbacher.algostorm.engine.script.JavascriptEngine$invokeFunction(kotlin.String, kotlin.reflect.KClass((com.aheidelbacher.algostorm.engine.script.JavascriptEngine.invokeFunction.T)), kotlin.Array((kotlin.Any)))/functionName) with the
specified arguments and returns its result.

### Parameters

`functionName` - the name of the script function that should be
executed

`returnType` - the expected type of the result

`args` - the script function parameters

### Exceptions

`IllegalArgumentException` - if the given [functionName](invoke-function.md#com.aheidelbacher.algostorm.engine.script.JavascriptEngine$invokeFunction(kotlin.String, kotlin.reflect.KClass((com.aheidelbacher.algostorm.engine.script.JavascriptEngine.invokeFunction.T)), kotlin.Array((kotlin.Any)))/functionName) is not
available to this engine

`ClassCastException` - if the result couldnt be converted to the
[returnType](invoke-function.md#com.aheidelbacher.algostorm.engine.script.JavascriptEngine$invokeFunction(kotlin.String, kotlin.reflect.KClass((com.aheidelbacher.algostorm.engine.script.JavascriptEngine.invokeFunction.T)), kotlin.Array((kotlin.Any)))/returnType)

**Return**
the script result, or `null` if it doesnt return anything.

