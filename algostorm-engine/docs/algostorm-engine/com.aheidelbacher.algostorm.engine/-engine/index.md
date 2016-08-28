[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine](../index.md) / [Engine](.)

# Engine

`abstract class Engine`

An asynchronous engine that runs the game loop on its own private thread.

All changes to the game state outside of this engines thread may lead to
inconsistent state and concurrency issues. Thus, the engine state should
remain private to the engine and modified only in the [onUpdate](on-update.md) and
[clearState](clear-state.md) methods.

All the engine methods are thread safe as long as the complete construction
of the engine and initialization of the state happen-before any other method
call.

### Types

| [Status](-status/index.md) | `enum class Status`<br>An enum which represents the status of an engine. |

### Constructors

| [&lt;init&gt;](-init-.md) | `Engine()`<br>An asynchronous engine that runs the game loop on its own private thread. |

### Properties

| [isShutdown](is-shutdown.md) | `val isShutdown: Boolean`<br>The current shutdown status of this engine. |
| [millisPerUpdate](millis-per-update.md) | `abstract val millisPerUpdate: Int`<br>The number of milliseconds spent in an update cycle and the resolution of
an atomic time unit. |
| [status](status.md) | `val status: `[`Status`](-status/index.md)<br>The current status of this engine. |

### Functions

| [clearState](clear-state.md) | `abstract fun clearState(): Unit`<br>Clears the current game state for a clean shutdown. The call to this
method is synchronized with the state lock. |
| [onHandleInput](on-handle-input.md) | `abstract fun onHandleInput(): Unit`<br>This method is invoked right before [onUpdate](on-update.md) is called from this
engines thread while this engine is running. The call to this method is
synchronized with the state lock. |
| [onRender](on-render.md) | `abstract fun onRender(): Unit`<br>This method is invoked right after [onUpdate](on-update.md) returns from this engines
thread while this engine is running. The call to this method is
synchronized with the state lock. |
| [onUpdate](on-update.md) | `abstract fun onUpdate(): Unit`<br>This method is invoked at most once every [millisPerUpdate](millis-per-update.md) from this
engines thread while this engine is running. The call to this method is
synchronized with the state lock. |
| [serializeState](serialize-state.md) | `fun serializeState(outputStream: `[`OutputStream`](http://docs.oracle.com/javase/6/docs/api/java/io/OutputStream.html)`): Unit`<br>Acquires the state lock and calls the [writeStateToStream](write-state-to-stream.md) method. |
| [shutdown](shutdown.md) | `fun shutdown(): Unit`<br>[Stops](stop.md) and [clears](clear-state.md) this engine, unsubscribes all its
systems from the event bus and sets the [isShutdown](is-shutdown.md) flag to `true`. |
| [start](start.md) | `fun start(): Unit`<br>Sets the [status](status.md) to [Status.RUNNING](-status/-r-u-n-n-i-n-g.md) and starts the engine thread. The
engine `status` must be [Status.STOPPED](-status/-s-t-o-p-p-e-d.md) at the time of calling. |
| [stop](stop.md) | `fun stop(): Unit`<br>Sets the engine [status](status.md) to [Status.STOPPING](-status/-s-t-o-p-p-i-n-g.md) and then joins the engine
thread to the current thread. If the join succeeds, the `status` will be
set to [Status.STOPPED](-status/-s-t-o-p-p-e-d.md). |
| [writeStateToStream](write-state-to-stream.md) | `abstract fun writeStateToStream(outputStream: `[`OutputStream`](http://docs.oracle.com/javase/6/docs/api/java/io/OutputStream.html)`): Unit`<br>Retrieves the current game state and serializes it to the given stream.
The call to this method is synchronized with the state lock. |

### Companion Object Properties

| [NAME](-n-a-m-e.md) | `const val NAME: String`<br>Name of the engine thread. |

### Companion Object Functions

| [getResourceStream](get-resource-stream.md) | `fun getResourceStream(name: String): `[`InputStream`](http://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)<br>Returns the resource file with the given name using the Engine
class [Class.getResource](http://docs.oracle.com/javase/6/docs/api/java/lang/Class.html#getResource(java.lang.String)) method. |

