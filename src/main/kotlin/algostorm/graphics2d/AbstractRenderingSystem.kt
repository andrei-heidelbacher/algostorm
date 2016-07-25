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

package algostorm.graphics2d

import algostorm.event.Subscribe
import algostorm.event.Subscriber
import algostorm.state.Layer
import algostorm.state.Map
import algostorm.state.TileSet.Viewport

/**
 * A system which handles the rendering of all entities in the game.
 */
abstract class AbstractRenderingSystem(protected val map: Map) : Subscriber {
    /**
     * This method should render the viewport projected on the indicated bitmap.
     *
     * It will be called from the private engine thread and should be blocking
     * and thread-safe.
     */
    protected abstract fun renderBitmap(viewport: Viewport): Unit

    protected abstract val cameraX: Int

    protected abstract val cameraY: Int

    /**
     * When a [Render] event is received, the [renderBitmap] method is called
     * for every tile, image and renderable object in the game.
     *
     * @param event the rendering request
     */
    @Subscribe fun handleRender(event: Render) {
        map.layers.filter { it.isVisible }.forEach { layer ->
            when (layer) {
                is Layer.ImageLayer -> {

                }
                is Layer.TileLayer -> {

                }
                is Layer.ObjectGroup -> {
                    layer.objects.filter {
                        it.isVisible && it.gid != 0
                    }.forEach {
                        val tileSet = map.getTileSet(it.gid)
                                ?: error("Invalid gid ${it.gid}!")
                        val tileId = map.getTileId(it.gid)
                                ?: error("Invalid gid ${it.gid}!")
                        renderBitmap(tileSet.getViewport(tileId))
                    }
                }
            }
        }
    }
}
