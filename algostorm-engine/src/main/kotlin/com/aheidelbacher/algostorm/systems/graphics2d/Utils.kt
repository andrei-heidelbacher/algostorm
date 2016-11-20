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

package com.aheidelbacher.algostorm.systems.graphics2d

import com.aheidelbacher.algostorm.ecs.Entity
import com.aheidelbacher.algostorm.state.MapObject
import com.aheidelbacher.algostorm.state.TileSet.Tile.Companion.isFlippedDiagonally
import com.aheidelbacher.algostorm.state.TileSet.Tile.Companion.isFlippedHorizontally
import com.aheidelbacher.algostorm.state.TileSet.Tile.Companion.isFlippedVertically
import com.aheidelbacher.algostorm.state.TileSet.Tile.Frame
import com.aheidelbacher.algostorm.state.TileSet.Viewport

/** The `Sprite` component of this entity. */
val Entity.sprite: Sprite?
    get() = get(Sprite::class)

/** Utility flag. */
val Sprite.isFlippedDiagonally: Boolean
    get() = gid.isFlippedDiagonally

/** Utility flag. */
val Sprite.isFlippedHorizontally: Boolean
    get() = gid.isFlippedHorizontally

/** Utility flag. */
val Sprite.isFlippedVertically: Boolean
    get() = gid.isFlippedVertically

fun MapObject.getViewport(gid: Int, currentTimeMillis: Long): Viewport {
    val tileSet = getTileSet(gid)
    val localTileId = getTileId(gid)
    val animation = tileSet.getTile(localTileId).animation
    val tileId = if (animation == null) {
        localTileId
    } else {
        var elapsedTimeMillis =
                currentTimeMillis % animation.sumBy(Frame::duration)
        var i = 0
        do {
            elapsedTimeMillis -= animation[i].duration
            ++i
        } while (elapsedTimeMillis >= 0)
        animation[i - 1].tileId
    }
    return tileSet.getViewport(tileId)
}
