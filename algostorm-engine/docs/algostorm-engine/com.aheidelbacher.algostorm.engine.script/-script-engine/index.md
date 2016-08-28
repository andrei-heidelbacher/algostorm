[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.script](../index.md) / [ScriptEngine](.)

# ScriptEngine

`interface ScriptEngine`

An object that can evaluate scripts and named functions from previously
evaluated scripts.

### Functions

| Name | Summary |
|---|---|
| [eval](eval.md) | `abstract fun eval(script: String): Unit`<br>Executes the script at the given path. Every variable and function
declaration in this script should be available to future [invokeFunction](invoke-function.md)
calls. |
| [invokeFunction](invoke-function.md) | `abstract fun <T : Any> invokeFunction(functionName: String, returnType: KClass<T>, vararg args: Any?): T?`<br>Executes the script function with the given [functionName](invoke-function.md#com.aheidelbacher.algostorm.engine.script.ScriptEngine$invokeFunction(kotlin.String, kotlin.reflect.KClass((com.aheidelbacher.algostorm.engine.script.ScriptEngine.invokeFunction.T)), kotlin.Array((kotlin.Any)))/functionName) with the
specified arguments and returns its result. |

### Companion Object Functions

| Name | Summary |
|---|---|
| [invokeFunction](invoke-function.md) | `fun <T : Any> ScriptEngine.invokeFunction(functionName: String, vararg args: Any?): T?`<br>Executes the script function with the given [functionName](invoke-function.md#com.aheidelbacher.algostorm.engine.script.ScriptEngine.Companion$invokeFunction(com.aheidelbacher.algostorm.engine.script.ScriptEngine, kotlin.String, kotlin.Array((kotlin.Any)))/functionName) with
the specified arguments and returns its result. |

### Companion Object Extension Functions

| Name | Summary |
|---|---|
| [invokeFunction](invoke-function.md) | `fun <T : Any> ScriptEngine.invokeFunction(functionName: String, vararg args: Any?): T?`<br>Executes the script function with the given [functionName](invoke-function.md#com.aheidelbacher.algostorm.engine.script.ScriptEngine.Companion$invokeFunction(com.aheidelbacher.algostorm.engine.script.ScriptEngine, kotlin.String, kotlin.Array((kotlin.Any)))/functionName) with
the specified arguments and returns its result. |

### Inheritors

| Name | Summary |
|---|---|
| [JavascriptEngine](../-javascript-engine/index.md) | `class JavascriptEngine : ScriptEngine`<br>An interpreter of Javascript files using Mozilla Rhino. |
