/*
 * Copyright (c) 2016  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.aheidelbacher.algostorm.state.builders

import com.aheidelbacher.algostorm.state.Layer.TileLayer

class TileLayerBuilder(val width: Int, val height: Int) {
    lateinit var name: String
    var isVisible: Boolean = true
    var offsetX: Int = 0
    var offsetY: Int = 0
    var data: IntArray = IntArray(width * height) { 0 }
        set(value) {
            require(value.size == width * height) {
                "$this invalid data $value size!"
            }
            field = value
        }

    private fun getIndex(x: Int, y: Int): Int {
        if (x !in 0 until width || y !in 0 until height) {
            throw IndexOutOfBoundsException(
                    "$this tile ($x, $y) is out of bounds!"
            )
        }
        return y * width + x
    }

    operator fun get(x: Int, y: Int): Int = data[getIndex(x, y)]

    operator fun set(x: Int, y: Int, value: Int) {
        data[getIndex(x, y)] = value
    }

    fun build(): TileLayer =
            TileLayer(name, isVisible, offsetX, offsetY, data.copyOf())
}
