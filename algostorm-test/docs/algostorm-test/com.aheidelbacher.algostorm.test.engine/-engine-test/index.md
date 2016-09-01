[algostorm-test](../../index.md) / [com.aheidelbacher.algostorm.test.engine](../index.md) / [EngineTest](.)

# EngineTest

`abstract class EngineTest`

An abstract test class for an [Engine](#).

In order to test common functionality to all engines, you may implement this
class and provide a concrete engine instance to test.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `EngineTest(engine: Engine)`<br>An abstract test class for an [Engine](#). |

### Properties

| Name | Summary |
|---|---|
| [engine](engine.md) | `val engine: Engine`<br>the engine instance that should be tested |

### Functions

| Name | Summary |
|---|---|
| [getElapsedFrames](get-elapsed-frames.md) | `abstract fun getElapsedFrames(): Int` |
| [testAverageFpsShouldEqualTargetFps](test-average-fps-should-equal-target-fps.md) | `fun testAverageFpsShouldEqualTargetFps(): Unit` |
| [testGetNonExistingResourceStreamShouldThrow](test-get-non-existing-resource-stream-should-throw.md) | `fun testGetNonExistingResourceStreamShouldThrow(): Unit` |
| [testRunOneSecondShouldNotThrow](test-run-one-second-should-not-throw.md) | `fun testRunOneSecondShouldNotThrow(): Unit` |
| [testShutdownTwiceShouldThrow](test-shutdown-twice-should-throw.md) | `fun testShutdownTwiceShouldThrow(): Unit` |
| [testStartAndInstantShutdown](test-start-and-instant-shutdown.md) | `fun testStartAndInstantShutdown(): Unit` |
| [testStartAndInstantStop](test-start-and-instant-stop.md) | `fun testStartAndInstantStop(): Unit` |
| [testStartTwiceShouldThrow](test-start-twice-should-throw.md) | `fun testStartTwiceShouldThrow(): Unit` |
| [testStopMultipleTimesShouldNotThrow](test-stop-multiple-times-should-not-throw.md) | `fun testStopMultipleTimesShouldNotThrow(): Unit` |

### Companion Object Properties

| Name | Summary |
|---|---|
| [FPS_TOLERANCE](-f-p-s_-t-o-l-e-r-a-n-c-e.md) | `const val FPS_TOLERANCE: Double` |
| [MAX_TIME_LIMIT](-m-a-x_-t-i-m-e_-l-i-m-i-t.md) | `const val MAX_TIME_LIMIT: Long` |
