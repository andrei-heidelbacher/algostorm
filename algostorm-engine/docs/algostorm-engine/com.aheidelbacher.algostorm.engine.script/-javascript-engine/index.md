[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.script](../index.md) / [JavascriptEngine](.)

# JavascriptEngine

`class JavascriptEngine : `[`ScriptEngine`](../-script-engine/index.md)

An interpreter of Javascript files using Mozilla Rhino.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `JavascriptEngine(loader: (String) -> `[`InputStream`](http://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`)`<br>An interpreter of Javascript files using Mozilla Rhino. |

### Functions

| Name | Summary |
|---|---|
| [eval](eval.md) | `fun eval(script: String): Unit`<br>Executes the script at the given path. Every variable and function
declaration in this script should be available to future [invokeFunction](invoke-function.md)
calls. |
| [invokeFunction](invoke-function.md) | `fun <T : Any> invokeFunction(functionName: String, returnType: KClass<T>, vararg args: Any?): T?`<br>Executes the script function with the given [functionName](invoke-function.md#com.aheidelbacher.algostorm.engine.script.JavascriptEngine$invokeFunction(kotlin.String, kotlin.reflect.KClass((com.aheidelbacher.algostorm.engine.script.JavascriptEngine.invokeFunction.T)), kotlin.Array((kotlin.Any)))/functionName) with the
specified arguments and returns its result. |

### Companion Object Functions

| Name | Summary |
|---|---|
| [executeWithContext](execute-with-context.md) | `fun <T> executeWithContext(block: Context.() -> T): T` |
