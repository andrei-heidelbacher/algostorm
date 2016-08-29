[algostorm-test](../../index.md) / [com.aheidelbacher.algostorm.test.engine.script](../index.md) / [ScriptEngineTest](.)

# ScriptEngineTest

`abstract class ScriptEngineTest`

An abstract test class for a [ScriptEngine](#).

In order to test common functionality to all script engines, you may
implement this class and provide a concrete script engine instance to test.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ScriptEngineTest(scriptEngine: ScriptEngine, scriptPaths: List<String>, voidFunctionName: String, resultFunctionName: String)`<br>An abstract test class for a [ScriptEngine](#). |

### Properties

| Name | Summary |
|---|---|
| [resultFunctionName](result-function-name.md) | `val resultFunctionName: String`<br>the name of a function which should receive an
integer `id` and a string `value` and return a [ScriptResult](../-script-result/index.md) with the same
`id` and `value` as the received parameters |
| [scriptEngine](script-engine.md) | `val scriptEngine: ScriptEngine`<br>the script engine instance that should be tested |
| [scriptPaths](script-paths.md) | `val scriptPaths: List<String>`<br>the paths to the scripts which will be evaluated before
any tests are run |
| [voidFunctionName](void-function-name.md) | `val voidFunctionName: String`<br>the name of a function which shouldnt receive any
parameters and should return void |

### Functions

| Name | Summary |
|---|---|
| [evalScripts](eval-scripts.md) | `fun evalScripts(): Unit` |
| [invokeFunction](invoke-function.md) | `fun invokeFunction(functionName: String, returnType: KClass<*>, vararg args: Any?): Any?`<br>Utility method that delegates the call to the [scriptEngine](script-engine.md). |
| [testResultScript](test-result-script.md) | `fun testResultScript(): Unit` |
| [testVoidScript](test-void-script.md) | `fun testVoidScript(): Unit` |
