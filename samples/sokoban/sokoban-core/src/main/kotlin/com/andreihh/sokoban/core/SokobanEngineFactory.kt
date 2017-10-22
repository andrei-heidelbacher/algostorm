/*
 * Copyright (c) 2017  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andreihh.sokoban.core

import com.andreihh.algostorm.core.drivers.audio.AudioDriver
import com.andreihh.algostorm.core.drivers.graphics2d.GraphicsDriver
import com.andreihh.algostorm.core.drivers.input.InputDriver
import com.andreihh.algostorm.core.drivers.io.FileSystemDriver
import com.andreihh.algostorm.core.engine.EngineFactory

class SokobanEngineFactory : EngineFactory {
    override fun create(
            audioDriver: AudioDriver,
            graphicsDriver: GraphicsDriver,
            inputDriver: InputDriver,
            fileSystemDriver: FileSystemDriver
    ): SokobanEngine = SokobanEngine(
            audioDriver = audioDriver,
            graphicsDriver = graphicsDriver,
            inputDriver = inputDriver,
            fileSystemDriver = fileSystemDriver
    )
}
