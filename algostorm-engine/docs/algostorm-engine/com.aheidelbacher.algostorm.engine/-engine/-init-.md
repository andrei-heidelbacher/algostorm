[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine](../index.md) / [Engine](index.md) / [&lt;init&gt;](.)

# &lt;init&gt;

`Engine()`

An asynchronous engine that runs the game loop on its own private thread.

All changes to the game state outside of this engines thread may lead to
inconsistent state and concurrency issues. Thus, the engine state should
remain private to the engine and modified only in the [onUpdate](on-update.md) and
[clearState](clear-state.md) methods.

All the engine methods are thread safe as long as the complete construction
of the engine and initialization of the state happen-before any other method
call.

