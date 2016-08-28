[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.script](../index.md) / [JavascriptEngine](index.md) / [eval](.)

# eval

`fun eval(script: String): Unit`

Overrides [ScriptEngine.eval](../-script-engine/eval.md)

Executes the script at the given path. Every variable and function
declaration in this script should be available to future [invokeFunction](invoke-function.md)
calls.

### Parameters

`script` - the path where the script is found

### Exceptions

`FileNotFoundException` - if the given script doesnt exist