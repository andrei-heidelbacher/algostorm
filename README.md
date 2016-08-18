## Algostorm

[![](https://jitpack.io/v/andrei-heidelbacher/algostorm.svg)](https://jitpack.io/#andrei-heidelbacher/algostorm)
[![Build Status](https://travis-ci.org/andrei-heidelbacher/algostorm.png)](https://travis-ci.org/andrei-heidelbacher/algostorm)
[![License](http://img.shields.io/:license-apache-blue.svg?style=flat-square)](http://www.apache.org/licenses/LICENSE-2.0.html)

Algostorm is a 2d grid-based game engine for the JVM. It is designed for
turn-based games, but can be adapted for real-time with a little effort.

### Features
* a property-based entity-system framework
* event-based inter-system communication along with an annotation processor to
type-check the event subscribers
* engine that runs on its own private thread
* default physics, in-game timers, graphics and animation utilities
* asset management (tile sets, sounds, scripts)
* thread-safe input bridge between raw user input and game-specific commands
* JSON serialization utilities
* supports reading Tiled JSON maps

### Using Algostorm
To use the engine, you need the following dependencies:
* kotlin-stdlib-1.0.3 or newer
* kotlin-reflect-1.0.3 or newer
* jackson-module-kotlin-2.8.1 or newer
* mozilla-rhino-1.7.7.1 or newer

You may download the latest release [here](https://github.com/andrei-heidelbacher/algostorm/releases).

Alternatively, you may fetch the jar along with all its dependencies remotely
from [jitpack](https://jitpack.io). The github repository can be found
[here](https://github.com/andrei-heidelbacher/algostorm).

### Licensing
The engine is released under the Apache V2.0 License.
