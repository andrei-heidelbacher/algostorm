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

/**
 * A system which handles the rendering of all entities in the game.
 */
abstract class AbstractRenderingSystem : Subscriber {
    /**
     * This method should render all the game data to the screen.
     *
     * It will be called from the private engine thread and should be blocking
     * and thread-safe.
     */
    protected abstract fun render(): Unit

    /**
     * When a [Render] event is received, the [render] method is called.
     *
     * @param event the rendering request
     */
    @Subscribe fun handleRender(event: Render) {
        render()
    }
}
