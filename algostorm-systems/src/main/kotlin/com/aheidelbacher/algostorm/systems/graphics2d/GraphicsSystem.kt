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

package com.aheidelbacher.algostorm.systems.graphics2d

import com.aheidelbacher.algostorm.systems.EventSystem

abstract class GraphicsSystem : EventSystem() {
    companion object {
        const val TILE_WIDTH: String = "TILE_WIDTH"
        const val TILE_HEIGHT: String = "TILE_HEIGHT"
        const val TILE_SET_COLLECTION: String = "TILE_SET_COLLECTION"
        const val CAMERA: String = "CAMERA"
        const val CANVAS: String = "CANVAS"
    }

    protected val tileWidth: Int by context(TILE_WIDTH)
    protected val tileHeight: Int by context(TILE_HEIGHT)
    protected val tileSetCollection: TileSetCollection
            by context(TILE_SET_COLLECTION)
}
