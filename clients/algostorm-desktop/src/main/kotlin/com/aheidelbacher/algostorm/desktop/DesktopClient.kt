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

package com.aheidelbacher.algostorm.desktop

import com.aheidelbacher.algostorm.core.engine.Engine
import com.aheidelbacher.algostorm.core.engine.audio.AudioDriver
import com.aheidelbacher.algostorm.core.engine.graphics2d.GraphicsDriver
import com.aheidelbacher.algostorm.core.engine.input.InputDriver

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.layout.StackPane
import javafx.stage.Stage

abstract class DesktopClient : Application() {
    abstract fun createEngine(
            audioDriver: AudioDriver,
            graphicsDriver: GraphicsDriver,
            inputDriver: InputDriver
    ): Engine

    override fun start(primaryStage: Stage) {
        val root = StackPane()
        val canvas = Canvas()
        root.children.add(canvas)
        canvas.widthProperty().bind(root.widthProperty())
        canvas.heightProperty().bind(root.heightProperty())
        val engine = createEngine(
                audioDriver = DesktopAudioDriver(),
                graphicsDriver = DesktopGraphicsDriver(canvas),
                inputDriver = DesktopInputDriver()
        )
        primaryStage.scene = Scene(root, 600.0, 600.0)
        primaryStage.show()
        engine.init(null)
        engine.start()
    }
}
