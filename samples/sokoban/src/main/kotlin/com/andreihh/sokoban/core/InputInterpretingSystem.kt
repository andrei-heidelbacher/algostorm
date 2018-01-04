/*
 * Copyright 2018 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.andreihh.sokoban.core

import com.andreihh.algostorm.core.drivers.input.Input
import com.andreihh.algostorm.core.ecs.EntityRef.Id
import com.andreihh.algostorm.systems.graphics2d.Camera
import com.andreihh.algostorm.systems.graphics2d.CameraSystem.Follow
import com.andreihh.algostorm.systems.graphics2d.CameraSystem.Scroll
import com.andreihh.algostorm.systems.graphics2d.GraphicsSystem
import com.andreihh.algostorm.systems.input.InputSystem
import com.andreihh.algostorm.systems.physics2d.PathFindingSystem.FindPath
import com.andreihh.algostorm.systems.physics2d.PhysicsSystem.TransformIntent

class InputInterpretingSystem : InputSystem() {
    private val camera: Camera by context(GraphicsSystem.CAMERA)
    private val tileWidth: Int by context(GraphicsSystem.TILE_WIDTH)
    private val tileHeight: Int by context(GraphicsSystem.TILE_HEIGHT)

    data class ScrollInput(val dx: Int, val dy: Int) : Input

    data class TouchInput(val x: Int, val y: Int) : Input

    override fun onScroll(dx: Int, dy: Int): Input? = ScrollInput(dx, dy)

    override fun onTouch(x: Int, y: Int): Input? = TouchInput(x, y)

    override fun onInput(input: Input) {
        when (input) {
            is ScrollInput -> post(Scroll(input.dx, input.dy))
            is TouchInput -> {
                val tx = (input.x + camera.x) / tileWidth
                val ty = (input.y + camera.y) / tileHeight
                val path = request(FindPath(Id(1), tx, ty))
                path?.let { post(Follow(Id(1))) }
                path?.forEach { d ->
                    post(TransformIntent(Id(1), d.dx, d.dy))
                }
            }
        }
    }
}
