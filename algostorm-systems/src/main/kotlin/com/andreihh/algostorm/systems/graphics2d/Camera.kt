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

package com.andreihh.algostorm.systems.graphics2d

class Camera {
    var width: Int = 0
        private set

    var height: Int = 0
        private set

    var x: Int = 0
        private set

    var y: Int = 0
        private set

    fun focusOn(x: Int, y: Int) {
        this.x = x - width / 2
        this.y = y - height / 2
    }

    fun translate(dx: Int, dy: Int) {
        x += dx
        y += dy
    }

    fun resize(width: Int, height: Int) {
        require(width >= 0)
        require(height >= 0)
        val centerX = x + this.width / 2
        val centerY = y + this.height / 2
        this.width = width
        this.height = height
        focusOn(centerX, centerY)
    }
}
