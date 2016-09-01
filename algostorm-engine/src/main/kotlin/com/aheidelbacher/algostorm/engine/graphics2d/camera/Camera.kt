/*
 * Copyright 2016 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.aheidelbacher.algostorm.engine.graphics2d.camera

import com.aheidelbacher.algostorm.engine.tiled.Map

interface Camera {
    companion object {
        const val CAMERA_X: String = "cameraX"
        const val CAMERA_Y: String = "cameraY"

        fun Map.getCamera(): Camera = object : Camera {
            override var x: Int
                get() = getInt(CAMERA_X)
                        ?: error("Map is missing $CAMERA_X property!")
                set(value) {
                    set(CAMERA_X, value)
                }

            override var y: Int
                get() = getInt(CAMERA_Y)
                        ?: error("Map is missing $CAMERA_Y property!")
                set(value) {
                    set(CAMERA_Y, value)
                }
        }
    }

    var x: Int

    var y: Int
}
