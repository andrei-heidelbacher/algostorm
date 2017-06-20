/*
 * Copyright 2017 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aheidelbacher.sokoban.desktop

import com.aheidelbacher.algostorm.core.drivers.client.audio.AudioDriver
import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.GraphicsDriver
import com.aheidelbacher.algostorm.core.drivers.client.input.InputDriver
import com.aheidelbacher.algostorm.core.drivers.io.FileSystemDriver
import com.aheidelbacher.algostorm.core.engine.Engine
import com.aheidelbacher.algostorm.desktop.DesktopClient
import com.aheidelbacher.sokoban.core.SokobanEngine

class SokobanClient : DesktopClient() {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            launch(SokobanClient::class.java, *args)
        }
    }

    override fun createEngine(
            audioDriver: AudioDriver,
            graphicsDriver: GraphicsDriver,
            inputDriver: InputDriver,
            fileSystemDriver: FileSystemDriver
    ): Engine = SokobanEngine(
            audioDriver = audioDriver,
            graphicsDriver = graphicsDriver,
            inputDriver = inputDriver,
            fileSystemDriver = fileSystemDriver
    )
}
