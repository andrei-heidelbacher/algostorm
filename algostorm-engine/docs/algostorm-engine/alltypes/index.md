

### All Types

| Name | Summary |
|---|---|
| [com.aheidelbacher.algostorm.engine.input.AbstractInputSystem](../com.aheidelbacher.algostorm.engine.input/-abstract-input-system/index.md) | A system which handles user input. |
| [com.aheidelbacher.algostorm.engine.graphics2d.camera.Camera](../com.aheidelbacher.algostorm.engine.graphics2d.camera/-camera/index.md) |  |
| [com.aheidelbacher.algostorm.engine.graphics2d.camera.CameraSystem](../com.aheidelbacher.algostorm.engine.graphics2d.camera/-camera-system/index.md) |  |
| [com.aheidelbacher.algostorm.engine.graphics2d.Canvas](../com.aheidelbacher.algostorm.engine.graphics2d/-canvas/index.md) | A canvas that allows `draw` calls. |
| [com.aheidelbacher.algostorm.engine.geometry2d.Circle](../com.aheidelbacher.algostorm.engine.geometry2d/-circle/index.md) |  |
| [com.aheidelbacher.algostorm.engine.physics2d.Collision](../com.aheidelbacher.algostorm.engine.physics2d/-collision/index.md) | An event which signals that [sourceId](../com.aheidelbacher.algostorm.engine.physics2d/-collision/source-id.md) collided with [targetId](../com.aheidelbacher.algostorm.engine.physics2d/-collision/target-id.md). |
| [com.aheidelbacher.algostorm.engine.graphics2d.Color](../com.aheidelbacher.algostorm.engine.graphics2d/-color/index.md) | Utility methods for manipulating colors. |
| [com.aheidelbacher.algostorm.engine.Engine](../com.aheidelbacher.algostorm.engine/-engine/index.md) | An asynchronous engine that runs the game loop on its own private thread. |
| [com.aheidelbacher.algostorm.engine.input.InputReader](../com.aheidelbacher.algostorm.engine.input/-input-reader/index.md) | Allows reading input. |
| [com.aheidelbacher.algostorm.engine.input.InputSocket](../com.aheidelbacher.algostorm.engine.input/-input-socket/index.md) | Thread-safe input socket which allows setting and retrieving inputs. |
| [com.aheidelbacher.algostorm.engine.input.InputWriter](../com.aheidelbacher.algostorm.engine.input/-input-writer/index.md) | Allows writing input. |
| [com.aheidelbacher.algostorm.engine.script.JavascriptEngine](../com.aheidelbacher.algostorm.engine.script/-javascript-engine/index.md) | An interpreter of Javascript files using Mozilla Rhino. |
| [com.aheidelbacher.algostorm.engine.state.Layer](../com.aheidelbacher.algostorm.engine.state/-layer/index.md) | An abstract layer in the game world. |
| [com.aheidelbacher.algostorm.engine.log.Logger](../com.aheidelbacher.algostorm.engine.log/-logger/index.md) | An object which provides logging facilities. |
| [com.aheidelbacher.algostorm.engine.log.LoggingSystem](../com.aheidelbacher.algostorm.engine.log/-logging-system/index.md) | A system which captures all events and logs them. |
| [com.aheidelbacher.algostorm.engine.state.Map](../com.aheidelbacher.algostorm.engine.state/-map/index.md) | A map which contains all the game state. |
| [com.aheidelbacher.algostorm.engine.graphics2d.Matrix](../com.aheidelbacher.algostorm.engine.graphics2d/-matrix/index.md) | Used to apply a sequence of transformations to a bitmap before drawing to the
canvas. |
| [com.aheidelbacher.algostorm.engine.state.Object](../com.aheidelbacher.algostorm.engine.state/-object/index.md) | A physical and renderable object in the game. Two objects are equal if and
only if they have the same [id](../com.aheidelbacher.algostorm.engine.state/-object/id.md). |
| [com.aheidelbacher.algostorm.engine.state.ObjectManager](../com.aheidelbacher.algostorm.engine.state/-object-manager/index.md) | A manager which offers easy creation, deletion and retrieval of objects from
a specified [Layer.ObjectGroup](../com.aheidelbacher.algostorm.engine.state/-layer/-object-group/index.md) of a given [Map](../com.aheidelbacher.algostorm.engine.state/-map/index.md). |
| [com.aheidelbacher.algostorm.engine.physics2d.PhysicsSystem](../com.aheidelbacher.algostorm.engine.physics2d/-physics-system/index.md) | A system that handles [TransformIntent](../com.aheidelbacher.algostorm.engine.physics2d/-physics-system/-transform-intent/index.md) events and publishes [Transformed](../com.aheidelbacher.algostorm.engine.physics2d/-transformed/index.md)
and [Collision](../com.aheidelbacher.algostorm.engine.physics2d/-collision/index.md) events. |
| [com.aheidelbacher.algostorm.engine.geometry2d.Point](../com.aheidelbacher.algostorm.engine.geometry2d/-point/index.md) |  |
| [com.aheidelbacher.algostorm.engine.geometry2d.Rectangle](../com.aheidelbacher.algostorm.engine.geometry2d/-rectangle/index.md) | A rectangle which covers the area `[x, x + width - 1] x [y, y + height - 1]`. |
| [com.aheidelbacher.algostorm.engine.time.RegisterTimer](../com.aheidelbacher.algostorm.engine.time/-register-timer/index.md) | An event which requests the creation of a [timer](../com.aheidelbacher.algostorm.engine.time/-register-timer/timer.md). |
| [com.aheidelbacher.algostorm.engine.graphics2d.RenderingSystem](../com.aheidelbacher.algostorm.engine.graphics2d/-rendering-system/index.md) | A system which handles the rendering of all objects in the game to the screen
canvas. |
| [com.aheidelbacher.algostorm.engine.script.ScriptEngine](../com.aheidelbacher.algostorm.engine.script/-script-engine/index.md) | An object that can evaluate scripts and named functions from previously
evaluated scripts. |
| [com.aheidelbacher.algostorm.engine.script.ScriptingSystem](../com.aheidelbacher.algostorm.engine.script/-scripting-system/index.md) | A system that handles script execution requests. |
| [com.aheidelbacher.algostorm.engine.serialization.Serializer](../com.aheidelbacher.algostorm.engine.serialization/-serializer/index.md) | Serialization and deserialization utility methods. |
| [com.aheidelbacher.algostorm.engine.sound.SoundEngine](../com.aheidelbacher.algostorm.engine.sound/-sound-engine/index.md) | An object that can play multiple sounds at once. |
| [com.aheidelbacher.algostorm.engine.sound.SoundSystem](../com.aheidelbacher.algostorm.engine.sound/-sound-system/index.md) | A system which handles playing and stopping sounds. |
| [com.aheidelbacher.algostorm.engine.time.Tick](../com.aheidelbacher.algostorm.engine.time/-tick/index.md) | An event which signals an atomic time unit has passed. |
| [com.aheidelbacher.algostorm.engine.state.TileSet](../com.aheidelbacher.algostorm.engine.state/-tile-set/index.md) | A tile set used for rendering. Tiles are indexed starting from `0`,
increasing from left to right and then from top to bottom. |
| [com.aheidelbacher.algostorm.engine.time.TimeSystem](../com.aheidelbacher.algostorm.engine.time/-time-system/index.md) | A system that triggers every registered [Timer](../com.aheidelbacher.algostorm.engine.time/-timer/index.md) when it expires. |
| [com.aheidelbacher.algostorm.engine.time.Timeline](../com.aheidelbacher.algostorm.engine.time/-timeline/index.md) | A container for the timers in the game. |
| [com.aheidelbacher.algostorm.engine.time.Timer](../com.aheidelbacher.algostorm.engine.time/-timer/index.md) | A timer which will post the associated [events](../com.aheidelbacher.algostorm.engine.time/-timer/events.md) when [remainingTicks](../com.aheidelbacher.algostorm.engine.time/-timer/remaining-ticks.md) ticks
have elapsed. |
| [com.aheidelbacher.algostorm.engine.physics2d.Transformed](../com.aheidelbacher.algostorm.engine.physics2d/-transformed/index.md) | An event which signals that the given object has been transformed. |
| [com.aheidelbacher.algostorm.engine.Update](../com.aheidelbacher.algostorm.engine/-update/index.md) | An event which signals that all game logic should advance by an atomic time
unit. It is recommended to not change the game state after this event has
been processed by its subscribers. |
