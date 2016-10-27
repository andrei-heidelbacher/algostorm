function testProcedure(message) {
    java.lang.System.out.println(message);
}

function testIntFunction(intValue) {
    var value = intValue;
    value += 1;
    value -= 1;
    return value;
}

function testStringFunction(stringValue) {
    var value = stringValue;
    value += "";
    return value;
}

function testScriptResultMockFunction(scriptResultMockValue) {
    var ScriptResultMock = Packages.com.aheidelbacher.algostorm.engine.script.JavascriptDriverTest.ScriptResultMock;
    var id = scriptResultMockValue.id;
    var value = scriptResultMockValue.value;
    return new ScriptResultMock(id, value);
}
