[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.script](../index.md) / [ScriptingSystem](.)

# ScriptingSystem

`class ScriptingSystem : Subscriber`

A system that handles script execution requests.

### Types

| Name | Summary |
|---|---|
| [RunScript](-run-script/index.md) | `data class RunScript : Event`<br>An event which requests the execution of a script. |
| [RunScriptWithResult](-run-script-with-result/index.md) | `data class RunScriptWithResult : Event`<br>An event which requests the execution of a script, attaching a callback
to the result. |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ScriptingSystem(scriptEngine: `[`ScriptEngine`](../-script-engine/index.md)`, scripts: List<String>)`<br>A system that handles script execution requests. |

### Functions

| Name | Summary |
|---|---|
| [onRunScript](on-run-script.md) | `fun onRunScript(event: `[`RunScript`](-run-script/index.md)`): Unit`<br>Upon receiving a [RunScript](-run-script/index.md), the [ScriptEngine.invokeFunction](../-script-engine/invoke-function.md) method is
called with the supplied arguments. The returned result (if any) is
discarded. |
| [onRunScriptWithResult](on-run-script-with-result.md) | `fun onRunScriptWithResult(event: `[`RunScriptWithResult`](-run-script-with-result/index.md)`): Unit` |
