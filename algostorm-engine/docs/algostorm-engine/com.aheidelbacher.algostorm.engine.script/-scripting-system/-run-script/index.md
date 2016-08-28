[algostorm-engine](../../../index.md) / [com.aheidelbacher.algostorm.engine.script](../../index.md) / [ScriptingSystem](../index.md) / [RunScript](.)

# RunScript

`data class RunScript : Event`

An event which requests the execution of a script.

### Constructors

| [&lt;init&gt;](-init-.md) | `RunScript(functionName: String, vararg args: Any?)``RunScript(functionName: String, args: List<*>)`<br>An event which requests the execution of a script. |

### Properties

| [args](args.md) | `val args: List<*>`<br>the arguments of the script function |
| [functionName](function-name.md) | `val functionName: String`<br>the name of script function that should be
executed |

