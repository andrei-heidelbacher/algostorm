[algostorm-engine](../../../index.md) / [com.aheidelbacher.algostorm.engine.script](../../index.md) / [ScriptingSystem](../index.md) / [RunScriptWithResult](.)

# RunScriptWithResult

`data class RunScriptWithResult : Event`

An event which requests the execution of a script, attaching a callback
to the result.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `RunScriptWithResult(functionName: String, returnType: KClass<*>, vararg args: Any?, onResult: (Any?) -> Unit)``RunScriptWithResult(functionName: String, returnType: KClass<*>, args: List<*>, onResult: (Any?) -> Unit)`<br>An event which requests the execution of a script, attaching a callback
to the result. |

### Properties

| Name | Summary |
|---|---|
| [args](args.md) | `val args: List<*>`<br>the arguments of the script function |
| [functionName](function-name.md) | `val functionName: String`<br>the name of script function that should be
executed |
| [onResult](on-result.md) | `val onResult: (Any?) -> Unit`<br>the callback which will be called with the script
result |
| [returnType](return-type.md) | `val returnType: KClass<*>`<br>the expected type of the result |
