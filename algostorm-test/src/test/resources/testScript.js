function getResult(id, value) {
    var Result = Packages.com.aheidelbacher.algostorm.test.script.ScriptResult;
    return new Result(id, value);
};

function voidFunction() {
    var sum = 0;
    for (var i = 0; i < 1000; i++) {
        sum += i;
    }
};
